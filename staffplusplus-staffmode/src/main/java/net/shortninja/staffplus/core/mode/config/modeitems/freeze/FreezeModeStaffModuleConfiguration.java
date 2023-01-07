package net.shortninja.staffplus.core.mode.config.modeitems.freeze;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperties;
import net.shortninja.staffplus.core.mode.config.ModeItemConfiguration;

@IocBean
@ConfigProperties("staffmode-modules:modules.freeze-module")
public class FreezeModeStaffModuleConfiguration extends ModeItemConfiguration {
    @Override
    public String getIdentifier() {
        return "freeze-module";
    }
}
