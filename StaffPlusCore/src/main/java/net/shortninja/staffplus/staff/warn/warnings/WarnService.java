package net.shortninja.staffplus.staff.warn.warnings;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.actions.ActionService;
import net.shortninja.staffplus.common.exceptions.BusinessException;
import net.shortninja.staffplus.event.warnings.WarningCreatedEvent;
import net.shortninja.staffplus.event.warnings.WarningsClearedEvent;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.infractions.Infraction;
import net.shortninja.staffplus.staff.infractions.InfractionCount;
import net.shortninja.staffplus.staff.infractions.InfractionProvider;
import net.shortninja.staffplus.staff.infractions.InfractionType;
import net.shortninja.staffplus.staff.warn.warnings.config.WarningSeverityConfiguration;
import net.shortninja.staffplus.staff.warn.warnings.database.WarnRepository;
import net.shortninja.staffplus.unordered.AppealStatus;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.bukkit.Bukkit.getScheduler;

public class WarnService implements InfractionProvider {

    private final PermissionHandler permission;
    private final MessageCoordinator message;
    private final Options options;
    private final Messages messages;
    private final WarnRepository warnRepository;
    private final ActionService actionService;
    private final ThresholdService thresholdService;

    public WarnService(PermissionHandler permission,
                       MessageCoordinator message,
                       Options options,
                       Messages messages,
                       WarnRepository warnRepository,
                       ActionService actionService, ThresholdService thresholdService) {
        this.permission = permission;
        this.message = message;
        this.options = options;
        this.messages = messages;
        this.warnRepository = warnRepository;
        this.actionService = actionService;
        this.thresholdService = thresholdService;
    }

    public void sendWarning(CommandSender sender, SppPlayer culprit, String reason, String severityLevel) {
        WarningSeverityConfiguration severity = options.warningConfiguration.getSeverityLevels().stream()
            .filter(s -> s.getName().equalsIgnoreCase(severityLevel))
            .findFirst()
            .orElseThrow(() -> new BusinessException("Cannot find severity level: [" + severityLevel + "]", messages.prefixWarnings));

        String issuerName = sender instanceof Player ? sender.getName() : "Console";
        UUID issuerUuid = sender instanceof Player ? ((Player) sender).getUniqueId() : StaffPlus.get().consoleUUID;
        Warning warning = new Warning(culprit.getId(), culprit.getUsername(), reason, issuerName, issuerUuid, System.currentTimeMillis(), severity);
        createWarning(sender, culprit, warning);
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
        actionService.executeActions(sender, user, options.warningConfiguration.getActions(), new WarningActionFilter(warning));
        thresholdService.handleThresholds(sender, user);

        if (user.isOnline()) {
            Player p = user.getPlayer();
            message.send(p, messages.warn.replace("%reason%", warning.getReason()), messages.prefixWarnings);
            options.warningConfiguration.getSound().play(p);
        }
    }

    public Warning getWarning(int warningId) {
        return warnRepository.findWarning(warningId)
            .orElseThrow(() -> new BusinessException("Warning with id [" + warningId + "] not found", messages.prefixWarnings));
    }

    public void clearWarnings(CommandSender sender, SppPlayer player) {
        String issuerName = sender instanceof Player ? sender.getName() : "Console";
        UUID issuerUuid = sender instanceof Player ? ((Player) sender).getUniqueId() : StaffPlus.get().consoleUUID;
        warnRepository.removeWarnings(player.getId());
        sendEvent(new WarningsClearedEvent(issuerName, issuerUuid, player.getUsername(), player.getId()));
    }

    public List<Warning> getWarnings(UUID uuid, boolean includeAppealed) {
        return warnRepository.getWarnings(uuid).stream()
            .filter(w -> includeAppealed || (w.getAppeal().isPresent() && w.getAppeal().get().getStatus() == AppealStatus.APPROVED))
            .collect(Collectors.toList());
    }

    public List<Warning> getWarnings() {
        return warnRepository.getWarnings();
    }

    public void removeWarning(int id) {
        warnRepository.removeWarning(id);
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
        return getWarnings(playerUUID, false);
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

    private void sendEvent(Event event) {
        getScheduler().runTask(StaffPlus.get(), () -> {
            Bukkit.getPluginManager().callEvent(event);
        });
    }
}
