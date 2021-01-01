package net.shortninja.staffplus.staff.kick.config;

public class KickConfiguration {

    private final boolean kickEnabled;

    private final String commandKickPlayer;
    private final String permissionKickPlayer;
    private final String permissionKickByPass;

    public KickConfiguration(boolean kickEnabled,
                             String commandKickPlayer,
                             String permissionKickPlayer,
                             String permissionKickByPass) {
        this.kickEnabled = kickEnabled;
        this.commandKickPlayer = commandKickPlayer;
        this.permissionKickPlayer = permissionKickPlayer;
        this.permissionKickByPass = permissionKickByPass;
    }

    public boolean isEnabled() {
        return kickEnabled;
    }

    public String getCommandKickPlayer() {
        return commandKickPlayer;
    }

    public String getPermissionKickPlayer() {
        return permissionKickPlayer;
    }

    public String getPermissionKickByPass() {
        return permissionKickByPass;
    }
}
