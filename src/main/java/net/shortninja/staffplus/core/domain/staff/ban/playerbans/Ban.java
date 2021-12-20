package net.shortninja.staffplus.core.domain.staff.ban.playerbans;

import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.domain.staff.infractions.Infraction;
import net.shortninja.staffplus.core.domain.staff.infractions.InfractionType;
import net.shortninja.staffplusplus.ban.IBan;
import net.shortninja.staffplusplus.investigate.evidence.Evidence;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

public class Ban implements IBan, Infraction, Evidence {

    private int id;
    private final String reason;
    private final Long creationDate;
    private final String targetName;
    private final UUID targetUuid;
    private final String issuerName;
    private final UUID issuerUuid;
    private final boolean silentBan;
    private final String template;

    private Long endDate;
    private boolean silentUnban;
    private String unbannedByName;
    private UUID unbannedByUuid;
    private String unbanReason;
    private String serverName;

    public Ban(int id, String reason, Long creationDate, Long endDate, String targetName, UUID targetUuid, String issuerName, UUID issuerUuid, String unbannedByName, UUID unbannedByUuid, String unbanReason, String serverName, boolean silentBan, boolean silentUnban, String template) {
        this.id = id;
        this.reason = reason;
        this.creationDate = creationDate;
        this.endDate = endDate;
        this.targetName = targetName;
        this.targetUuid = targetUuid;
        this.issuerName = issuerName;
        this.issuerUuid = issuerUuid;
        this.unbannedByName = unbannedByName;
        this.unbannedByUuid = unbannedByUuid;
        this.unbanReason = unbanReason;
        this.serverName = serverName;
        this.silentBan = silentBan;
        this.silentUnban = silentUnban;
        this.template = template;
    }

    public Ban(String reason, Long endDate, String issuerName, UUID issuerUuid, String targetName, UUID targetUuid, boolean silentBan, String template) {
        this.reason = reason;
        this.targetName = targetName;
        this.issuerName = issuerName;
        this.silentBan = silentBan;
        this.template = template;
        this.creationDate = System.currentTimeMillis();
        this.endDate = endDate;
        this.targetUuid = targetUuid;
        this.issuerUuid = issuerUuid;
    }

    public Ban(String reason, String issuerName, UUID issuerUuid, String targetName, UUID targetUuid, boolean silentBan, String template) {
        this.reason = reason;
        this.targetName = targetName;
        this.issuerName = issuerName;
        this.silentBan = silentBan;
        this.template = template;
        this.creationDate = System.currentTimeMillis();
        this.targetUuid = targetUuid;
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
    public String getEvidenceType() {
        return "BAN";
    }

    @Override
    public String getDescription() {
        return reason;
    }

    @Override
    public String getReason() {
        return reason;
    }

    @Override
    public UUID getTargetUuid() {
        return targetUuid;
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
        if (endDate == null) {
            return null;
        }
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(endDate), ZoneId.systemDefault());
    }

    @Override
    public UUID getUnbannedByUuid() {
        return unbannedByUuid;
    }

    @Override
    public String getTargetName() {
        return targetName;
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
        if (endDate == null) {
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

    @Override
    public boolean isSilentBan() {
        return silentBan;
    }

    @Override
    public boolean isSilentUnban() {
        return silentUnban;
    }

    public Optional<String> getTemplate() {
        return Optional.ofNullable(template);
    }

    public void setSilentUnban(boolean silentUnban) {
        this.silentUnban = silentUnban;
    }

    public boolean hasEnded() {
        return endDate != null && endDate <= System.currentTimeMillis();
    }
}
