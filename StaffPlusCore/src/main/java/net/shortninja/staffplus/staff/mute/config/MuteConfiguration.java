package net.shortninja.staffplus.staff.mute.config;

import net.shortninja.staffplus.common.config.GuiItemConfig;

public class MuteConfiguration {

    private final boolean muteEnabled;

    private final String commandMutePlayer;
    private final String commandTempMutePlayer;
    private final String commandUnmutePlayer;
    private final String permissionMutePlayer;
    private final String permissionMuteByPass;
    private final GuiItemConfig guiItemConfig;

    public MuteConfiguration(boolean muteEnabled,
                             String commandMutePlayer,
                             String commandTempMutePlayer,
                             String commandUnmutePlayer, String permissionMutePlayer,
                             String permissionMuteByPass,
                             GuiItemConfig guiItemConfig) {
        this.muteEnabled = muteEnabled;
        this.commandMutePlayer = commandMutePlayer;
        this.commandTempMutePlayer = commandTempMutePlayer;
        this.commandUnmutePlayer = commandUnmutePlayer;
        this.permissionMutePlayer = permissionMutePlayer;
        this.permissionMuteByPass = permissionMuteByPass;
        this.guiItemConfig = guiItemConfig;
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

    public String getPermissionMuteByPass() {
        return permissionMuteByPass;
    }
}
