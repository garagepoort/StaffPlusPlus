package net.shortninja.staffplus.unordered;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IAction {

<<<<<<< HEAD
	void click(Player player, ItemStack item, int slot);

	boolean shouldClose();

	void execute(Player player, String input);
=======
    void click(Player player, ItemStack item, int slot);

    boolean shouldClose();

    void execute(Player player, String input);
>>>>>>> b2eb803718fc6d2d09f3ef627210b17920278857
}