package net.shortninja.staffplus.core.domain.staff.appeals;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.domain.staff.appeals.database.AppealRepository;
import net.shortninja.staffplusplus.appeals.AppealApprovedEvent;
import net.shortninja.staffplusplus.appeals.AppealRejectedEvent;
import net.shortninja.staffplusplus.appeals.AppealStatus;
import net.shortninja.staffplusplus.appeals.Appealable;
import net.shortninja.staffplusplus.appeals.AppealableType;
import net.shortninja.staffplusplus.appeals.AppealedEvent;
import net.shortninja.staffplusplus.session.SppPlayer;

import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;

@IocBean
public class AppealService {

    private final AppealRepository appealRepository;

    public AppealService(AppealRepository appealRepository) {
        this.appealRepository = appealRepository;
    }

    public void addAppeal(SppPlayer appealer, Appealable appealable, String reason) {
        Appeal appeal = new Appeal(appealable.getId(), appealer.getId(), appealer.getUsername(), reason, appealable.getType());
        appealRepository.addAppeal(appeal, appealable.getType());

        appealable.setAppeal(appeal);
        sendEvent(new AppealedEvent(appealable));
    }

    public void approveAppeal(SppPlayer resolver, int appealId, AppealableType appealableType) {
        this.approveAppeal(resolver, appealId, null, appealableType);
    }

    public void approveAppeal(SppPlayer resolver, int appealId, String appealReason, AppealableType appealableType) {
        appealRepository.updateAppealStatus(appealId, resolver.getId(), appealReason, AppealStatus.APPROVED, appealableType);
        Appeal appeal = getAppeal(appealId);

        sendEvent(new AppealApprovedEvent(appeal));
    }

    public Appeal getAppeal(int appealId) {
        return appealRepository.findAppeal(appealId).orElseThrow(() -> new BusinessException("No appeal found with id: [" + appealId + "]"));
    }

    public void rejectAppeal(SppPlayer resolver, int appealId, AppealableType appealableType) {
        this.rejectAppeal(resolver, appealId, null, appealableType);
    }

    public void rejectAppeal(SppPlayer resolver, int appealId, String appealReason, AppealableType appealableType) {
        appealRepository.updateAppealStatus(appealId, resolver.getId(), appealReason, AppealStatus.REJECTED, appealableType);
        Appeal appeal = getAppeal(appealId);
        sendEvent(new AppealRejectedEvent(appeal));
    }
}
