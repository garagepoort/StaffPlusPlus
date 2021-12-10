package net.shortninja.staffplus.core.domain.staff.playernotes.queue.dto;

import java.util.UUID;

public class PlayerNoteCreateQueueMessage {

    private final UUID playerUuid;
    private final String playerName;
    private final UUID targetUuid;
    private final String targetName;
    private final String note;
    private final boolean isPrivateNote;

    public PlayerNoteCreateQueueMessage(UUID playerUuid, String playerName, UUID targetUuid, String targetName, String note, boolean isPrivateNote) {
        this.playerUuid = playerUuid;
        this.playerName = playerName;
        this.targetUuid = targetUuid;
        this.targetName = targetName;
        this.note = note;
        this.isPrivateNote = isPrivateNote;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getNote() {
        return note;
    }

    public UUID getTargetUuid() {
        return targetUuid;
    }

    public String getTargetName() {
        return targetName;
    }

    public boolean isPrivateNote() {
        return isPrivateNote;
    }
}
