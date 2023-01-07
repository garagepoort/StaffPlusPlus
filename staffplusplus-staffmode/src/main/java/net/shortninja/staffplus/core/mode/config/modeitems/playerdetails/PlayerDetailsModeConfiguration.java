package net.shortninja.staffplus.core.mode.config.modeitems.playerdetails;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperties;
import net.shortninja.staffplus.core.mode.config.ModeItemConfiguration;

@IocBean
@ConfigProperties("staffmode-modules:modules.player-details-module")
public class PlayerDetailsModeConfiguration extends ModeItemConfiguration {
    @Override
    public String getIdentifier() {
        return "player-details-module";
    }
}
