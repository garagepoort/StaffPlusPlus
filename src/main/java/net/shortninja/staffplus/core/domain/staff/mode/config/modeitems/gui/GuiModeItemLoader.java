package net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.gui;

import be.garagepoort.mcioc.IocBean;

import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.domain.staff.mode.config.ModeItemLoader;
import org.bukkit.configuration.file.FileConfiguration;

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
    protected GuiModeConfiguration load(FileConfiguration config) {
        GuiModeConfiguration modeItemConfiguration = new GuiModeConfiguration(getModuleName(),
            config.getBoolean("staff-mode.gui-module.miner-gui"),
            config.getString("staff-mode.gui-module.miner-title"),
            config.getString("staff-mode.gui-module.miner-name"),
            config.getString("staff-mode.gui-module.miner-lore"),
            config.getInt("staff-mode.gui-module.xray-level")
        );
        return super.loadGeneralConfig(config, modeItemConfiguration);
    }
}
