package net.shortninja.staffplus.core.domain.staff.ban.playerbans.bungee.dto;

import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.bungee.BungeeMessage;
import net.shortninja.staffplusplus.ban.IBan;

import java.util.UUID;

public class BanBungeeDto extends BungeeMessage {

    private final int id;
    private final String reason;
    private final String targetName;
    private final UUID targetUuid;
    private final String issuerName;
    private final UUID issuerUuid;
    private final String unbannedByName;
    private final UUID unbannedByUuid;
    private final String unbanReason;
    private final boolean silentBan;
    private final boolean silentUnban;
    private final Long endTimestamp;
    private String banMessage;

    public BanBungeeDto(IBan ban, String banMessage) {
        super(ban.getServerName());
        this.id = ban.getId();
        this.reason = ban.getReason();
        this.targetName = ban.getReason();
        this.targetUuid = ban.getTargetUuid();
        this.issuerName = ban.getIssuerName();
        this.issuerUuid = ban.getIssuerUuid();
        this.unbannedByName = ban.getUnbannedByName();
        this.unbannedByUuid = ban.getUnbannedByUuid();
        this.unbanReason = ban.getUnbanReason();
        this.silentBan = ban.isSilentBan();
        this.silentUnban = ban.isSilentUnban();
        this.banMessage = banMessage;
        this.endTimestamp = ban.getEndTimestamp();
    }

    public BanBungeeDto(IBan ban) {
        super(ban.getServerName());
        this.id = ban.getId();
        this.reason = ban.getReason();
        this.targetName = ban.getReason();
        this.targetUuid = ban.getTargetUuid();
        this.issuerName = ban.getIssuerName();
        this.issuerUuid = ban.getIssuerUuid();
        this.unbannedByName = ban.getUnbannedByName();
        this.unbannedByUuid = ban.getUnbannedByUuid();
        this.unbanReason = ban.getUnbanReason();
        this.silentBan = ban.isSilentBan();
        this.silentUnban = ban.isSilentUnban();
        this.endTimestamp = ban.getEndTimestamp();
    }

    public int getId() {
        return id;
    }

    public String getReason() {
        return reason;
    }

    public String getTargetName() {
        return targetName;
    }

    public UUID getTargetUuid() {
        return targetUuid;
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

    public String getUnbanReason() {
        return unbanReason;
    }

    public boolean isSilentBan() {
        return silentBan;
    }

    public boolean isSilentUnban() {
        return silentUnban;
    }

    public String getBanMessage() {
        return banMessage;
    }

    public Long getEndTimestamp() {
        return endTimestamp;
    }

    public String getHumanReadableDuration() {
        if(endTimestamp == null) {
            return null;
        }
        long duration = JavaUtils.getDuration(endTimestamp);
        return JavaUtils.toHumanReadableDuration(duration);
    }
}
