package net.shortninja.staffplus.core.domain.staff.ban.ipbans;

import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplusplus.ban.IIpBan;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

public class IpBan implements IIpBan {

    private Long id;
    private String ip;
    private String issuerName;
    private UUID issuerUuid;
    private String unbannedByName;
    private UUID unbannedByUuid;
    private boolean silentBan;
    private boolean silentUnban;
    private Long creationDate;
    private Long endDate;
    private String serverName;

    public IpBan(Long id, String ip, String issuerName, UUID issuerUuid, String unbannedByName, UUID unbannedByUuid,
                 boolean silentBan, boolean silentUnban, Long creationDate, Long endDate, String serverName) {
        this.id = id;
        this.ip = ip;
        this.issuerName = issuerName;
        this.issuerUuid = issuerUuid;
        this.unbannedByName = unbannedByName;
        this.unbannedByUuid = unbannedByUuid;
        this.silentBan = silentBan;
        this.silentUnban = silentUnban;
        this.creationDate = creationDate;
        this.endDate = endDate;
        this.serverName = serverName;
    }

    public IpBan(String ip, Long endDate, String issuerName, UUID issuerUuid, String serverName, boolean silentBan) {
        this.ip = ip;
        this.issuerName = issuerName;
        this.serverName = serverName;
        this.silentBan = silentBan;
        this.creationDate = System.currentTimeMillis();
        this.endDate = endDate;
        this.issuerUuid = issuerUuid;
    }

    public IpBan(String ip, String issuerName, UUID issuerUuid, String serverName, boolean silentBan) {
        this.ip = ip;
        this.issuerName = issuerName;
        this.silentBan = silentBan;
        this.creationDate = System.currentTimeMillis();
        this.issuerUuid = issuerUuid;
        this.serverName = serverName;
    }

    public Long getId() {
        return id;
    }

    public String getIp() {
        return ip;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIssuerName() {
        return issuerName;
    }

    public UUID getIssuerUuid() {
        return issuerUuid;
    }

    public Optional<String> getUnbannedByName() {
        return Optional.ofNullable(unbannedByName);
    }

    public Optional<UUID> getUnbannedByUuid() {
        return Optional.ofNullable(unbannedByUuid);
    }

    public boolean isSilentBan() {
        return silentBan;
    }

    public boolean isSilentUnban() {
        return silentUnban;
    }

    public Long getCreationDate() {
        return creationDate;
    }

    public Optional<Long> getEndTimestamp() {
        return Optional.ofNullable(endDate);
    }

    public Optional<ZonedDateTime> getEndDateTime() {
        if (endDate == null) {
            return Optional.empty();
        }
        return Optional.of(ZonedDateTime.ofInstant(Instant.ofEpochMilli(endDate), ZoneId.systemDefault()));
    }

    public String getServerName() {
        return serverName;
    }

    @Override
    public boolean isSubnet() {
        return ip.contains("/");
    }

    public void setSilentUnban(boolean silentUnban) {
        this.silentUnban = silentUnban;
    }

    @Override
    public String getHumanReadableDuration() {
        if (endDate == null) {
            return null;
        }
        long duration = JavaUtils.getDuration(endDate);
        return JavaUtils.toHumanReadableDuration(duration);
    }
}
