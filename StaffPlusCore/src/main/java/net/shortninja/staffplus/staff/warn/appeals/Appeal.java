package net.shortninja.staffplus.staff.warn.appeals;

import net.shortninja.staffplus.unordered.AppealStatus;
import net.shortninja.staffplus.unordered.IWarningAppeal;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

public class Appeal implements IWarningAppeal {

    private int id;
    private int warningId;
    private UUID appealerUuid;
    private String appealerName;
    private UUID resolverUuid;
    private String resolverName;
    private String reason;
    private String resolveReason;
    private AppealStatus status;
    private Long timestamp;

    public Appeal(int id, int warningId, UUID appealerUuid, String appealerName, UUID resolverUuid, String resolverName, String reason, String resolveReason, AppealStatus status, Long timestamp) {
        this.id = id;
        this.warningId = warningId;
        this.appealerUuid = appealerUuid;
        this.appealerName = appealerName;
        this.resolverUuid = resolverUuid;
        this.resolverName = resolverName;
        this.reason = reason;
        this.resolveReason = resolveReason;
        this.status = status;
        this.timestamp = timestamp;
    }

    public Appeal(int warningId, UUID appealerUuid, String appealerName, String reason) {
        this.warningId = warningId;
        this.appealerUuid = appealerUuid;
        this.appealerName = appealerName;
        this.reason = reason;
        this.status = AppealStatus.OPEN;
        this.timestamp = System.currentTimeMillis();
    }

    public int getId() {
        return id;
    }

    public int getWarningId() {
        return warningId;
    }

    public UUID getAppealerUuid() {
        return appealerUuid;
    }

    public String getAppealerName() {
        return appealerName;
    }

    public String getReason() {
        return reason;
    }

    public AppealStatus getStatus() {
        return status;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public UUID getResolverUuid() {
        return resolverUuid;
    }

    public String getResolverName() {
        return resolverName;
    }

    public Optional<String> getResolveReason() {
        return Optional.ofNullable(resolveReason);
    }

    @Override
    public Long getCreationTimestamp() {
        return timestamp;
    }

    @Override
    public ZonedDateTime getCreationDate() {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }
}
