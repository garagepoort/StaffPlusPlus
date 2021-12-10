package net.shortninja.staffplus.core.domain.staff.playernotes.queue.listeners;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.queue.QueueMessageListener;
import net.shortninja.staffplus.core.domain.staff.playernotes.PlayerNoteService;
import net.shortninja.staffplus.core.domain.staff.playernotes.queue.dto.PlayerNoteQueueMessage;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Bukkit;

@IocBean
@IocMultiProvider(QueueMessageListener.class)
public class PlayerNoteDeleteListener implements QueueMessageListener<PlayerNoteQueueMessage> {

    private final PlayerNoteService playerNoteService;

    public PlayerNoteDeleteListener(PlayerNoteService playerNoteService) {
        this.playerNoteService = playerNoteService;
    }

    @Override
    public String handleMessage(PlayerNoteQueueMessage message) {
        SppPlayer sppPlayer = new SppPlayer(message.getPlayerUuid(), message.getPlayerName(), Bukkit.getOfflinePlayer(message.getPlayerUuid()));
        playerNoteService.deleteNote(sppPlayer, message.getNoteId());
        return "Note with ID " + message.getNoteId() + " has been deleted";
    }

    @Override
    public String getType() {
        return "player-note/delete";
    }

    @Override
    public Class getMessageClass() {
        return PlayerNoteQueueMessage.class;
    }
}
