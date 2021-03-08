package net.shortninja.staffplus.staff.delayedactions;

import java.util.Optional;

public class DelayedAction {

    private String command;
    private Integer executableActionId;
    private final Executor executor;
    private final boolean rollback;

    public DelayedAction(String command, Integer executableActionId, Executor executor, boolean rollback) {
        this.command = command;
        this.executableActionId = executableActionId;
        this.executor = executor;
        this.rollback = rollback;
    }

    public String getCommand() {
        return command;
    }

    public Optional<Integer> getExecutableActionId() {
        return Optional.ofNullable(executableActionId);
    }

    public boolean isRollback() {
        return rollback;
    }

    public Executor getExecutor() {
        return executor;
    }
}
