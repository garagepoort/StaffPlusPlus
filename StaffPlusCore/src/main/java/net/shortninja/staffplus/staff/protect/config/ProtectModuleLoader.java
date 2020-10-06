package net.shortninja.staffplus.staff.protect.config;

import net.shortninja.staffplus.common.config.ConfigLoader;
import net.shortninja.staffplus.common.config.GuiItemConfig;

public class ProtectModuleLoader extends ConfigLoader<ProtectConfiguration> {

    @Override
    public ProtectConfiguration load() {
        boolean playerProtectEnabled = config.getBoolean("protect-module.player-enabled");
        boolean areaProtectEnabled = config.getBoolean("protect-module.area-enabled");
        int areaMaxSize = config.getInt("protect-module.area-max-size");

        boolean modeGuiProtectedAreas = config.getBoolean("staff-mode.gui-module.protected-areas-gui");
        String modeGuiProtectedAreasTitle = config.getString("staff-mode.gui-module.protected-areas-title");
        String modeGuiProtectedAreasName = config.getString("staff-mode.gui-module.protected-areas-name");
        String modeGuiProtectedAreasLore = config.getString("staff-mode.gui-module.protected-areas-lore");
        GuiItemConfig guiItemConfig = new GuiItemConfig(modeGuiProtectedAreas, modeGuiProtectedAreasTitle, modeGuiProtectedAreasName, modeGuiProtectedAreasLore);

        String commandProtectPlayer = config.getString("commands.protect-player");
        String commandProtectArea = config.getString("commands.protect-area");

        String permissionProtectPlayer = config.getString("permissions.protect-player");
        String permissionProtectArea = config.getString("permissions.protect-area");

        return new ProtectConfiguration(
            playerProtectEnabled,
            areaProtectEnabled,
            areaMaxSize,
            commandProtectPlayer,
            commandProtectArea,
            permissionProtectPlayer,
            permissionProtectArea,
            guiItemConfig);
    }
}
