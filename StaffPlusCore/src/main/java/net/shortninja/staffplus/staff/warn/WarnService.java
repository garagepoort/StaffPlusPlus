package net.shortninja.staffplus.staff.warn;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.exceptions.BusinessException;
import net.shortninja.staffplus.event.warnings.WarningCreatedEvent;
import net.shortninja.staffplus.event.warnings.WarningThresholdReachedEvent;
import net.shortninja.staffplus.event.warnings.WarningsClearedEvent;
import net.shortninja.staffplus.player.PlayerManager;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.infractions.Infraction;
import net.shortninja.staffplus.staff.infractions.InfractionCount;
import net.shortninja.staffplus.staff.infractions.InfractionProvider;
import net.shortninja.staffplus.staff.infractions.InfractionType;
import net.shortninja.staffplus.staff.warn.config.WarningAction;
import net.shortninja.staffplus.staff.warn.config.WarningSeverityConfiguration;
import net.shortninja.staffplus.staff.warn.config.WarningThresholdConfiguration;
import net.shortninja.staffplus.staff.delayedactions.DelayedActionsRepository;
import net.shortninja.staffplus.staff.warn.database.WarnRepository;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.*;

import static net.shortninja.staffplus.staff.warn.config.WarningActionRunStrategy.*;
import static org.bukkit.Bukkit.getScheduler;

public class WarnService implements InfractionProvider {

    private final PermissionHandler permission;
    private final MessageCoordinator message;
    private final Options options;
    private final Messages messages;
    private final PlayerManager playerManager;
    private final WarnRepository warnRepository;
    private final DelayedActionsRepository delayedActionsRepository;

    public WarnService(PermissionHandler permission,
                       MessageCoordinator message,
                       Options options,
                       Messages messages,
                       PlayerManager playerManager,
                       WarnRepository warnRepository,
                       DelayedActionsRepository delayedActionsRepository) {
        this.permission = permission;
        this.message = message;
        this.options = options;
        this.messages = messages;
        this.playerManager = playerManager;
        this.warnRepository = warnRepository;
        this.delayedActionsRepository = delayedActionsRepository;
    }

    public void sendWarning(CommandSender sender, SppPlayer user, String reason, String severityLevel) {
        WarningSeverityConfiguration severity = options.warningConfiguration.getSeverityLevels().stream()
            .filter(s -> s.getName().equalsIgnoreCase(severityLevel))
            .findFirst()
            .orElseThrow(() -> new BusinessException("Cannot find severity level: [" + severityLevel + "]", messages.prefixWarnings));

        String issuerName = sender instanceof Player ? sender.getName() : "Console";
        UUID issuerUuid = sender instanceof Player ? ((Player) sender).getUniqueId() : StaffPlus.get().consoleUUID;
        Warning warning = new Warning(user.getId(), user.getUsername(), reason, issuerName, issuerUuid, System.currentTimeMillis(), severity);
        createWarning(sender, user, warning);
    }

    public void sendWarning(CommandSender sender, SppPlayer user, String reason) {
        String issuerName = sender instanceof Player ? sender.getName() : "Console";
        UUID issuerUuid = sender instanceof Player ? ((Player) sender).getUniqueId() : StaffPlus.get().consoleUUID;
        Warning warning = new Warning(user.getId(), user.getUsername(), reason, issuerName, issuerUuid, System.currentTimeMillis());
        createWarning(sender, user, warning);
    }

    private void createWarning(CommandSender sender, SppPlayer user, Warning warning) {
        // Offline users cannot bypass being warned this way. Permissions are taken away upon logging out
        if (user.isOnline() && permission.has(user.getPlayer(), options.permissionWarnBypass)) {
            message.send(sender, messages.bypassed, messages.prefixGeneral);
            return;
        }

        warnRepository.addWarning(warning);
        message.send(sender, messages.warned.replace("%target%", warning.getName()).replace("%reason%", warning.getReason()), messages.prefixWarnings);

        sendEvent(new WarningCreatedEvent(warning));
        handleThresholds(user);
        if (user.isOnline()) {
            Player p = user.getPlayer();
            message.send(p, messages.warn.replace("%reason%", warning.getReason()), messages.prefixWarnings);
            options.warningConfiguration.getSound().play(p);
        }
    }

    private void handleThresholds(SppPlayer user) {
        int totalScore = warnRepository.getTotalScore(user.getId());
        List<WarningThresholdConfiguration> thresholds = options.warningConfiguration.getThresholds();
        Optional<WarningThresholdConfiguration> threshold = thresholds.stream()
            .sorted((o1, o2) -> o2.getScore() - o1.getScore())
            .filter(w -> w.getScore() <= totalScore)
            .findFirst();
        if (!threshold.isPresent()) {
            return;
        }
        List<String> validCommands = new ArrayList<>();
        for (WarningAction action : threshold.get().getActions()) {
            if (action.getRunStrategy() == ALWAYS
                || (action.getRunStrategy() == ONLINE && user.isOnline())
                || (action.getRunStrategy() == DELAY && user.isOnline())) {

                Bukkit.getScheduler().runTask(StaffPlus.get(), () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), action.getCommand().replace("%player%", user.getUsername())));
                validCommands.add(action.getCommand());
            } else if (action.getRunStrategy() == DELAY && !user.isOnline()) {
                delayedActionsRepository.saveDelayedAction(user.getId(), action.getCommand());
                validCommands.add(action.getCommand());
            }
        }
        sendEvent(new WarningThresholdReachedEvent(user.getUsername(), user.getId(), threshold.get().getScore(), validCommands));
    }

    public void clearWarnings(CommandSender sender, SppPlayer player) {
        String issuerName = sender instanceof Player ? sender.getName() : "Console";
        UUID issuerUuid = sender instanceof Player ? ((Player) sender).getUniqueId() : StaffPlus.get().consoleUUID;
        warnRepository.removeWarnings(player.getId());
        sendEvent(new WarningsClearedEvent(issuerName, issuerUuid, player.getUsername(), player.getId()));
    }

    public List<Warning> getWarnings(UUID uuid) {
        return warnRepository.getWarnings(uuid);
    }

    public List<Warning> getWarnings() {
        return warnRepository.getWarnings();
    }

    public void removeWarning(int id) {
        warnRepository.removeWarning(id);
    }

    private void sendEvent(Event event) {
        getScheduler().runTask(StaffPlus.get(), () -> {
            Bukkit.getPluginManager().callEvent(event);
        });
    }

    public List<Warning> getWarnings(UUID uniqueId, int offset, int amount) {
        return warnRepository.getWarnings(uniqueId, offset, amount);
    }

    public void markWarningsRead(UUID uniqueId) {
        warnRepository.markWarningsRead(uniqueId);
    }

    @Override
    public List<? extends Infraction> getInfractions(Player executor, UUID playerUUID) {
        if (!options.infractionsConfiguration.isShowWarnings()) {
            return Collections.emptyList();
        }
        return warnRepository.getWarnings(playerUUID);
    }

    @Override
    public Optional<InfractionCount> getInfractionsCount() {
        if (!options.infractionsConfiguration.isShowWarnings()) {
            return Optional.empty();
        }
        return Optional.of(new InfractionCount(InfractionType.WARNING, warnRepository.getCountByPlayer()));
    }

    @Override
    public InfractionType getType() {
        return InfractionType.WARNING;
    }
}
