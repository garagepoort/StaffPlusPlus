package net.shortninja.staffplus.core.mode.config.modeitems.examine;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperties;
import net.shortninja.staffplus.core.mode.config.ModeItemConfiguration;

@IocBean
@ConfigProperties("staffmode-modules:modules.examine-module")
public class ExamineModeConfiguration extends ModeItemConfiguration {
    @Override
    public String getIdentifier() {
        return "examine-module";
    }
}
