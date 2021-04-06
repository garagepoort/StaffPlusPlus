package net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.gui;

import be.garagepoort.mcioc.IocBean;

import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.domain.staff.mode.config.ModeItemLoader;

@IocBean
public class GuiModeItemLoader extends ModeItemLoader<GuiModeConfiguration> {
    public GuiModeItemLoader(IProtocolService protocolService) {
        super(protocolService);
    }

    @Override
    protected String getModuleName() {
        return "gui-module";
    }

    @Override
    protected GuiModeConfiguration load() {
        GuiModeConfiguration modeItemConfiguration = new GuiModeConfiguration(getModuleName(),
            staffModeModulesConfig.getBoolean("modules.gui-module.miner-gui"),
            staffModeModulesConfig.getString("modules.gui-module.miner-title"),
            staffModeModulesConfig.getString("modules.gui-module.miner-name"),
            staffModeModulesConfig.getString("modules.gui-module.miner-lore"),
            staffModeModulesConfig.getInt("modules.gui-module.xray-level")
        );
        return super.loadGeneralConfig(modeItemConfiguration);
    }
}
