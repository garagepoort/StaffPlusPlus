package net.shortninja.staffplus.core.mode.config.modeitems.counter;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperties;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.mode.config.ModeItemConfiguration;

@IocBean
@ConfigProperties("staffmode-modules:modules.counter-module")
public class CounterModeConfiguration extends ModeItemConfiguration {

    @ConfigProperty("show-staff-mode")
    private boolean modeCounterShowStaffMode;
    @ConfigProperty("title")
    private String title;

    public boolean isModeCounterShowStaffMode() {
        return modeCounterShowStaffMode;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String getIdentifier() {
        return "counter-module";
    }
}
