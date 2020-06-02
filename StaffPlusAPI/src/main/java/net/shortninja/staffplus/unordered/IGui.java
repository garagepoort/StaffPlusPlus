package net.shortninja.staffplus.unordered;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface IGui {

<<<<<<< HEAD
	String getTitle();

	Inventory getInventory();

	IAction getAction(int slot);

	void setItem(int slot, ItemStack item, IAction action);

	void setGlass(IUser user);
=======
    String getTitle();

    Inventory getInventory();

    IAction getAction(int slot);

    void setItem(int slot, ItemStack item, IAction action);

    void setGlass(IUser user);
>>>>>>> b2eb803718fc6d2d09f3ef627210b17920278857
}
