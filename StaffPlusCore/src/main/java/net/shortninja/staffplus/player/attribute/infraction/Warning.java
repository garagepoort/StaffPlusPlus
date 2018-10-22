package net.shortninja.staffplus.player.attribute.infraction;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.server.data.config.Options;

import java.util.UUID;

public class Warning {
    private Options options = StaffPlus.get().options;
    private UUID uuid;
    private String name;
    private String reason;
    private String issuerName;
    private UUID issuerUuid;
    private long time;
    private int id;

    public Warning(UUID uuid, String name, String reason, String issuerName, UUID issuerUuid, long time) {
        this.uuid = uuid;
        this.name = name;
        this.reason = reason;
        this.issuerName = issuerName;
        this.issuerUuid = issuerUuid;
        this.time = time;
    }

    public Warning(UUID uuid, String name, int id, String reason, String issuerName, UUID issuerUuid, long time) {
        this.uuid = uuid;
        this.name = name;
        this.reason = reason;
        this.issuerName = issuerName;
        this.issuerUuid = issuerUuid;
        this.time = time;
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

    public long getTime() {
        return time;
    }

    public int getId() {
        return id;
    }

    public boolean shouldRemove() {
        boolean shouldRemove = false;

        if ((System.currentTimeMillis() - time) >= options.warningsClear) {
            shouldRemove = true;
        }

        return shouldRemove;
    }
}