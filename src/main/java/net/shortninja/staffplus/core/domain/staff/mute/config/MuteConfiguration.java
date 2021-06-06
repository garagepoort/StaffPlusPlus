package net.shortninja.staffplus.core.domain.staff.mute.config;

import net.shortninja.staffplus.core.common.gui.GuiItemConfig;

public class MuteConfiguration {

    private final boolean muteEnabled;

    private final String commandMutePlayer;
    private final String commandTempMutePlayer;
    private final String commandUnmutePlayer;
    private final String permissionMutePlayer;
    private final String permissionTempmutePlayer;
    private final String permissionUnmutePlayer;
    private final String permissionMuteByPass;
    private final GuiItemConfig guiItemConfig;
    private final String staffNotificationPermission;

    public MuteConfiguration(boolean muteEnabled,
                             String commandMutePlayer,
                             String commandTempMutePlayer,
                             String commandUnmutePlayer,
                             String permissionMutePlayer,
                             String permissionTempmutePlayer, String permissionUnmutePlayer,
                             String permissionMuteByPass,
                             GuiItemConfig guiItemConfig,
                             String staffNotificationPermission) {
        this.muteEnabled = muteEnabled;
        this.commandMutePlayer = commandMutePlayer;
        this.commandTempMutePlayer = commandTempMutePlayer;
        this.commandUnmutePlayer = commandUnmutePlayer;
        this.permissionMutePlayer = permissionMutePlayer;
        this.permissionTempmutePlayer = permissionTempmutePlayer;
        this.permissionUnmutePlayer = permissionUnmutePlayer;
        this.permissionMuteByPass = permissionMuteByPass;
        this.guiItemConfig = guiItemConfig;
        this.staffNotificationPermission = staffNotificationPermission;
    }

    public boolean isEnabled() {
        return muteEnabled;
    }

    public GuiItemConfig getGuiItemConfig() {
        return guiItemConfig;
    }

    public String getCommandMutePlayer() {
        return commandMutePlayer;
    }

    public String getCommandUnmutePlayer() {
        return commandUnmutePlayer;
    }

    public String getCommandTempMutePlayer() {
        return commandTempMutePlayer;
    }

    public String getPermissionMutePlayer() {
        return permissionMutePlayer;
    }

    public String getPermissionTempmutePlayer() {
        return permissionTempmutePlayer;
    }

    public String getPermissionMuteByPass() {
        return permissionMuteByPass;
    }

    public String getPermissionUnmutePlayer() {
        return permissionUnmutePlayer;
    }

    public String getStaffNotificationPermission() {
        return staffNotificationPermission;
    }
}
