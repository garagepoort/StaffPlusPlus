package net.shortninja.staffplus.domain.staff.mode.config;

import be.garagepoort.staffplusplus.craftbukkit.common.IProtocol;
import net.shortninja.staffplus.StaffPlus;
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
        IProtocol versionProtocol = StaffPlus.get().versionProtocol;
        this.item = versionProtocol.addNbtString(item, identifier);
    }

    public String getIdentifier() {
        return identifier;
    }
}
