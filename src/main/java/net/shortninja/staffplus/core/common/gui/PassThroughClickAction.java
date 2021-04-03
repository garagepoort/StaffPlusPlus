package net.shortninja.staffplus.core.common.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class PassThroughClickAction implements IAction {
    @Override
    public void click(Player player, ItemStack item, int slot, ClickType clickType) {
        //dummy
    }

    @Override
    public boolean shouldClose(Player player) {
        return false;
    }
}
