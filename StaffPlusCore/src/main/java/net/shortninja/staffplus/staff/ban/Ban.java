package net.shortninja.staffplus.staff.ban;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

public class Ban {

    private int id;
    private String reason;
    private Long endDate;
    private Long creationDate;
    private String playerName;
    private UUID playerUuid;
    private String issuerName;
    private UUID issuerUuid;
    private String unbannedByName;
    private UUID unbannedByUuid;
    private String unbanReason;

    public Ban(int id, String reason, Long creationDate, Long endDate, String playerName, UUID playerUuid, String issuerName, UUID issuerUuid, String unbannedByName, UUID unbannedByUuid, String unbanReason) {
        this.id = id;
        this.reason = reason;
        this.creationDate = creationDate;
        this.endDate = endDate;
        this.playerName = playerName;
        this.playerUuid = playerUuid;
        this.issuerName = issuerName;
        this.issuerUuid = issuerUuid;
        this.unbannedByName = unbannedByName;
        this.unbannedByUuid = unbannedByUuid;
        this.unbanReason = unbanReason;
    }

    public Ban(String reason, Long endDate, String issuerName, UUID issuerUuid, String playerName, UUID playerUuid) {
        this.reason = reason;
        this.playerName = playerName;
        this.issuerName = issuerName;
        this.creationDate = System.currentTimeMillis();
        this.endDate = endDate;
        this.playerUuid = playerUuid;
        this.issuerUuid = issuerUuid;
    }
    public Ban(String reason, String issuerName, UUID issuerUuid, String playerName, UUID playerUuid) {
        this.reason = reason;
        this.playerName = playerName;
        this.issuerName = issuerName;
        this.creationDate = System.currentTimeMillis();
        this.playerUuid = playerUuid;
        this.issuerUuid = issuerUuid;
    }

    public ZonedDateTime getCreationDate() {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(creationDate), ZoneId.systemDefault());
    }

    public Long getCreationTimestamp() {
        return creationDate;
    }

    public int getId() {
        return id;
    }

    public String getReason() {
        return reason;
    }

    public Long getEndDate() {
        return endDate;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public UUID getIssuerUuid() {
        return issuerUuid;
    }

    public UUID getUnbannedByUuid() {
        return unbannedByUuid;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getIssuerName() {
        return issuerName;
    }

    public String getUnbannedByName() {
        return unbannedByName;
    }

    public String getUnbanReason() {
        return unbanReason;
    }
}
