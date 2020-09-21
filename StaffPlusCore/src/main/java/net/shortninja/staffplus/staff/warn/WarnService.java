package net.shortninja.staffplus.staff.warn;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.staff.delayedactions.DelayedActionsRepository;
import net.shortninja.staffplus.common.BusinessException;
import net.shortninja.staffplus.event.warnings.WarningCreatedEvent;
import net.shortninja.staffplus.event.warnings.WarningThresholdReachedEvent;
import net.shortninja.staffplus.event.warnings.WarningsClearedEvent;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.player.attribute.infraction.Warning;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.server.data.config.warning.WarningAction;
import net.shortninja.staffplus.server.data.config.warning.WarningSeverityConfiguration;
import net.shortninja.staffplus.server.data.config.warning.WarningThresholdConfiguration;
import net.shortninja.staffplus.unordered.IUser;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import net.shortninja.staffplus.staff.warn.database.WarnRepository;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static net.shortninja.staffplus.server.data.config.warning.WarningActionRunStrategy.*;
import static org.bukkit.Bukkit.getScheduler;

public class WarnService {

    private PermissionHandler permission;
    private MessageCoordinator message;
    private Options options;
    private Messages messages;
    private UserManager userManager;
    private WarnRepository warnRepository;
    private DelayedActionsRepository delayedActionsRepository;

    public WarnService(PermissionHandler permission,
                       MessageCoordinator message,
                       Options options,
                       Messages messages,
                       UserManager userManager,
                       WarnRepository warnRepository,
                       DelayedActionsRepository delayedActionsRepository) {
        this.permission = permission;
        this.message = message;
        this.options = options;
        this.messages = messages;
        this.userManager = userManager;
        this.warnRepository = warnRepository;
        this.delayedActionsRepository = delayedActionsRepository;
    }

    public void sendWarning(CommandSender sender, String playerName, String reason, String severityLevel) {
        IUser user = userManager.getOnOrOfflineUser(playerName);
        if (user == null) {
            message.send(sender, messages.playerOffline, messages.prefixGeneral);
            return;
        }

        WarningSeverityConfiguration severity = options.warningConfiguration.getSeverityLevels().stream()
                .filter(s -> s.getName().equalsIgnoreCase(severityLevel))
                .findFirst()
                .orElseThrow(() -> new BusinessException("Cannot find severity level: [" + severityLevel + "]", messages.prefixWarnings));

        String issuerName = sender instanceof Player ? sender.getName() : "Console";
        UUID issuerUuid = sender instanceof Player ? ((Player) sender).getUniqueId() : StaffPlus.get().consoleUUID;
        Warning warning = new Warning(user.getUuid(), playerName, reason, issuerName, issuerUuid, System.currentTimeMillis(), severity);
        createWarning(sender, user, warning);
    }

    public void sendWarning(CommandSender sender, String playerName, String reason) {
        IUser user = userManager.getOnOrOfflineUser(playerName);
        if (user == null) {
            message.send(sender, messages.playerOffline, messages.prefixGeneral);
            return;
        }

        String issuerName = sender instanceof Player ? sender.getName() : "Console";
        UUID issuerUuid = sender instanceof Player ? ((Player) sender).getUniqueId() : StaffPlus.get().consoleUUID;
        Warning warning = new Warning(user.getUuid(), playerName, reason, issuerName, issuerUuid, System.currentTimeMillis());
        createWarning(sender, user, warning);
    }

    private void createWarning(CommandSender sender, IUser user, Warning warning) {
        // Offline users cannot bypass being warned this way. Permissions are taken away upon logging out
        if (user.isOnline() && permission.has(user.getPlayer().get(), options.permissionWarnBypass)) {
            message.send(sender, messages.bypassed, messages.prefixGeneral);
            return;
        }

        warnRepository.addWarning(warning);
        message.send(sender, messages.warned.replace("%target%", warning.getName()).replace("%reason%", warning.getReason()), messages.prefixWarnings);

        sendEvent(new WarningCreatedEvent(warning));
        handleThresholds(user);
        if (user.isOnline()) {
            Optional<Player> p = user.getPlayer();
            message.send(p.get(), messages.warn.replace("%reason%", warning.getReason()), messages.prefixWarnings);
            options.warningsSound.play(p.get());
        }
    }

    private void handleThresholds(IUser user) {
        int totalScore = warnRepository.getTotalScore(user.getUuid());
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

                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), action.getCommand().replace("%player%", user.getName()));
                validCommands.add(action.getCommand());
            } else if (action.getRunStrategy() == DELAY && !user.isOnline()) {
                delayedActionsRepository.saveDelayedAction(user.getUuid(), action.getCommand());
                validCommands.add(action.getCommand());
            }
        }
        sendEvent(new WarningThresholdReachedEvent(user.getName(), user.getUuid(), threshold.get().getScore(), validCommands));
    }

    public void clearWarnings(CommandSender sender, IUser user) {
        String issuerName = sender instanceof Player ? sender.getName() : "Console";
        UUID issuerUuid = sender instanceof Player ? ((Player) sender).getUniqueId() : StaffPlus.get().consoleUUID;
        warnRepository.removeWarnings(user.getUuid());
        sendEvent(new WarningsClearedEvent(issuerName, issuerUuid, user.getName(), user.getUuid()));
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
}
