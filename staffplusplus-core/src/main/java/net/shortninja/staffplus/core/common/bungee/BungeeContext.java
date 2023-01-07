package net.shortninja.staffplus.core.common.bungee;

public enum BungeeContext {
    BROADCAST("StaffPlusPlusBroadcast");

    private final String contextString;

    BungeeContext(String contextString) {
        this.contextString = contextString;
    }

    public String getContextString() {
        return contextString;
    }
}
