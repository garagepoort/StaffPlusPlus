package net.shortninja.staffplus.core.domain.player.ip;

import java.util.UUID;

public class PlayerIpRecord {

    private String ip;
    private UUID playerUuid;
    private String playerName;

    public PlayerIpRecord(String ip, UUID playerUuid, String playerName) {
        this.ip = ip;
        this.playerUuid = playerUuid;
        this.playerName = playerName;
    }

    public String getIp() {
        return ip;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public String getPlayerName() {
        return playerName;
    }
}
