package net.shortninja.staffplus.core.mode.config.modeitems.follow;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperties;
import net.shortninja.staffplus.core.mode.config.ModeItemConfiguration;

@IocBean
@ConfigProperties("staffmode-modules:modules.follow-module")
public class FollowModeConfiguration extends ModeItemConfiguration {

    @Override
    public String getIdentifier() {
        return "follow-module";
    }
}
