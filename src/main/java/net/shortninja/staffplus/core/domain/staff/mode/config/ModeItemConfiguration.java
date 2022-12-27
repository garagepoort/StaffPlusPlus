package net.shortninja.staffplus.core.domain.staff.mode.config;

import org.bukkit.inventory.ItemStack;

public class ModeItemConfiguration {
    private String identifier;
    private boolean enabled;
    private ItemStack item;
    private boolean movable;

    public ModeItemConfiguration(String identifier) {
        this.identifier = identifier;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setMovable(boolean movable) {
        this.movable = movable;
    }

    public boolean isMovable() {
        return movable;
    }
}
