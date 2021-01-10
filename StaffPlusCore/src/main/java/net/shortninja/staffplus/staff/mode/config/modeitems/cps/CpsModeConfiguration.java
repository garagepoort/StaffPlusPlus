package net.shortninja.staffplus.staff.mode.config.modeitems.cps;

import net.shortninja.staffplus.staff.mode.config.ModeItemConfiguration;

public class CpsModeConfiguration extends ModeItemConfiguration {
    private long modeCpsTime;
    private int modeCpsMax;

    public CpsModeConfiguration(long modeCpsTime, int modeCpsMax) {
        this.modeCpsTime = modeCpsTime;
        this.modeCpsMax = modeCpsMax;
    }

    public long getModeCpsTime() {
        return modeCpsTime;
    }

    public int getModeCpsMax() {
        return modeCpsMax;
    }
}
