package net.shortninja.staffplus.core.domain.staff.examine.gui;

import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ExamineGuiItemProvider {

    ItemStack getItem(SppPlayer player);

    IAction getClickAction(ExamineGui examineGui, Player staff, SppPlayer targetPlayer);

    boolean enabled(Player staff, SppPlayer player);

    int getSlot();
}
