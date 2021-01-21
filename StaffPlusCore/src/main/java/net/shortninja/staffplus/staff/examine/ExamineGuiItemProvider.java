package net.shortninja.staffplus.staff.examine;

import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.unordered.IAction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ExamineGuiItemProvider {

    ItemStack getItem(SppPlayer player);

    IAction getClickAction(ExamineGui examineGui, Player staff, SppPlayer targetPlayer);

    boolean enabled(Player staff, SppPlayer player);

    int getSlot();
}
