package net.shortninja.staffplus.core.domain.staff.mute.appeals.queue.listeners;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.queue.QueueMessageListener;
import net.shortninja.staffplus.core.domain.staff.appeals.AppealService;
import net.shortninja.staffplus.core.domain.staff.mute.appeals.queue.dto.RejectMuteAppealQueueMessage;
import net.shortninja.staffplusplus.appeals.AppealableType;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Bukkit;

@IocBean
@IocMultiProvider(QueueMessageListener.class)
public class RejectMuteAppealListener implements QueueMessageListener<RejectMuteAppealQueueMessage> {

    private final AppealService appealService;

    public RejectMuteAppealListener(AppealService appealService) {
        this.appealService = appealService;
    }

    @Override
    public String handleMessage(RejectMuteAppealQueueMessage message) {
        SppPlayer sppPlayer = new SppPlayer(message.getPlayerUuid(), message.getPlayerName(), Bukkit.getOfflinePlayer(message.getPlayerUuid()));
        appealService.rejectAppeal(sppPlayer, message.getAppealId(), message.getReason(), AppealableType.MUTE);
        return "Mute Appeal has been rejected";
    }

    @Override
    public String getType() {
        return "mute/reject-appeal";
    }

    @Override
    public Class getMessageClass() {
        return RejectMuteAppealQueueMessage.class;
    }
}
