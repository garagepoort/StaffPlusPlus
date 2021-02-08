package net.shortninja.staffplus.common.actions;

import java.util.Map;

public class ExecutableAction {

    private String command;
    private ActionRunStrategy runStrategy;
    private Map<String, String> filters;

    public ExecutableAction(String command, ActionRunStrategy runStrategy, Map<String, String> filters) {
        this.command = command;
        this.runStrategy = runStrategy;
        this.filters = filters;
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
}
