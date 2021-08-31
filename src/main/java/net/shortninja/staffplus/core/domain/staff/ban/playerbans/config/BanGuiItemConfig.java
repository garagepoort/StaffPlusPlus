package net.shortninja.staffplus.core.domain.staff.ban.playerbans.config;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.common.gui.IGuiItemConfig;

@IocBean
public class BanGuiItemConfig implements IGuiItemConfig {
    @ConfigProperty("staffmode-modules:modules.gui-module.ban-gui")
    private boolean enabled;
    @ConfigProperty("staffmode-modules:modules.gui-module.ban-title")
    private String title;

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getTitle() {
        return title;
    }

}
