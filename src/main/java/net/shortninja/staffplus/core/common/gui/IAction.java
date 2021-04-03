package net.shortninja.staffplus.core.common.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public interface IAction {


	void click(Player player, ItemStack item, int slot, ClickType clickType);

	boolean shouldClose(Player player);

}