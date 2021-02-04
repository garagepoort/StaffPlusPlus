package net.shortninja.staffplus.staff.ban;

import net.shortninja.staffplus.staff.infractions.Infraction;
import net.shortninja.staffplus.staff.infractions.InfractionType;
import net.shortninja.staffplus.unordered.IBan;
import net.shortninja.staffplus.util.lib.JavaUtils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

public class Ban implements IBan, Infraction {

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
    private String serverName;

    public Ban(int id, String reason, Long creationDate, Long endDate, String playerName, UUID playerUuid, String issuerName, UUID issuerUuid, String unbannedByName, UUID unbannedByUuid, String unbanReason, String serverName) {
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
        this.serverName = serverName;
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

    @Override
    public Long getCreationTimestamp() {
        return creationDate;
    }

    @Override
    public ZonedDateTime getCreationDate() {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(creationDate), ZoneId.systemDefault());
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getReason() {
        return reason;
    }

    @Override
    public UUID getPlayerUuid() {
        return playerUuid;
    }

    @Override
    public UUID getIssuerUuid() {
        return issuerUuid;
    }

    @Override
    public Long getEndTimestamp() {
        return endDate;
    }

    @Override
    public ZonedDateTime getEndDate() {
        if(endDate == null) {
            return null;
        }
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(endDate), ZoneId.systemDefault());
    }

    @Override
    public UUID getUnbannedByUuid() {
        return unbannedByUuid;
    }

    @Override
    public String getPlayerName() {
        return playerName;
    }

    @Override
    public String getIssuerName() {
        return issuerName;
    }

    @Override
    public String getUnbannedByName() {
        return unbannedByName;
    }

    @Override
    public String getUnbanReason() {
        return unbanReason;
    }

    @Override
    public String getHumanReadableDuration() {
        if(endDate == null) {
            return null;
        }
        long duration = JavaUtils.getDuration(endDate);
        return JavaUtils.toHumanReadableDuration(duration);
    }

    public void setUnbannedByName(String unbannedByName) {
        this.unbannedByName = unbannedByName;
    }

    public void setUnbannedByUuid(UUID unbannedByUuid) {
        this.unbannedByUuid = unbannedByUuid;
    }

    public void setUnbanReason(String unbanReason) {
        this.unbanReason = unbanReason;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public InfractionType getInfractionType() {
        return InfractionType.BAN;
    }

    @Override
    public String getServerName() {
        return serverName;
    }
}
