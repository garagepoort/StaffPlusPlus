package net.shortninja.staffplus.player.attribute.infraction;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.server.data.config.warning.WarningSeverityConfiguration;
import net.shortninja.staffplus.unordered.IWarning;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

public class Warning implements IWarning {
    private Options options = IocContainer.getOptions();
    private UUID uuid;
    private String name;
    private String reason;
    private String issuerName;
    private int score;
    private String severity;
    private UUID issuerUuid;
    private long time;
    private int id;

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

    public Warning(UUID uuid, String name, int id, String reason, String issuerName, UUID issuerUuid, long time, int score, String severity) {
        this.uuid = uuid;
        this.name = name;
        this.reason = reason;
        this.issuerName = issuerName;
        this.issuerUuid = issuerUuid;
        this.time = time;
        this.id = id;
        this.score = score;
        this.severity = severity;
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

    public boolean shouldRemove() {
        boolean shouldRemove = false;

        if ((System.currentTimeMillis() - time) >= options.warningConfiguration.getClear()) {
            shouldRemove = true;
        }

        return shouldRemove;
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
}