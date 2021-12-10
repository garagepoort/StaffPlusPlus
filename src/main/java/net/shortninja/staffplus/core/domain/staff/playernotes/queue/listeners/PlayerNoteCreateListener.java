package net.shortninja.staffplus.core.domain.staff.playernotes.queue.listeners;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.queue.QueueMessageListener;
import net.shortninja.staffplus.core.domain.staff.playernotes.PlayerNoteService;
import net.shortninja.staffplus.core.domain.staff.playernotes.queue.dto.PlayerNoteCreateQueueMessage;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Bukkit;

@IocBean
@IocMultiProvider(QueueMessageListener.class)
public class PlayerNoteCreateListener implements QueueMessageListener<PlayerNoteCreateQueueMessage> {

    private final PlayerNoteService playerNoteService;

    public PlayerNoteCreateListener(PlayerNoteService playerNoteService) {
        this.playerNoteService = playerNoteService;
    }

    @Override
    public String handleMessage(PlayerNoteCreateQueueMessage message) {
        SppPlayer sppPlayer = new SppPlayer(message.getPlayerUuid(), message.getPlayerName(), Bukkit.getOfflinePlayer(message.getPlayerUuid()));
        SppPlayer target = new SppPlayer(message.getTargetUuid(), message.getTargetName(), Bukkit.getOfflinePlayer(message.getPlayerUuid()));
        playerNoteService.createNote(sppPlayer, message.getNote(), target, message.isPrivateNote());
        return "Note has has been created for player: " + target.getUsername();
    }

    @Override
    public String getType() {
        return "player-note/create";
    }

    @Override
    public Class getMessageClass() {
        return PlayerNoteCreateQueueMessage.class;
    }
}
