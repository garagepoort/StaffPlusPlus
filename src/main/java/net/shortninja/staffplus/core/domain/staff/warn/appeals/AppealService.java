package net.shortninja.staffplus.core.domain.staff.warn.appeals;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.config.AppealConfiguration;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.database.AppealRepository;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.Warning;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.database.WarnRepository;
import net.shortninja.staffplusplus.appeals.AppealApprovedEvent;
import net.shortninja.staffplusplus.appeals.AppealStatus;
import net.shortninja.staffplusplus.appeals.Appealable;
import net.shortninja.staffplusplus.appeals.AppealedEvent;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplusplus.warnings.WarningAppealRejectedEvent;
import org.bukkit.entity.Player;

import java.util.Optional;

import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;
import static net.shortninja.staffplus.core.common.utils.Validator.validator;

@IocBean
public class AppealService {

    private final PlayerManager playerManager;
    private final AppealRepository appealRepository;
    private final WarnRepository warnRepository;

    private final Messages messages;
    private final PermissionHandler permission;
    private final Options options;
    private final AppealConfiguration appealConfiguration;

    public AppealService(PlayerManager playerManager, AppealRepository appealRepository, WarnRepository warnRepository,
                         Messages messages, PermissionHandler permission, Options options) {
        this.playerManager = playerManager;
        this.appealRepository = appealRepository;
        this.warnRepository = warnRepository;

        this.messages = messages;
        this.permission = permission;
        this.options = options;
        this.appealConfiguration = options.appealConfiguration;
    }

    public void addAppeal(Player appealer, Appealable appealable, String reason) {
        validator(appealer)
            .validateAnyPermission(appealConfiguration.createAppealPermission)
            .validateNotEmpty(reason, "Reason for appeal can not be empty");

        Appeal appeal = new Appeal(appealable.getId(), appealer.getUniqueId(), appealer.getName(), reason);
        appealRepository.addAppeal(appeal);

        appealable.setAppeal(appeal);
        sendEvent(new AppealedEvent(appealable));
    }

    public void approveAppeal(Player resolver, int appealId) {
        this.approveAppeal(resolver, appealId, null);
    }

    public void approveAppeal(Player resolver, int appealId, String appealReason) {
        permission.validate(resolver, appealConfiguration.approveAppealPermission);
        Appeal appeal = getAppeal(appealId);
        Warning warning = warnRepository.findWarning(appeal.getAppealableId()).orElseThrow(() -> new BusinessException("No warning found."));

        if (warning.getServerName() != null && !warning.getServerName().equals(options.serverName)) {
            throw new BusinessException("For consistency reasons an appeal must accepted on the same server the warning was created. Please try accepting the appeal while connected to server " + warning.getServerName());
        }

        appealRepository.updateAppealStatus(appealId, resolver.getUniqueId(), appealReason, AppealStatus.APPROVED);
        sendMessageToPlayer(appeal, messages.appealApproved);
        this.messages.send(resolver, messages.appealApprove, messages.prefixWarnings);

        Warning updatedWarning = warnRepository.findWarning(appeal.getAppealableId()).orElseThrow(() -> new BusinessException("No warning found."));
        sendEvent(new AppealApprovedEvent(updatedWarning));
    }

    public Appeal getAppeal(int appealId) {
        return appealRepository.findAppeal(appealId).orElseThrow(() -> new BusinessException("No appeal found with id: [" + appealId + "]"));
    }

    public void rejectAppeal(Player resolver, int appealId) {
        this.rejectAppeal(resolver, appealId, null);
    }

    public void rejectAppeal(Player resolver, int appealId, String appealReason) {
        permission.validate(resolver, appealConfiguration.rejectAppealPermission);
        Appeal appeal = getAppeal(appealId);

        appealRepository.updateAppealStatus(appealId, resolver.getUniqueId(), appealReason, AppealStatus.REJECTED);
        sendMessageToPlayer(appeal, messages.appealRejected);
        this.messages.send(resolver, messages.appealReject, messages.prefixWarnings);

        Warning updatedWarning = warnRepository.findWarning(appeal.getAppealableId()).orElseThrow(() -> new BusinessException("No warning found"));
        sendEvent(new WarningAppealRejectedEvent(updatedWarning));
    }

    private void sendMessageToPlayer(Appeal appeal, String message) {
        Optional<SppPlayer> appealer = playerManager.getOnOrOfflinePlayer(appeal.getAppealerUuid());
        if (appealer.isPresent() && appealer.get().isOnline()) {
            this.messages.send(appealer.get().getPlayer(), message, messages.prefixWarnings);
        }
    }

}
