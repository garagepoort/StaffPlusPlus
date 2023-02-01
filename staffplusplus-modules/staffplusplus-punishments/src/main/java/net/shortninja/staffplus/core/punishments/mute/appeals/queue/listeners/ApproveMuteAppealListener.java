package net.shortninja.staffplus.core.punishments.mute.appeals.queue.listeners;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.queue.QueueMessageListener;
import net.shortninja.staffplus.core.appeals.AppealService;
import net.shortninja.staffplus.core.punishments.mute.appeals.queue.dto.ApproveMuteAppealQueueMessage;
import net.shortninja.staffplusplus.appeals.AppealableType;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Bukkit;

@IocBean
@IocMultiProvider(QueueMessageListener.class)
public class ApproveMuteAppealListener implements QueueMessageListener<ApproveMuteAppealQueueMessage> {

    private final AppealService appealService;

    public ApproveMuteAppealListener(AppealService appealService) {
        this.appealService = appealService;
    }

    @Override
    public String handleMessage(ApproveMuteAppealQueueMessage message) {
        SppPlayer sppPlayer = new SppPlayer(message.getPlayerUuid(), message.getPlayerName(), Bukkit.getOfflinePlayer(message.getPlayerUuid()));
        appealService.approveAppeal(sppPlayer, message.getAppealId(), message.getReason(), AppealableType.MUTE);
        return "Mute Appeal has been approved";
    }

    @Override
    public String getType() {
        return "mute/approve-appeal";
    }

    @Override
    public Class getMessageClass() {
        return ApproveMuteAppealQueueMessage.class;
    }
}
