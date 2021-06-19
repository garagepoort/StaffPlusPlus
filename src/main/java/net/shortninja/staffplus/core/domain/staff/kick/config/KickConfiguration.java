package net.shortninja.staffplus.core.domain.staff.kick.config;

import java.util.List;

public class KickConfiguration {

    private final boolean kickEnabled;

    private final String commandKickPlayer;
    private final String permissionKickPlayer;
    private final String permissionKickByPass;
    private final boolean fixedReason;
    private final List<KickReasonConfiguration> kickReasons;

    public KickConfiguration(boolean kickEnabled,
                             String commandKickPlayer,
                             String permissionKickPlayer,
                             String permissionKickByPass, boolean fixedReason,
                             List<KickReasonConfiguration> kickReasons) {
        this.kickEnabled = kickEnabled;
        this.commandKickPlayer = commandKickPlayer;
        this.permissionKickPlayer = permissionKickPlayer;
        this.permissionKickByPass = permissionKickByPass;
        this.fixedReason = fixedReason;
        this.kickReasons = kickReasons;
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

    public List<KickReasonConfiguration> getKickReasons() {
        return kickReasons;
    }

    public boolean isFixedReason() {
        return fixedReason;
    }
}
