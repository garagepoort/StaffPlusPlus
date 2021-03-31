package net.shortninja.staffplus.core.domain.staff.mode.config;

import org.bukkit.inventory.ItemStack;

public class ModeItemConfiguration {
    private String identifier;
    private boolean enabled;
    private int slot;
    private ItemStack item;

    public ModeItemConfiguration(String identifier) {
        this.identifier = identifier;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public String getIdentifier() {
        return identifier;
    }
}
