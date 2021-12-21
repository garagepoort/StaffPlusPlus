package net.shortninja.staffplus.core.domain.staff.ban.appeals.queue.listeners;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.queue.QueueMessageListener;
import net.shortninja.staffplus.core.domain.staff.appeals.AppealService;
import net.shortninja.staffplus.core.domain.staff.ban.appeals.queue.dto.BanAppealQueueMessage;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.Ban;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.BanService;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Bukkit;

@IocBean
@IocMultiProvider(QueueMessageListener.class)
public class BanAppealListener implements QueueMessageListener<BanAppealQueueMessage> {

    private final AppealService appealService;
    private final BanService banService;

    public BanAppealListener(AppealService appealService, BanService banService) {
        this.appealService = appealService;
        this.banService = banService;
    }

    @Override
    public String handleMessage(BanAppealQueueMessage message) {
        SppPlayer sppPlayer = new SppPlayer(message.getPlayerUuid(), message.getPlayerName(), Bukkit.getOfflinePlayer(message.getPlayerUuid()));
        Ban ban = banService.getById(message.getBanId());
        appealService.addAppeal(sppPlayer, ban, message.getReason());
        return "Ban Appeal has been request";
    }

    @Override
    public String getType() {
        return "ban/create-appeal";
    }

    @Override
    public Class getMessageClass() {
        return BanAppealQueueMessage.class;
    }
}
