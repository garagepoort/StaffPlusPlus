package net.shortninja.staffplus.core.domain.staff.mute.appeals.queue.listeners;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.queue.QueueMessageListener;
import net.shortninja.staffplus.core.domain.staff.appeals.AppealService;
import net.shortninja.staffplus.core.domain.staff.mute.Mute;
import net.shortninja.staffplus.core.domain.staff.mute.MuteService;
import net.shortninja.staffplus.core.domain.staff.mute.appeals.queue.dto.MuteAppealQueueMessage;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Bukkit;

@IocBean
@IocMultiProvider(QueueMessageListener.class)
public class MuteAppealListener implements QueueMessageListener<MuteAppealQueueMessage> {

    private final AppealService appealService;
    private final MuteService muteService;

    public MuteAppealListener(AppealService appealService, MuteService muteService) {
        this.appealService = appealService;
        this.muteService = muteService;
    }

    @Override
    public String handleMessage(MuteAppealQueueMessage message) {
        SppPlayer sppPlayer = new SppPlayer(message.getPlayerUuid(), message.getPlayerName(), Bukkit.getOfflinePlayer(message.getPlayerUuid()));
        Mute mute = muteService.getById(message.getMuteId());
        appealService.addAppeal(sppPlayer, mute, message.getReason());
        return "Mute Appeal has been request";
    }

    @Override
    public String getType() {
        return "mute/create-appeal";
    }

    @Override
    public Class getMessageClass() {
        return MuteAppealQueueMessage.class;
    }
}
