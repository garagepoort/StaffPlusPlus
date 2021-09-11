package net.shortninja.staffplus.core.domain.actions;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ConfiguredCommand {

    private final String command;
    private final String executioner;
    private final ActionRunStrategy executionerRunStrategy;
    private final String target;
    private final ActionRunStrategy targetRunStrategy;
    private final Map<String, String> filters;
    private final ConfiguredCommand rollbackCommand;

    public ConfiguredCommand(String command, String executioner, ActionRunStrategy executionerRunStrategy, String target, ActionRunStrategy targetRunStrategy, Map<String, String> filters, ConfiguredCommand rollbackCommand) {
        this.command = command;
        this.executioner = executioner;
        this.executionerRunStrategy = executionerRunStrategy;
        this.target = target;
        this.targetRunStrategy = targetRunStrategy;
        this.rollbackCommand = rollbackCommand;
        this.filters = new HashMap<>();
        filters.forEach((k, v) -> this.filters.put(k.toLowerCase(), v.toLowerCase()));
    }

    public String getCommand() {
        return command;
    }

    public String getExecutioner() {
        return executioner;
    }

    public ActionRunStrategy getExecutionerRunStrategy() {
        return executionerRunStrategy;
    }

    public Optional<String> getTarget() {
        return Optional.ofNullable(target);
    }

    public Optional<ActionRunStrategy> getTargetRunStrategy() {
        return Optional.ofNullable(targetRunStrategy);
    }

    public Map<String, String> getFilters() {
        return filters;
    }

    public Optional<ConfiguredCommand> getRollbackCommand() {
        return Optional.ofNullable(rollbackCommand);
    }
}
