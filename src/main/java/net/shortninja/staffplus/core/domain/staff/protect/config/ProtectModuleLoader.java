package net.shortninja.staffplus.core.domain.staff.protect.config;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.AbstractConfigLoader;
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
        GuiItemConfig guiItemConfig = new GuiItemConfig(modeGuiProtectedAreas, modeGuiProtectedAreasTitle);

        String commandProtectPlayer = commandsConfig.getString("protect-player");
        String commandProtectArea = commandsConfig.getString("protect-area");

        String permissionProtectPlayer = permissionsConfig.getString("protect-player");
        String permissionProtectArea = permissionsConfig.getString("protect-area");

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
