package net.shortninja.staffplus.core.domain.staff.mute.appeals.queue.dto;

import java.util.UUID;

public class MuteAppealQueueMessage {

    private final UUID playerUuid;
    private final String playerName;
    private final int muteId;
    private final String reason;

    public MuteAppealQueueMessage(UUID playerUuid, String playerName, int muteId, String reason) {
        this.playerUuid = playerUuid;
        this.playerName = playerName;
        this.muteId = muteId;
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getMuteId() {
        return muteId;
    }
}
