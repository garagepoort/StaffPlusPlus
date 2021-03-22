package net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.compass;

import net.shortninja.staffplus.core.domain.staff.mode.config.ModeItemLoader;
import org.bukkit.configuration.file.FileConfiguration;

public class CompassModeItemLoader extends ModeItemLoader<CompassModeConfiguration> {
    @Override
    protected String getModuleName() {
        return "compass-module";
    }

    @Override
    protected CompassModeConfiguration load(FileConfiguration config) {
        int velocity = config.getInt("staff-mode.compass-module.velocity");

        CompassModeConfiguration modeItemConfiguration = new CompassModeConfiguration(getModuleName(), velocity);
        return super.loadGeneralConfig(config, modeItemConfiguration);
    }
}
