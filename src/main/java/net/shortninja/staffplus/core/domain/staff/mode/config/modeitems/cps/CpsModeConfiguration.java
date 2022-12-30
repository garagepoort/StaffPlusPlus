package net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.cps;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperties;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.domain.staff.mode.config.ModeItemConfiguration;

@IocBean
@ConfigProperties("staffmode-modules:modules.cps-module")
public class CpsModeConfiguration extends ModeItemConfiguration {
    @ConfigProperty("max")
    private double modeCpsMax;
    private long modeCpsTime;

    @ConfigProperty("time")
    public void setModeCpsTime(long modeCpsTime) {
        this.modeCpsTime = modeCpsTime * 20;
    }

    public long getModeCpsTime() {
        return modeCpsTime;
    }

    public double getModeCpsMax() {
        return modeCpsMax;
    }

    @Override
    public String getIdentifier() {
        return "cps-module";
    }

}
