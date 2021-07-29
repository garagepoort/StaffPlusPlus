package net.shortninja.staffplus.core.domain.staff.ban.ipbans.bungee.dto;

import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.bungee.BungeeMessage;
import net.shortninja.staffplusplus.ban.IIpBan;

import java.util.UUID;

public class IpBanBungeeDto extends BungeeMessage {

    private final Long id;
    private final String ip;
    private final String issuerName;
    private final UUID issuerUuid;
    private final String unbannedByName;
    private final UUID unbannedByUuid;
    private final boolean silentBan;
    private final boolean silentUnban;
    private final Long creationDate;
    private final Long endTimestamp;
    private final boolean isSubnet;
    private String template;

    public IpBanBungeeDto(IIpBan ban, String template) {
        super(ban.getServerName());
        this.id = ban.getId();
        this.ip = ban.getIp();
        this.issuerName = ban.getIssuerName();
        this.issuerUuid = ban.getIssuerUuid();
        this.unbannedByName = ban.getUnbannedByName().orElse(null);
        this.unbannedByUuid = ban.getUnbannedByUuid().orElse(null);
        this.silentBan = ban.isSilentBan();
        this.silentUnban = ban.isSilentUnban();
        this.creationDate = ban.getCreationDate();
        this.endTimestamp = ban.getEndTimestamp().orElse(null);
        this.isSubnet = ban.isSubnet();
        this.template = template;
    }
    public IpBanBungeeDto(IIpBan ban) {
        super(ban.getServerName());
        this.id = ban.getId();
        this.ip = ban.getIp();
        this.issuerName = ban.getIssuerName();
        this.issuerUuid = ban.getIssuerUuid();
        this.unbannedByName = ban.getUnbannedByName().orElse(null);
        this.unbannedByUuid = ban.getUnbannedByUuid().orElse(null);
        this.silentBan = ban.isSilentBan();
        this.silentUnban = ban.isSilentUnban();
        this.creationDate = ban.getCreationDate();
        this.endTimestamp = ban.getEndTimestamp().orElse(null);
        this.isSubnet = ban.isSubnet();
    }

    public Long getId() {
        return id;
    }

    public String getIp() {
        return ip;
    }

    public String getIssuerName() {
        return issuerName;
    }

    public UUID getIssuerUuid() {
        return issuerUuid;
    }

    public String getUnbannedByName() {
        return unbannedByName;
    }

    public UUID getUnbannedByUuid() {
        return unbannedByUuid;
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

    public Long getEndTimestamp() {
        return endTimestamp;
    }

    public String getHumanReadableDuration() {
        if (endTimestamp == null) {
            return null;
        }
        long duration = JavaUtils.getDuration(endTimestamp);
        return JavaUtils.toHumanReadableDuration(duration);
    }

    public boolean isSubnet() {
        return isSubnet;
    }

    public String getTemplate() {
        return template;
    }
}
