package net.shortninja.staffplus.player.attribute.infraction;

import java.util.UUID;

public class Report {
    private UUID uuid;
    private String name;
    private String reason;
    private String reporterName;
    private UUID reporterUuid;
    private int id;

    public Report(UUID uuid, String name, String reason, String reporterName, UUID reporterUuid) {
        this.uuid = uuid;
        this.name = name;
        this.reason = reason;
        this.reporterName = reporterName;
        this.reporterUuid = reporterUuid;
    }

    public Report(UUID uuid, String name, int id, String reason, String reporterName, UUID reporterUuid, long time) {
        this.uuid = uuid;
        this.name = name;
        this.reason = reason;
        this.reporterName = reporterName;
        this.reporterUuid = reporterUuid;
        this.id = id;
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

    public String getReporterName() {
        return reporterName;
    }

    /*
     * This is only required in order to keep report names up to date when the
     * reporter changes his or her name.
     */
    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public UUID getReporterUuid() {
        return reporterUuid;
    }
}