package net.shortninja.staffplus.core.punishments.ban.appeals.queue.listeners;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.queue.QueueMessageListener;
import net.shortninja.staffplus.core.appeals.AppealService;
import net.shortninja.staffplus.core.punishments.ban.appeals.queue.dto.RejectBanAppealQueueMessage;
import net.shortninja.staffplusplus.appeals.AppealableType;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Bukkit;

@IocBean
@IocMultiProvider(QueueMessageListener.class)
public class RejectBanAppealListener implements QueueMessageListener<RejectBanAppealQueueMessage> {

    private final AppealService appealService;

    public RejectBanAppealListener(AppealService appealService) {
        this.appealService = appealService;
    }

    @Override
    public String handleMessage(RejectBanAppealQueueMessage message) {
        SppPlayer sppPlayer = new SppPlayer(message.getPlayerUuid(), message.getPlayerName(), Bukkit.getOfflinePlayer(message.getPlayerUuid()));
        appealService.rejectAppeal(sppPlayer, message.getAppealId(), message.getReason(), AppealableType.BAN);
        return "Ban Appeal has been rejected";
    }

    @Override
    public String getType() {
        return "ban/reject-appeal";
    }

    @Override
    public Class getMessageClass() {
        return RejectBanAppealQueueMessage.class;
    }
}
