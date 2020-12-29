package net.shortninja.staffplus.staff.mute;

import net.shortninja.staffplus.unordered.IMute;
import net.shortninja.staffplus.util.lib.JavaUtils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

public class Mute implements IMute {

    private int id;
    private String reason;
    private Long endDate;
    private Long creationDate;
    private String playerName;
    private UUID playerUuid;
    private String issuerName;
    private UUID issuerUuid;
    private String unmutedByName;
    private UUID unmutedByUuid;
    private String unmuteReason;

    public Mute(int id, String reason, Long creationDate, Long endDate, String playerName, UUID playerUuid, String issuerName, UUID issuerUuid, String unmutedByName, UUID unmutedByUuid, String unmuteReason) {
        this.id = id;
        this.reason = reason;
        this.creationDate = creationDate;
        this.endDate = endDate;
        this.playerName = playerName;
        this.playerUuid = playerUuid;
        this.issuerName = issuerName;
        this.issuerUuid = issuerUuid;
        this.unmutedByName = unmutedByName;
        this.unmutedByUuid = unmutedByUuid;
        this.unmuteReason = unmuteReason;
    }

    public Mute(String reason, Long endDate, String issuerName, UUID issuerUuid, String playerName, UUID playerUuid) {
        this.reason = reason;
        this.playerName = playerName;
        this.issuerName = issuerName;
        this.creationDate = System.currentTimeMillis();
        this.endDate = endDate;
        this.playerUuid = playerUuid;
        this.issuerUuid = issuerUuid;
    }
    public Mute(String reason, String issuerName, UUID issuerUuid, String playerName, UUID playerUuid) {
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
    public UUID getUnmutedByUuid() {
        return unmutedByUuid;
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
    public String getUnmutedByName() {
        return unmutedByName;
    }

    @Override
    public String getUnmuteReason() {
        return unmuteReason;
    }

    @Override
    public String getHumanReadableDuration() {
        if(endDate == null) {
            return null;
        }
        long duration = JavaUtils.getDuration(endDate);
        return JavaUtils.toHumanReadableDuration(duration);
    }

    public void setUnmutedByName(String unmutedByName) {
        this.unmutedByName = unmutedByName;
    }

    public void setUnmutedByUuid(UUID unmutedByUuid) {
        this.unmutedByUuid = unmutedByUuid;
    }

    public void setUnmuteReason(String unmuteReason) {
        this.unmuteReason = unmuteReason;
    }

    public void setId(int id) {
        this.id = id;
    }
}
