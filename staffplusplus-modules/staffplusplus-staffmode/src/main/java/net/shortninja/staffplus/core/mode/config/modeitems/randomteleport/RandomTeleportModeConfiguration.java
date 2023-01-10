package net.shortninja.staffplus.core.mode.config.modeitems.randomteleport;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperties;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.mode.config.ModeItemConfiguration;

@IocBean
@ConfigProperties("staffmode-modules:modules.random-teleport-module")
public class RandomTeleportModeConfiguration extends ModeItemConfiguration {

    @ConfigProperty("random")
    private boolean random;

    public boolean isRandom() {
        return random;
    }

    @Override
    public String getIdentifier() {
        return "random-teleport-module";
    }
}
