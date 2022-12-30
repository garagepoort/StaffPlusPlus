package net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperties;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.domain.staff.mode.config.ModeItemConfiguration;

@IocBean
@ConfigProperties("staffmode-modules:modules.gui-module")
public class GuiModeConfiguration extends ModeItemConfiguration {
    @ConfigProperty("xray-level")
    public int modeGuiMinerLevel;

    @Override
    public String getIdentifier() {
        return "gui-module";
    }
}
