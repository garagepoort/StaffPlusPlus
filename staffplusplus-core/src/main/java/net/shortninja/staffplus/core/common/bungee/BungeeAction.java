package net.shortninja.staffplus.core.common.bungee;

public enum BungeeAction {
    FORWARD("Forward");

    private final String actionString;

    BungeeAction(String actionString) {
        this.actionString = actionString;
    }

    public String getActionString() {
        return actionString;
    }
}
