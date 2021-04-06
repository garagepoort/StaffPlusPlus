package net.shortninja.staffplus.core.domain.staff.protect.config;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.config.AbstractConfigLoader;
import net.shortninja.staffplus.core.common.gui.GuiItemConfig;

@IocBean
public class ProtectModuleLoader extends AbstractConfigLoader<ProtectConfiguration> {

    @Override
    protected ProtectConfiguration load() {
        boolean playerProtectEnabled = defaultConfig.getBoolean("protect-module.player-enabled");
        boolean areaProtectEnabled = defaultConfig.getBoolean("protect-module.area-enabled");
        int areaMaxSize = defaultConfig.getInt("protect-module.area-max-size");

        boolean modeGuiProtectedAreas = staffModeModulesConfig.getBoolean("modules.gui-module.protected-areas-gui");
        String modeGuiProtectedAreasTitle = staffModeModulesConfig.getString("modules.gui-module.protected-areas-title");
        String modeGuiProtectedAreasName = staffModeModulesConfig.getString("modules.gui-module.protected-areas-name");
        String modeGuiProtectedAreasLore = staffModeModulesConfig.getString("modules.gui-module.protected-areas-lore");
        GuiItemConfig guiItemConfig = new GuiItemConfig(modeGuiProtectedAreas, modeGuiProtectedAreasTitle, modeGuiProtectedAreasName, modeGuiProtectedAreasLore);

        String commandProtectPlayer = defaultConfig.getString("commands.protect-player");
        String commandProtectArea = defaultConfig.getString("commands.protect-area");

        String permissionProtectPlayer = defaultConfig.getString("permissions.protect-player");
        String permissionProtectArea = defaultConfig.getString("permissions.protect-area");

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
