package net.shortninja.staffplus.ui;

public enum ArgumentType {

    TELEPORT("-T");

    private String prefix;

    ArgumentType(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
