package net.shortninja.staffplus.core.domain.staff.warn.appeals;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.config.WarningAppealConfiguration;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.database.AppealRepository;
import net.shortninja.staffplusplus.appeals.AppealApprovedEvent;
import net.shortninja.staffplusplus.appeals.AppealRejectedEvent;
import net.shortninja.staffplusplus.appeals.AppealStatus;
import net.shortninja.staffplusplus.appeals.Appealable;
import net.shortninja.staffplusplus.appeals.AppealableType;
import net.shortninja.staffplusplus.appeals.AppealedEvent;
import org.bukkit.entity.Player;

import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;
import static net.shortninja.staffplus.core.common.utils.Validator.validator;

@IocBean
public class AppealService {

    private final PlayerManager playerManager;
    private final AppealRepository appealRepository;

    private final Messages messages;
    private final PermissionHandler permission;
    private final WarningAppealConfiguration warningAppealConfiguration;

    public AppealService(PlayerManager playerManager,
                         AppealRepository appealRepository,
                         Messages messages,
                         PermissionHandler permission,
                         Options options) {
        this.playerManager = playerManager;
        this.appealRepository = appealRepository;

        this.messages = messages;
        this.permission = permission;
        this.warningAppealConfiguration = options.warningAppealConfiguration;
    }

    public void addAppeal(Player appealer, Appealable appealable, String reason) {
        validator(appealer)
            .validateAnyPermission(warningAppealConfiguration.createAppealPermission)
            .validateNotEmpty(reason, "Reason for appeal can not be empty");

        Appeal appeal = new Appeal(appealable.getId(), appealer.getUniqueId(), appealer.getName(), reason, appealable.getType());
        appealRepository.addAppeal(appeal, appealable.getType());

        appealable.setAppeal(appeal);
        sendEvent(new AppealedEvent(appealable));
    }

    public void approveAppeal(Player resolver, int appealId, AppealableType appealableType) {
        this.approveAppeal(resolver, appealId, null, appealableType);
    }

    public void approveAppeal(Player resolver, int appealId, String appealReason, AppealableType appealableType) {
        permission.validate(resolver, warningAppealConfiguration.approveAppealPermission);
        appealRepository.updateAppealStatus(appealId, resolver.getUniqueId(), appealReason, AppealStatus.APPROVED, appealableType);
        Appeal appeal = getAppeal(appealId);

        sendEvent(new AppealApprovedEvent(appeal));
    }

    public Appeal getAppeal(int appealId) {
        return appealRepository.findAppeal(appealId).orElseThrow(() -> new BusinessException("No appeal found with id: [" + appealId + "]"));
    }

    public void rejectAppeal(Player resolver, int appealId, AppealableType appealableType) {
        this.rejectAppeal(resolver, appealId, null, appealableType);
    }

    public void rejectAppeal(Player resolver, int appealId, String appealReason, AppealableType appealableType) {
        permission.validate(resolver, warningAppealConfiguration.rejectAppealPermission);

        appealRepository.updateAppealStatus(appealId, resolver.getUniqueId(), appealReason, AppealStatus.REJECTED, appealableType);
        Appeal appeal = getAppeal(appealId);
        sendEvent(new AppealRejectedEvent(appeal));
    }

}
