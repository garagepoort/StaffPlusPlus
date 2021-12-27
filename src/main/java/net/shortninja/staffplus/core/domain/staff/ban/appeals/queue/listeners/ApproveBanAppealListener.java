package net.shortninja.staffplus.core.domain.staff.ban.appeals.queue.listeners;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.queue.QueueMessageListener;
import net.shortninja.staffplus.core.domain.staff.appeals.AppealService;
import net.shortninja.staffplus.core.domain.staff.ban.appeals.queue.dto.ApproveBanAppealQueueMessage;
import net.shortninja.staffplusplus.appeals.AppealableType;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Bukkit;

@IocBean
@IocMultiProvider(QueueMessageListener.class)
public class ApproveBanAppealListener implements QueueMessageListener<ApproveBanAppealQueueMessage> {

    private final AppealService appealService;

    public ApproveBanAppealListener(AppealService appealService) {
        this.appealService = appealService;
    }

    @Override
    public String handleMessage(ApproveBanAppealQueueMessage message) {
        SppPlayer sppPlayer = new SppPlayer(message.getPlayerUuid(), message.getPlayerName(), Bukkit.getOfflinePlayer(message.getPlayerUuid()));
        appealService.approveAppeal(sppPlayer, message.getAppealId(), message.getReason(), AppealableType.BAN);
        return "Ban Appeal has been approved";
    }

    @Override
    public String getType() {
        return "ban/approve-appeal";
    }

    @Override
    public Class getMessageClass() {
        return ApproveBanAppealQueueMessage.class;
    }
}
