package net.shortninja.staffplus.domain.actions;

import java.util.HashMap;
import java.util.Map;

public class ConfiguredAction {

    private String command;
    private String rollbackCommand;
    private ActionRunStrategy runStrategy;
    private ActionRunStrategy rollbackRunStrategy;
    private Map<String, String> filters;

    public ConfiguredAction(String command, String rollbackCommand, ActionRunStrategy runStrategy, ActionRunStrategy rollbackRunStrategy, Map<String, String> filters) {
        this.command = command;
        this.rollbackCommand = rollbackCommand;
        this.runStrategy = runStrategy;
        this.rollbackRunStrategy = rollbackRunStrategy;
        this.filters = new HashMap<>();
        filters.forEach((k, v) -> this.filters.put(k.toLowerCase(), v.toLowerCase()));
    }

    public String getCommand() {
        return command;
    }

    public ActionRunStrategy getRunStrategy() {
        return runStrategy;
    }

    public Map<String, String> getFilters() {
        return filters;
    }

    public String getRollbackCommand() {
        return rollbackCommand;
    }

    public ActionRunStrategy getRollbackRunStrategy() {
        return rollbackRunStrategy;
    }
}
