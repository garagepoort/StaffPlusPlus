package net.shortninja.staffplus.common.config;

public class GuiItemConfig {
    private final boolean enabled;
    private final String title;
    private final String itemName;
    private final String itemLore;

    public GuiItemConfig(boolean enabled, String title, String itemName, String itemLore) {
        this.enabled = enabled;
        this.title = title;
        this.itemName = itemName;
        this.itemLore = itemLore;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getTitle() {
        return title;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemLore() {
        return itemLore;
    }
}
