package net.shortninja.staffplus.staff.mode.config;

import org.bukkit.inventory.ItemStack;

public class ModeItemConfiguration {
    private boolean enabled;
    private int slot;
    private ItemStack item;

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
}
