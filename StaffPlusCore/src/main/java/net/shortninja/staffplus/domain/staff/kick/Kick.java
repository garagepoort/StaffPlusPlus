package net.shortninja.staffplus.domain.staff.kick;

import net.shortninja.staffplus.domain.staff.infractions.Infraction;
import net.shortninja.staffplus.domain.staff.infractions.InfractionType;
import net.shortninja.staffplusplus.kick.IKick;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

public class Kick implements IKick, Infraction {

    private int id;
    private String reason;
    private Long creationDate;
    private String targetName;
    private UUID targetUuid;
    private String issuerName;
    private UUID issuerUuid;
    private String serverName;

    public Kick(int id, String reason, Long creationDate, String targetName, UUID targetUuid, String issuerName, UUID issuerUuid, String serverName) {
        this.id = id;
        this.reason = reason;
        this.creationDate = creationDate;
        this.targetName = targetName;
        this.targetUuid = targetUuid;
        this.issuerName = issuerName;
        this.issuerUuid = issuerUuid;
        this.serverName = serverName;
    }

    public Kick(String reason, String issuerName, UUID issuerUuid, String targetName, UUID targetUuid) {
        this.reason = reason;
        this.targetName = targetName;
        this.issuerName = issuerName;
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
    public String getTargetName() {
        return targetName;
    }

    @Override
    public String getIssuerName() {
        return issuerName;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public InfractionType getInfractionType() {
        return InfractionType.KICK;
    }

    @Override
    public String getServerName() {
        return serverName;
    }
}
