package net.shortninja.staffplus.core.domain.staff.mute.config;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.common.gui.IGuiItemConfig;

@IocBean
public class MuteGuiItemConfig implements IGuiItemConfig {
    @ConfigProperty("staffmode-modules:modules.gui-module.mute-gui")
    private boolean enabled;
    @ConfigProperty("staffmode-modules:modules.gui-module.mute-title")
    private String title;
    @ConfigProperty("staffmode-modules:modules.gui-module.mute-name")
    private String itemName;
    @ConfigProperty("staffmode-modules:modules.gui-module.mute-lore")
    private String itemLore;

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getItemName() {
        return itemName;
    }

    @Override
    public String getItemLore() {
        return itemLore;
    }
}
