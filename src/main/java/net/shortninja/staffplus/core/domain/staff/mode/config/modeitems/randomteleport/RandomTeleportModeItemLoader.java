package net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.randomteleport;

import be.garagepoort.mcioc.IocBean;

import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.domain.staff.mode.config.ModeItemLoader;
import org.bukkit.configuration.file.FileConfiguration;

@IocBean
public class RandomTeleportModeItemLoader extends ModeItemLoader<RandomTeleportModeConfiguration> {
    public RandomTeleportModeItemLoader(IProtocolService protocolService) {
        super(protocolService);
    }

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
