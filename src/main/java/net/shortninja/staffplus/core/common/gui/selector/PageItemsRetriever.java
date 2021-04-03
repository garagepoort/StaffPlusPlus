package net.shortninja.staffplus.core.common.gui.selector;

import net.shortninja.staffplus.core.domain.player.SppPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface PageItemsRetriever {
    List<ItemStack> getItems(Player player, SppPlayer target, int offset, int amount);
}
