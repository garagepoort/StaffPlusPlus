package net.shortninja.staffplus.core.common.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface IGui {
    
    String getTitle();

    Inventory getInventory();

    IAction getAction(int slot);

    void setItem(int slot, ItemStack item, IAction action);
}
