package net.shortninja.staffplus.common.bungee;

public enum BungeeContext {
    SESSION("StaffPlusPlusSession"),
    BROADCAST("StaffPlusPlusBroadcast");

    private final String contextString;

    BungeeContext(String contextString) {
        this.contextString = contextString;
    }

    public String getContextString() {
        return contextString;
    }
}
