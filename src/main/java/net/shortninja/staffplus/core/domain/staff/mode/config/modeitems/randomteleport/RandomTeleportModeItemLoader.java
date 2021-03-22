package net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.randomteleport;

import net.shortninja.staffplus.core.domain.staff.mode.config.ModeItemLoader;
import org.bukkit.configuration.file.FileConfiguration;

public class RandomTeleportModeItemLoader extends ModeItemLoader<RandomTeleportModeConfiguration> {
    @Override
    protected String getModuleName() {
        return "random-teleport-module";
    }

    @Override
    protected RandomTeleportModeConfiguration load(FileConfiguration config) {
        RandomTeleportModeConfiguration modeItemConfiguration = new RandomTeleportModeConfiguration(getModuleName(), config.getBoolean("staff-mode.random-teleport-module.random"));
        return super.loadGeneralConfig(config, modeItemConfiguration);
    }
}
