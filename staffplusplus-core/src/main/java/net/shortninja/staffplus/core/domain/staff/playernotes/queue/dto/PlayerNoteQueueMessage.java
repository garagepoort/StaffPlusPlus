package net.shortninja.staffplus.core.domain.staff.playernotes.queue.dto;

import java.util.UUID;

public class PlayerNoteQueueMessage {

    private final UUID playerUuid;
    private final String playerName;
    private final int noteId;

    public PlayerNoteQueueMessage(UUID playerUuid, String playerName, int noteId) {
        this.playerUuid = playerUuid;
        this.playerName = playerName;
        this.noteId = noteId;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getNoteId() {
        return noteId;
    }
}
