package net.shortninja.staffplus.staff.mode.item;

import be.garagepoort.staffplusplus.craftbukkit.common.IProtocol;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.staff.mode.config.ModeItemConfiguration;
import org.bukkit.inventory.ItemStack;

public class ModeItem {
    private IProtocol versionProtocol = StaffPlus.get().versionProtocol;
    private String identifier;
    private ItemStack item;
    private int slot;
    private boolean isEnabled;

    public ModeItem(String identifier, ModeItemConfiguration modeItemConfiguration) {
        this.identifier = identifier;
        this.item = versionProtocol.addNbtString(modeItemConfiguration.getItem(), identifier); // Make this item uniquely identifiable by adding an NBT tag.
        this.slot = modeItemConfiguration.getSlot();
        this.isEnabled = modeItemConfiguration.isEnabled();
    }

    public String getIdentifier() {
        return identifier;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public int getSlot() {
        return slot;
    }

    public boolean isEnabled() {
        return isEnabled;
    }
}