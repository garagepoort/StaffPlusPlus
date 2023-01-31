package net.shortninja.staffplus.core.punishments.ban.appeals.queue.dto;

import java.util.UUID;

public class ApproveBanAppealQueueMessage {

    private final UUID playerUuid;
    private final String playerName;
    private final int appealId;
    private final String reason;

    public ApproveBanAppealQueueMessage(UUID playerUuid, String playerName, int appealId, String reason) {
        this.playerUuid = playerUuid;
        this.playerName = playerName;
        this.appealId = appealId;
        this.reason = reason;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getAppealId() {
        return appealId;
    }

    public String getReason() {
        return reason;
    }
}
