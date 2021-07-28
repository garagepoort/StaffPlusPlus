package net.shortninja.staffplus.core.domain.staff.examine.gui;

import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ExamineGuiItemProvider {

    ItemStack getItem(SppPlayer player);

    String getClickAction(Player staff, SppPlayer targetPlayer, String backAction);

    boolean enabled(Player staff, SppPlayer player);

    int getSlot();
}
