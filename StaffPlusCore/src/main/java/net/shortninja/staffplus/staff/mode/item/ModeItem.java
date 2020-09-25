package net.shortninja.staffplus.staff.mode.item;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.server.compatibility.IProtocol;
import org.bukkit.inventory.ItemStack;

public class ModeItem {
    private IProtocol versionProtocol = StaffPlus.get().versionProtocol;
    private String identifier;
    private ItemStack item;
    private int slot;
    private boolean isEnabled;

    public ModeItem(String identifier, ItemStack item, int slot, boolean isEnabled) {
        this.identifier = identifier;
        this.item = versionProtocol.addNbtString(item, identifier); // Make this item uniquely identifiable by adding an NBT tag.
        this.slot = slot;
        this.isEnabled = isEnabled;
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