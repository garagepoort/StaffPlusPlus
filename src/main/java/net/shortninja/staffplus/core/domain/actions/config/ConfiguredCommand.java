package net.shortninja.staffplus.core.domain.actions.config;

import be.garagepoort.mcioc.configuration.ConfigEmbeddedObject;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.configuration.ConfigTransformer;
import be.garagepoort.mcioc.configuration.transformers.ToEnum;
import net.shortninja.staffplus.core.domain.actions.ActionRunStrategy;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ConfiguredCommand {

    @ConfigProperty(value = "command", required = true)
    private String command;

    @ConfigProperty("executor")
    private String executor = "console";

    @ConfigProperty("executor-run-strategy")
    @ConfigTransformer(ToEnum.class)
    private ActionRunStrategy executorRunStrategy = ActionRunStrategy.ONLINE;

    @ConfigProperty("target")
    private String target;

    @ConfigProperty("target-run-strategy")
    @ConfigTransformer(ToEnum.class)
    private ActionRunStrategy targetRunStrategy;

    @ConfigProperty("filters")
    @ConfigTransformer(FiltersTransformer.class)
    private Map<String, String> filters = new HashMap<>();

    @ConfigProperty("rollback-command")
    @ConfigEmbeddedObject(ConfiguredCommand.class)
    private ConfiguredCommand rollbackCommand;

    public ConfiguredCommand() {}

    public ConfiguredCommand(String command, String executor, ActionRunStrategy executorRunStrategy, String target, ActionRunStrategy targetRunStrategy, Map<String, String> filters, ConfiguredCommand rollbackCommand) {
        this.command = command;
        this.executor = executor;
        this.executorRunStrategy = executorRunStrategy;
        this.target = target;
        this.targetRunStrategy = targetRunStrategy;
        this.rollbackCommand = rollbackCommand;
        this.filters = new HashMap<>();
        filters.forEach((k, v) -> this.filters.put(k.toLowerCase(), v.toLowerCase()));
    }

    public String getCommand() {
        return command;
    }

    public String getExecutor() {
        return executor;
    }

    public ActionRunStrategy getExecutorRunStrategy() {
        return executorRunStrategy;
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
