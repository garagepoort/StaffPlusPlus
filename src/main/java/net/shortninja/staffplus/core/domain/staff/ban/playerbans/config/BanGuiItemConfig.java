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
    @ConfigProperty("staffmode-modules:modules.gui-module.ban-name")
    private String itemName;
    @ConfigProperty("staffmode-modules:modules.gui-module.ban-lore")
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
