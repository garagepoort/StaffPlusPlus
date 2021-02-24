package net.shortninja.staffplus.staff.warn.warnings;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.exceptions.BusinessException;
import net.shortninja.staffplus.event.warnings.WarningCreatedEvent;
import net.shortninja.staffplus.event.warnings.WarningExpiredEvent;
import net.shortninja.staffplus.event.warnings.WarningRemovedEvent;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.infractions.Infraction;
import net.shortninja.staffplus.staff.infractions.InfractionCount;
import net.shortninja.staffplus.staff.infractions.InfractionProvider;
import net.shortninja.staffplus.staff.infractions.InfractionType;
import net.shortninja.staffplus.staff.warn.appeals.database.AppealRepository;
import net.shortninja.staffplus.staff.warn.warnings.config.WarningSeverityConfiguration;
import net.shortninja.staffplus.staff.warn.warnings.database.WarnRepository;
import net.shortninja.staffplus.unordered.AppealStatus;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.util.BukkitUtils.sendEvent;

public class WarnService implements InfractionProvider {
    private final PermissionHandler permission;
    private final MessageCoordinator message;
    private final Options options;
    private final Messages messages;
    private final WarnRepository warnRepository;
    private final AppealRepository appealRepository;

    public WarnService(PermissionHandler permission,
                       MessageCoordinator message,
                       Options options,
                       Messages messages,
                       WarnRepository warnRepository,
                       AppealRepository appealRepository) {
        this.permission = permission;
        this.message = message;
        this.options = options;
        this.messages = messages;
        this.warnRepository = warnRepository;
        this.appealRepository = appealRepository;
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

        int warningId = warnRepository.addWarning(warning);
        warning.setId(warningId);
        message.send(sender, messages.warned.replace("%target%", warning.getName()).replace("%reason%", warning.getReason()), messages.prefixWarnings);

        sendEvent(new WarningCreatedEvent(warning));

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

    public List<Warning> getWarnings(UUID uuid, boolean includeAppealed) {
        return warnRepository.getWarnings(uuid).stream()
            .filter(w -> includeAppealed || (w.getAppeal().isPresent() && w.getAppeal().get().getStatus() == AppealStatus.APPROVED))
            .collect(Collectors.toList());
    }

    public List<Warning> getWarnings() {
        return warnRepository.getWarnings();
    }

    public void removeWarning(CommandSender sender, int id) {
        Warning warning = getWarning(id);
        if (warning.getServerName() != null && !warning.getServerName().equals(options.serverName)) {
            throw new BusinessException("For consistency reasons a warning must be removed on the same server it was created. Please try removing the warning while connected to server " + warning.getServerName());
        }

        appealRepository.deleteAppealsForWarning(id);
        warnRepository.removeWarning(id);
        message.send(sender, "&2Warning has been removed", messages.prefixWarnings);
        sendEvent(new WarningRemovedEvent(warning));
    }

    public void expireWarning(CommandSender sender, int id) {
        Warning warning = getWarning(id);
        warnRepository.expireWarning(id);
        message.send(sender, "&2Warning has been expired", messages.prefixWarnings);
        sendEvent(new WarningExpiredEvent(warning));
    }

    public List<Warning> getWarnings(UUID uniqueId, int offset, int amount) {
        return warnRepository.getWarnings(uniqueId, offset, amount);
    }

    public List<Warning> getAppealedWarnings(int offset, int amount) {
        return warnRepository.getAppealedWarnings(offset, amount);
    }

    public void markWarningsRead(UUID uniqueId) {
        warnRepository.markWarningsRead(uniqueId);
    }

    public void expireWarnings() {
        long now = System.currentTimeMillis();
        options.warningConfiguration.getSeverityLevels().stream()
            .filter(s -> s.getExpirationDuration() > 0)
            .forEach(s -> warnRepository.expireWarnings(s.getName(), now - s.getExpirationDuration()));
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

}
