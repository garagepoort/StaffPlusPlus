package net.shortninja.staffplus.core.domain.staff.appeals;

import net.shortninja.staffplusplus.appeals.AppealStatus;
import net.shortninja.staffplusplus.appeals.AppealableType;
import net.shortninja.staffplusplus.appeals.IAppeal;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

public class Appeal implements IAppeal {

    private int id;
    private int appealableId;
    private UUID appealerUuid;
    private String appealerName;
    private UUID resolverUuid;
    private String resolverName;
    private String reason;
    private String resolveReason;
    private AppealStatus status;
    private Long timestamp;
    private AppealableType type;

    public Appeal(int id, int appealableId, UUID appealerUuid, String appealerName, UUID resolverUuid, String resolverName, String reason, String resolveReason, AppealStatus status, Long timestamp, AppealableType type) {
        this.id = id;
        this.appealableId = appealableId;
        this.appealerUuid = appealerUuid;
        this.appealerName = appealerName;
        this.resolverUuid = resolverUuid;
        this.resolverName = resolverName;
        this.reason = reason;
        this.resolveReason = resolveReason;
        this.status = status;
        this.timestamp = timestamp;
        this.type = type;
    }

    public Appeal(int appealableId, UUID appealerUuid, String appealerName, String reason, AppealableType type) {
        this.appealableId = appealableId;
        this.appealerUuid = appealerUuid;
        this.appealerName = appealerName;
        this.reason = reason;
        this.type = type;
        this.status = AppealStatus.OPEN;
        this.timestamp = System.currentTimeMillis();
    }

    public int getId() {
        return id;
    }

    @Override
    public int getAppealableId() {
        return appealableId;
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

    @Override
    public AppealableType getType() {
        return type;
    }
}
