package net.shortninja.staffplus.core.mode.config.modeitems.compass;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperties;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.mode.config.ModeItemConfiguration;

@IocBean
@ConfigProperties("staffmode-modules:modules.compass-module")
public class CompassModeConfiguration extends ModeItemConfiguration {

    @ConfigProperty("velocity")
    private int velocity;

    public int getVelocity() {
        return velocity;
    }

    @Override
    public String getIdentifier() {
        return "compass-module";
    }
}
