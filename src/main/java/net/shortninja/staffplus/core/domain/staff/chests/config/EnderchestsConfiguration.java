package net.shortninja.staffplus.core.domain.staff.chests.config;

public class EnderchestsConfiguration {

    private final boolean enabled;

    private final String commandOpenEnderChests;
    private final String permissionViewOnline;
    private final String permissionViewOffline;
    private final String permissionInteract;

    public EnderchestsConfiguration(boolean enabled, String commandOpenEnderChests, String permissionViewOnline, String permissionViewOffline, String permissionInteract) {
        this.enabled = enabled;
        this.commandOpenEnderChests = commandOpenEnderChests;
        this.permissionViewOnline = permissionViewOnline;
        this.permissionViewOffline = permissionViewOffline;
        this.permissionInteract = permissionInteract;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getCommandOpenEnderChests() {
        return commandOpenEnderChests;
    }

    public String getPermissionViewOnline() {
        return permissionViewOnline;
    }

    public String getPermissionViewOffline() {
        return permissionViewOffline;
    }

    public String getPermissionInteract() {
        return permissionInteract;
    }
}
