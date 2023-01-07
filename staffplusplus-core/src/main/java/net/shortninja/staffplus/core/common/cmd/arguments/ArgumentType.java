package net.shortninja.staffplus.core.common.cmd.arguments;

public enum ArgumentType {

    DELAY("-D"),
    TELEPORT("-T"),
    HEALTH("-H"),
    STRIP("-S");

    private String prefix;

    ArgumentType(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
