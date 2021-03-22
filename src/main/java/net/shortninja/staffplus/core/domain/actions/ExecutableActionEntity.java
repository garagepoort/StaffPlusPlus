package net.shortninja.staffplus.core.domain.actions;

import net.shortninja.staffplusplus.Actionable;

public class ExecutableActionEntity {

    private int id;

    private String command;
    private String rollbackCommand;
    private ActionRunStrategy runStrategy;
    private ActionRunStrategy rollbackRunStrategy;

    private Long executionTimestamp;
    private Long rollbackTimestamp;

    private int actionableId;
    private String actionableType;

    public ExecutableActionEntity(int id, String command, String rollbackCommand,
                                  ActionRunStrategy runStrategy,
                                  ActionRunStrategy rollbackRunStrategy, Long executionTimestamp, Long rollbackTimestamp,
                                  int actionableId, String actionableType) {
        this.id = id;
        this.command = command;
        this.rollbackCommand = rollbackCommand;
        this.runStrategy = runStrategy;
        this.rollbackRunStrategy = rollbackRunStrategy;
        this.executionTimestamp = executionTimestamp;
        this.rollbackTimestamp = rollbackTimestamp;
        this.actionableId = actionableId;
        this.actionableType = actionableType;
    }

    public ExecutableActionEntity(ConfiguredAction configuredAction, Actionable actionable, boolean executed) {
        this.command = configuredAction.getCommand();
        this.rollbackCommand = configuredAction.getRollbackCommand();
        this.runStrategy = configuredAction.getRunStrategy();
        this.rollbackRunStrategy = configuredAction.getRollbackRunStrategy();
        this.executionTimestamp = executed ? System.currentTimeMillis() : null;
        this.actionableId = actionable.getId();
        this.actionableType = actionable.getActionableType();
    }

    public int getId() {
        return id;
    }

    public String getCommand() {
        return command;
    }

    public String getRollbackCommand() {
        return rollbackCommand;
    }

    public boolean isExecuted() {
        return executionTimestamp != null;
    }

    public boolean isRollbacked() {
        return rollbackTimestamp != null;
    }

    public int getActionableId() {
        return actionableId;
    }

    public String getActionableType() {
        return actionableType;
    }

    public ActionRunStrategy getRunStrategy() {
        return runStrategy;
    }

    public ActionRunStrategy getRollbackRunStrategy() {
        return rollbackRunStrategy;
    }

    public Long getExecutionTimestamp() {
        return executionTimestamp;
    }

    public Long getRollbackTimestamp() {
        return rollbackTimestamp;
    }
}
