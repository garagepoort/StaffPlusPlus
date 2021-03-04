package net.shortninja.staffplus.common;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IAction {


	void click(Player player, ItemStack item, int slot);

	boolean shouldClose(Player player);

}