package net.shortninja.staffplus.staff.mode.config.modeitems.gui;

import net.shortninja.staffplus.staff.mode.config.ModeItemLoader;
import org.bukkit.configuration.file.FileConfiguration;

public class GuiModeItemLoader extends ModeItemLoader<GuiModeConfiguration> {
    @Override
    protected String getModuleName() {
        return "gui-module";
    }

    @Override
    protected GuiModeConfiguration load(FileConfiguration config) {
        GuiModeConfiguration modeItemConfiguration = new GuiModeConfiguration(
            config.getBoolean("staff-mode.gui-module.miner-gui"),
            config.getString("staff-mode.gui-module.miner-title"),
            config.getString("staff-mode.gui-module.miner-name"),
            config.getString("staff-mode.gui-module.miner-lore"),
            config.getInt("staff-mode.gui-module.xray-level")
        );
        return super.loadGeneralConfig(config, modeItemConfiguration);
    }
}
