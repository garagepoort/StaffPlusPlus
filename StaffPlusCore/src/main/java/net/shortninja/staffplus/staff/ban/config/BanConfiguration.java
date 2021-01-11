package net.shortninja.staffplus.staff.ban.config;

import net.shortninja.staffplus.common.config.GuiItemConfig;

public class BanConfiguration {

    private final boolean banEnabled;

    private final String commandBanPlayer;
    private final String commandTempBanPlayer;
    private final String commandUnbanPlayer;
    private final String permissionBanPlayer;
    private final String permissionUnbanPlayer;
    private final String permissionBanByPass;
    private final GuiItemConfig guiItemConfig;

    public BanConfiguration(boolean banEnabled,
                            String commandBanPlayer,
                            String commandTempBanPlayer,
                            String commandUnbanPlayer,
                            String permissionBanPlayer,
                            String permissionUnbanPlayer,
                            String permissionBanByPass,
                            GuiItemConfig guiItemConfig) {
        this.banEnabled = banEnabled;
        this.commandBanPlayer = commandBanPlayer;
        this.commandTempBanPlayer = commandTempBanPlayer;
        this.commandUnbanPlayer = commandUnbanPlayer;
        this.permissionBanPlayer = permissionBanPlayer;
        this.permissionUnbanPlayer = permissionUnbanPlayer;
        this.permissionBanByPass = permissionBanByPass;
        this.guiItemConfig = guiItemConfig;
    }

    public boolean isEnabled() {
        return banEnabled;
    }

    public GuiItemConfig getGuiItemConfig() {
        return guiItemConfig;
    }

    public String getCommandBanPlayer() {
        return commandBanPlayer;
    }

    public String getCommandUnbanPlayer() {
        return commandUnbanPlayer;
    }

    public String getCommandTempBanPlayer() {
        return commandTempBanPlayer;
    }

    public String getPermissionBanPlayer() {
        return permissionBanPlayer;
    }

    public String getPermissionBanByPass() {
        return permissionBanByPass;
    }

    public String getPermissionUnbanPlayer() {
        return permissionUnbanPlayer;
    }
}
