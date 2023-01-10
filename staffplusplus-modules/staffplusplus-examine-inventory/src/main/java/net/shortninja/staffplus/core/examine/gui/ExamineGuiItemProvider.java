package net.shortninja.staffplus.core.examine.gui;

import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ExamineGuiItemProvider {

    ItemStack getItem(Player player1, SppPlayer player);

    String getClickAction(Player staff, SppPlayer targetPlayer);

    boolean enabled(Player staff, SppPlayer player);

    int getSlot();
}
