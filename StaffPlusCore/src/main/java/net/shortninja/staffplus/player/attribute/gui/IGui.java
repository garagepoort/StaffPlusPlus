package net.shortninja.staffplus.player.attribute.gui;

import net.shortninja.staffplus.player.PlayerSession;
import net.shortninja.staffplus.unordered.IAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface IGui {


    String getTitle();

    Inventory getInventory();

    IAction getAction(int slot);

    void setItem(int slot, ItemStack item, IAction action);

    void setGlass(PlayerSession user);
}
