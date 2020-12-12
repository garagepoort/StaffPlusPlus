package net.shortninja.staffplus.common;

import net.shortninja.staffplus.unordered.IAction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PassThroughClickAction implements IAction {
    @Override
    public void click(Player player, ItemStack item, int slot) {
        //dummy
    }

    @Override
    public boolean shouldClose(Player player) {
        return false;
    }
}
