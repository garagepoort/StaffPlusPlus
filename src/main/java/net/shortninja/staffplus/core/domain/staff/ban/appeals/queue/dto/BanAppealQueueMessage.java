package net.shortninja.staffplus.core.domain.staff.ban.appeals.queue.dto;

import java.util.UUID;

public class BanAppealQueueMessage {

    private final UUID playerUuid;
    private final String playerName;
    private final int banId;
    private final String reason;

    public BanAppealQueueMessage(UUID playerUuid, String playerName, int banId, String reason) {
        this.playerUuid = playerUuid;
        this.playerName = playerName;
        this.banId = banId;
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

    public int getBanId() {
        return banId;
    }
}
