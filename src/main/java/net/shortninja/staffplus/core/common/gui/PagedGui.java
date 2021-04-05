package net.shortninja.staffplus.core.common.gui;


import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Supplier;

public abstract class PagedGui extends AbstractGui {
    private static final int SIZE = 54;
    private SppPlayer target;
    private final Player player;
    private final int currentPage;

    protected PagedGui(Player player, String title, int currentPage) {
        super(SIZE, title);
        this.player = player;
        this.currentPage = currentPage;
    }

    protected PagedGui(Player player, String title, int currentPage, Supplier<AbstractGui> backGuiSupplier) {
        super(SIZE, title, backGuiSupplier);
        this.player = player;
        this.currentPage = currentPage;
    }

    protected PagedGui(Player player, SppPlayer target, String title, int currentPage) {
        super(SIZE, title);
        this.player = player;
        this.target = target;
        this.currentPage = currentPage;
    }

    protected PagedGui(Player player, SppPlayer target, String title, int currentPage, Supplier<AbstractGui> backGuiSupplier) {
        super(SIZE, title, backGuiSupplier);
        this.player = player;
        this.target = target;
        this.currentPage = currentPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    @Override
    public void buildGui() {
        int offset = currentPage * 45;
        int count = 0;
        for (ItemStack report : getItems(player, target, offset, 45)) {
            setItem(count, report, getAction());
            count++;
        }

        IAction nextPageAction = new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot, ClickType clickType) {
                getNextUi(player, target, getTitle(), currentPage + 1).show(player);
            }

            @Override
            public boolean shouldClose(Player player) {
                return false;
            }
        };

        IAction previousPageAction = new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot, ClickType clickType) {
                getNextUi(player, target, getTitle(), currentPage - 1).show(player);
            }

            @Override
            public boolean shouldClose(Player player) {
                return false;
            }
        };

        addNextPageItem(nextPageAction, 53);
        addNextPageItem(nextPageAction, 52);
        addNextPageItem(nextPageAction, 51);

        if (currentPage != 0) {
            addPreviousPageItem(previousPageAction, 45);
            addPreviousPageItem(previousPageAction, 46);
            addPreviousPageItem(previousPageAction, 47);
        }
    }


    protected abstract AbstractGui getNextUi(Player player, SppPlayer target, String title, int page);

    public abstract IAction getAction();

    public abstract List<ItemStack> getItems(Player player, SppPlayer target, int offset, int amount);

    private void addNextPageItem(IAction action, int slot) {
        ItemStack item = Items.editor(Items.createGreenColoredGlass("Next Page", ""))
            .setAmount(1)
            .build();
        setItem(slot, item, action);
    }

    private void addPreviousPageItem(IAction action, int slot) {
        ItemStack item = Items.editor(Items.createRedColoredGlass("Previous Page", ""))
            .setAmount(1)
            .build();
        setItem(slot, item, action);
    }

    public SppPlayer getTarget() {
        return target;
    }
}