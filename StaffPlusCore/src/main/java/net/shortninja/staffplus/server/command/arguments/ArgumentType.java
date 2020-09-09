package net.shortninja.staffplus.server.command.arguments;

public enum ArgumentType {

    TELEPORT("-T"),
    STRIP("-S");

    private String prefix;

    ArgumentType(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
