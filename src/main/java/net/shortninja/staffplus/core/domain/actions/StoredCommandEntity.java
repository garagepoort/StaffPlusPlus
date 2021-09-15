package net.shortninja.staffplus.core.domain.actions;

import org.bukkit.OfflinePlayer;

import java.util.Optional;
import java.util.UUID;

public class StoredCommandEntity {

    private Integer id;
    private final String command;
    private final UUID executorUuid;
    private final UUID targetUuid;
    private final ActionRunStrategy executorRunStrategy;
    private final ActionRunStrategy targetRunStrategy;

    private final Long creationTimestamp;
    private Long executionTimestamp;
    private Long rollbackTimestamp;

    private final Integer actionableId;
    private final String actionableType;
    private final String serverName;

    private final StoredCommandEntity rollbackCommand;
    private boolean delayed;

    public StoredCommandEntity(Integer id,
                               String command,
                               UUID executorUuid,
                               UUID targetUuid,
                               ActionRunStrategy executorRunStrategy,
                               ActionRunStrategy targetRunStrategy,
                               Long creationTimestamp,
                               Long executionTimestamp,
                               Long rollbackTimestamp, Integer actionableId,
                               String actionableType,
                               String serverName, StoredCommandEntity rollbackCommand, boolean delayed) {
        this.id = id;
        this.command = command;
        this.executorUuid = executorUuid;
        this.targetUuid = targetUuid;
        this.executorRunStrategy = executorRunStrategy;
        this.targetRunStrategy = targetRunStrategy;
        this.creationTimestamp = creationTimestamp;
        this.executionTimestamp = executionTimestamp;
        this.rollbackTimestamp = rollbackTimestamp;
        this.actionableId = actionableId;
        this.actionableType = actionableType;
        this.serverName = serverName;
        this.rollbackCommand = rollbackCommand;
        this.delayed = delayed;
    }

    public StoredCommandEntity(CreateStoredCommandRequest request, boolean isDelayed) {
        this.command = request.getCommand();
        this.executorUuid = request.getExecutor();
        this.executorRunStrategy = request.getExecutorRunStrategy();
        this.targetUuid = request.getTarget().map(OfflinePlayer::getUniqueId).orElse(null);
        this.targetRunStrategy = request.getTargetRunStrategy().orElse(null);
        this.creationTimestamp = System.currentTimeMillis();
        this.executionTimestamp = null;
        this.actionableId = request.getActionableId().orElse(null);
        this.actionableType = request.getActionableType().orElse(null);
        this.serverName = request.getServerName();
        this.rollbackCommand = request.getRollbackCommand().map(s -> new StoredCommandEntity(s, false)).orElse(null);
        this.delayed = isDelayed;
    }

    public Integer getId() {
        return id;
    }

    public String getCommand() {
        return command;
    }

    public UUID getExecutorUuid() {
        return executorUuid;
    }

    public Optional<UUID> getTargetUuid() {
        return Optional.ofNullable(targetUuid);
    }

    public ActionRunStrategy getExecutorRunStrategy() {
        return executorRunStrategy;
    }

    public Optional<ActionRunStrategy> getTargetRunStrategy() {
        return Optional.ofNullable(targetRunStrategy);
    }

    public Long getCreationTimestamp() {
        return creationTimestamp;
    }

    public Optional<Long> getExecutionTimestamp() {
        return Optional.ofNullable(executionTimestamp);
    }

    public Optional<Integer> getActionableId() {
        return Optional.ofNullable(actionableId);
    }

    public Optional<String> getActionableType() {
        return Optional.ofNullable(actionableType);
    }

    public Optional<StoredCommandEntity> getRollbackCommand() {
        return Optional.ofNullable(rollbackCommand);
    }

    public boolean isDelayed() {
        return delayed;
    }

    public void setExecutionTimestamp(long executionTimestamp) {
        this.executionTimestamp = executionTimestamp;
    }

    public boolean isRollbackable() {
        return rollbackCommand != null;
    }
    public boolean isRollbacked() {
        return rollbackCommand != null && rollbackTimestamp != null;
    }

    public boolean isExecuted() {
        return executionTimestamp != null;
    }

    public Optional<String> getServerName() {
        return Optional.ofNullable(serverName);
    }

    public void setId(int id) {
        this.id = id;
    }
}
