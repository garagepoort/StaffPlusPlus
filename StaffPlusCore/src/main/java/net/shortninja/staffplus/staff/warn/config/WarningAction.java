package net.shortninja.staffplus.staff.warn.config;

public class WarningAction {

    private WarningActionRunStrategy runStrategy;
    private String command;

    public WarningAction(WarningActionRunStrategy runStrategy, String command) {
        this.runStrategy = runStrategy;
        this.command = command;
    }

    public WarningActionRunStrategy getRunStrategy() {
        return runStrategy;
    }

    public String getCommand() {
        return command;
    }
}
