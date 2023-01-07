package net.shortninja.staffplus.core.common.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;

@IocBean
public class GuiItemConfig implements IGuiItemConfig{
    @ConfigProperty("modules.gui-module.investigation-gui")
    private boolean enabled;
    @ConfigProperty("modules.gui-module.investigation-title")
    private String title;

    public boolean isEnabled() {
        return enabled;
    }

    public String getTitle() {
        return title;
    }

}
