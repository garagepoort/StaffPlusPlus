package net.shortninja.staffplus.core.domain.staff.mute;

import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.domain.staff.infractions.Infraction;
import net.shortninja.staffplus.core.domain.staff.infractions.InfractionType;
import net.shortninja.staffplusplus.appeals.AppealableType;
import net.shortninja.staffplusplus.appeals.IAppeal;
import net.shortninja.staffplusplus.investigate.evidence.Evidence;
import net.shortninja.staffplusplus.mute.IMute;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

public class Mute implements IMute, Infraction, Evidence {

    private int id;
    private final String reason;
    private Long endDate;
    private final Long creationDate;
    private final String targetName;
    private final UUID targetUuid;
    private final String issuerName;
    private final UUID issuerUuid;
    private String unmutedByName;
    private UUID unmutedByUuid;
    private String unmuteReason;
    private String serverName;
    private final boolean softMute;
    private IAppeal appeal;

    public Mute(int id,
                String reason,
                Long creationDate,
                Long endDate,
                String targetName,
                UUID playerUuid,
                String issuerName,
                UUID issuerUuid,
                String unmutedByName,
                UUID unmutedByUuid,
                String unmuteReason,
                String serverName,
                boolean softMute,
                IAppeal appeal) {
        this.id = id;
        this.reason = reason;
        this.creationDate = creationDate;
        this.endDate = endDate;
        this.targetName = targetName;
        this.targetUuid = playerUuid;
        this.issuerName = issuerName;
        this.issuerUuid = issuerUuid;
        this.unmutedByName = unmutedByName;
        this.unmutedByUuid = unmutedByUuid;
        this.unmuteReason = unmuteReason;
        this.serverName = serverName;
        this.softMute = softMute;
        this.appeal = appeal;
    }

    public Mute(String reason, Long endDate, String issuerName, UUID issuerUuid, String targetName, UUID playerUuid, boolean softMute) {
        this.reason = reason;
        this.targetName = targetName;
        this.issuerName = issuerName;
        this.softMute = softMute;
        this.creationDate = System.currentTimeMillis();
        this.endDate = endDate;
        this.targetUuid = playerUuid;
        this.issuerUuid = issuerUuid;
    }

    public Mute(String reason, String issuerName, UUID issuerUuid, String targetName, UUID playerUuid, boolean softMute) {
        this.reason = reason;
        this.targetName = targetName;
        this.issuerName = issuerName;
        this.softMute = softMute;
        this.creationDate = System.currentTimeMillis();
        this.targetUuid = playerUuid;
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
    public Optional<? extends IAppeal> getAppeal() {
        return Optional.ofNullable(appeal);
    }

    @Override
    public void setAppeal(IAppeal appeal) {
        this.appeal = appeal;
    }

    @Override
    public AppealableType getType() {
        return AppealableType.MUTE;
    }

    @Override
    public String getEvidenceType() {
        return "MUTE";
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
    public UUID getUnmutedByUuid() {
        return unmutedByUuid;
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
    public String getUnmutedByName() {
        return unmutedByName;
    }

    @Override
    public String getUnmuteReason() {
        return unmuteReason;
    }

    @Override
    public String getHumanReadableDuration() {
        if (endDate == null) {
            return null;
        }
        long duration = JavaUtils.getDuration(endDate);
        return JavaUtils.toHumanReadableDuration(duration);
    }

    public void setUnmutedByName(String unmutedByName) {
        this.unmutedByName = unmutedByName;
    }

    public void setUnmutedByUuid(UUID unmutedByUuid) {
        this.unmutedByUuid = unmutedByUuid;
    }

    public void setUnmuteReason(String unmuteReason) {
        this.unmuteReason = unmuteReason;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public InfractionType getInfractionType() {
        return InfractionType.MUTE;
    }

    @Override
    public String getServerName() {
        return serverName;
    }

    public boolean hasEnded() {
        return endDate != null && endDate <= System.currentTimeMillis();
    }

    @Override
    public boolean isSoftMute() {
        return softMute;
    }
}
