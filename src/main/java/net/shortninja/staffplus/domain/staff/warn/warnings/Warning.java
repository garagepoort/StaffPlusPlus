package net.shortninja.staffplus.domain.staff.warn.warnings;

import net.shortninja.staffplus.domain.staff.infractions.Infraction;
import net.shortninja.staffplus.domain.staff.infractions.InfractionType;
import net.shortninja.staffplus.domain.staff.warn.appeals.Appeal;
import net.shortninja.staffplus.domain.staff.warn.warnings.config.WarningSeverityConfiguration;
import net.shortninja.staffplusplus.warnings.AppealStatus;
import net.shortninja.staffplusplus.warnings.IWarning;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

public class Warning implements IWarning, Infraction {
    private final UUID targetUuid;
    private final String targetName;
    private final String reason;
    private final int score;
    private final UUID issuerUuid;
    private final long time;
    private int id;
    private String issuerName;
    private String severity;
    private boolean read;
    private String serverName;
    private Appeal appeal;
    private boolean expired;

    public Warning(UUID targetUuid, String targetName, String reason, String issuerName, UUID issuerUuid, long time, WarningSeverityConfiguration warningSeverityConfiguration) {
        this.targetUuid = targetUuid;
        this.targetName = targetName;
        this.reason = reason;
        this.issuerName = issuerName;
        this.issuerUuid = issuerUuid;
        this.time = time;
        this.score = warningSeverityConfiguration.getScore();
        this.severity = warningSeverityConfiguration.getName();
    }

    public Warning(UUID targetUuid, String targetName, int id, String reason, String issuerName, UUID issuerUuid, long time, int score, String severity, boolean read, String serverName, Appeal appeal, boolean expired) {
        this.targetUuid = targetUuid;
        this.targetName = targetName;
        this.reason = reason;
        this.issuerName = issuerName;
        this.issuerUuid = issuerUuid;
        this.time = time;
        this.id = id;
        this.score = score;
        this.severity = severity;
        this.read = read;
        this.serverName = serverName;
        this.appeal = appeal;
        this.expired = expired;
    }

    public Warning(UUID targetUuid, String playerName, String reason, String issuerName, UUID issuerUuid, long currentTimeMillis) {
        this.targetUuid = targetUuid;
        this.targetName = playerName;
        this.reason = reason;
        this.issuerName = issuerName;
        this.issuerUuid = issuerUuid;
        this.time = currentTimeMillis;
        this.score = 0;
    }

    public UUID getTargetUuid() {
        return targetUuid;
    }

    public String getTargetName() {
        return targetName;
    }

    public String getReason() {
        return reason;
    }

    public String getIssuerName() {
        return issuerName;
    }

    /*
     * This is only required in order to keep warning names up to date when the
     * issuer changes his or her name.
     */
    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }

    public UUID getIssuerUuid() {
        return issuerUuid;
    }

    public int getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    public String getSeverity() {
        return severity;
    }

    @Override
    public ZonedDateTime getCreationDate() {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
    }

    public boolean isRead() {
        return read;
    }

    @Override
    public String getActionableType() {
        return "WARNING";
    }

    @Override
    public InfractionType getInfractionType() {
        return InfractionType.WARNING;
    }

    @Override
    public Long getCreationTimestamp() {
        return time;
    }


    @Override
    public String getServerName() {
        return serverName;
    }

    public Optional<Appeal> getAppeal() {
        return Optional.ofNullable(appeal);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAppeal(Appeal appeal) {
        this.appeal = appeal;
    }

    public boolean isExpired() {
        return expired;
    }

    public boolean hasApprovedAppeal() {
        return getAppeal().map(a -> a.getStatus() == AppealStatus.APPROVED).orElse(false);
    }
}