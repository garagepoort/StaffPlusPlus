package net.shortninja.staffplus.staff.warn.warnings;

import net.shortninja.staffplus.staff.infractions.Infraction;
import net.shortninja.staffplus.staff.infractions.InfractionType;
import net.shortninja.staffplus.staff.warn.appeals.Appeal;
import net.shortninja.staffplus.staff.warn.warnings.config.WarningSeverityConfiguration;
import net.shortninja.staffplus.unordered.IWarning;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static net.shortninja.staffplus.unordered.AppealStatus.APPROVED;

public class Warning implements IWarning, Infraction {
    private final UUID uuid;
    private final String name;
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

    public Warning(UUID uuid, String name, String reason, String issuerName, UUID issuerUuid, long time, WarningSeverityConfiguration warningSeverityConfiguration) {
        this.uuid = uuid;
        this.name = name;
        this.reason = reason;
        this.issuerName = issuerName;
        this.issuerUuid = issuerUuid;
        this.time = time;
        this.score = warningSeverityConfiguration.getScore();
        this.severity = warningSeverityConfiguration.getName();
    }

    public Warning(UUID uuid, String name, int id, String reason, String issuerName, UUID issuerUuid, long time, int score, String severity, boolean read, String serverName, Appeal appeal, boolean expired) {
        this.uuid = uuid;
        this.name = name;
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

    public Warning(UUID uuid, String playerName, String reason, String issuerName, UUID issuerUuid, long currentTimeMillis) {
        this.uuid = uuid;
        this.name = playerName;
        this.reason = reason;
        this.issuerName = issuerName;
        this.issuerUuid = issuerUuid;
        this.time = currentTimeMillis;
        this.score = 0;
    }

    public long getTime() {
        return time;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
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
    public ZonedDateTime getTimestamp() {
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
    public UUID getTargetUuid() {
        return uuid;
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
        return getAppeal().map(a -> a.getStatus() == APPROVED).orElse(false);
    }
}