package net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.counter;

import net.shortninja.staffplus.core.domain.staff.mode.config.ModeItemConfiguration;

public class CounterModeConfiguration extends ModeItemConfiguration {

    private boolean modeCounterShowStaffMode;
    private String title;

    public CounterModeConfiguration(String identifier, boolean modeCounterShowStaffMode, String title) {
        super(identifier);
        this.modeCounterShowStaffMode = modeCounterShowStaffMode;
        this.title = title;
    }

    public boolean isModeCounterShowStaffMode() {
        return modeCounterShowStaffMode;
    }

    public String getTitle() {
        return title;
    }
}
