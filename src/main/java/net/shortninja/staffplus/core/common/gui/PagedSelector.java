package net.shortninja.staffplus.core.common.gui;

import net.shortninja.staffplus.core.common.gui.selector.OnSelect;
import net.shortninja.staffplus.core.common.gui.selector.PageItemsRetriever;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Supplier;

public class PagedSelector extends PagedGui {
    private final OnSelect onSelect;
    private final PageItemsRetriever pageItemsRetriever;

    public PagedSelector(Player player, String title, int currentPage, OnSelect onSelect, PageItemsRetriever pageItemsRetriever) {
        super(player, title, currentPage);
        this.onSelect = onSelect;
        this.pageItemsRetriever = pageItemsRetriever;
    }

    public PagedSelector(Player player, String title, int currentPage, Supplier<AbstractGui> backGuiSupplier, OnSelect onSelect, PageItemsRetriever pageItemsRetriever) {
        super(player, title, currentPage, backGuiSupplier);
        this.onSelect = onSelect;
        this.pageItemsRetriever = pageItemsRetriever;
    }

    @Override
    protected AbstractGui getNextUi(Player player, SppPlayer target, String title, int page) {
        return new PagedSelector(player, title, page, this.previousGuiSupplier, onSelect, pageItemsRetriever);
    }

    @Override
    public IAction getAction() {
        return new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot, ClickType clickType) {
                onSelect.onSelect(item);
            }

            @Override
            public boolean shouldClose(Player player) {
                return true;
            }
        };
    }

    @Override
    public List<ItemStack> getItems(Player player, SppPlayer target, int offset, int amount) {
        return pageItemsRetriever.getItems(player, target, offset, amount);
    }
}
