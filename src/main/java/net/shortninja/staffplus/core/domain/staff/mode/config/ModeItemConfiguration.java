package net.shortninja.staffplus.core.domain.staff.mode.config;

import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.configuration.ConfigTransformer;
import org.bukkit.inventory.ItemStack;

public class ModeItemConfiguration {
    @ConfigProperty("name")
    protected String identifier;
    @ConfigProperty("enabled")
    protected boolean enabled;
    @ConfigProperty("item")
    @ConfigTransformer(ModeItemConfigTransformer.class)
    protected ItemStack item;
    @ConfigProperty("movable")
    protected boolean movable = true;

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
