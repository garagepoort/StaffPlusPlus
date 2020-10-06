package net.shortninja.staffplus.staff.protect.config;

import net.shortninja.staffplus.common.config.GuiItemConfig;

public class ProtectConfiguration {

    private final boolean playerProtectEnabled;
    private final boolean areaProtectEnabled;
    private final int areaMaxSize;

    private final String commandProtectPlayer;
    private final String commandProtectArea;
    private final String permissionProtectPlayer;
    private final String permissionProtectArea;
    private final GuiItemConfig guiItemConfig;

    public ProtectConfiguration(boolean playerProtectEnabled,
                                boolean areaProtectEnabled,
                                int areaMaxSize,
                                String commandProtectPlayer,
                                String commandProtectArea,
                                String permissionProtectPlayer,
                                String permissionProtectArea,
                                GuiItemConfig guiItemConfig) {
        this.playerProtectEnabled = playerProtectEnabled;
        this.areaProtectEnabled = areaProtectEnabled;
        this.areaMaxSize = areaMaxSize;
        this.commandProtectPlayer = commandProtectPlayer;
        this.commandProtectArea = commandProtectArea;
        this.permissionProtectPlayer = permissionProtectPlayer;
        this.permissionProtectArea = permissionProtectArea;
        this.guiItemConfig = guiItemConfig;
    }

    public boolean isPlayerProtectEnabled() {
        return playerProtectEnabled;
    }

    public boolean isAreaProtectEnabled() {
        return areaProtectEnabled;
    }

    public int getAreaMaxSize() {
        return areaMaxSize;
    }

    public GuiItemConfig getGuiItemConfig() {
        return guiItemConfig;
    }

    public String getCommandProtectPlayer() {
        return commandProtectPlayer;
    }

    public String getCommandProtectArea() {
        return commandProtectArea;
    }

    public String getPermissionProtectPlayer() {
        return permissionProtectPlayer;
    }

    public String getPermissionProtectArea() {
        return permissionProtectArea;
    }
}
