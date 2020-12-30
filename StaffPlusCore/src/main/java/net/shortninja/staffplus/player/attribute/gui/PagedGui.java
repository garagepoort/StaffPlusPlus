package net.shortninja.staffplus.player.attribute.gui;


import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.common.cmd.CommandUtil;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.session.SessionManager;
import net.shortninja.staffplus.unordered.IAction;
import net.shortninja.staffplus.util.lib.hex.Items;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Supplier;

public abstract class PagedGui extends AbstractGui {
    private static final int SIZE = 54;
    private SppPlayer target;
    private final int currentPage;
    private SessionManager sessionManager = IocContainer.getSessionManager();

    public PagedGui(Player player, String title, int currentPage) {
        super(SIZE, title);
        this.currentPage = currentPage;
        buildUi(player, title, currentPage);
    }

    public PagedGui(Player player, String title, int currentPage, Supplier<AbstractGui> backGuiSupplier) {
        super(SIZE, title, backGuiSupplier);
        this.currentPage = currentPage;
        buildUi(player, title, currentPage);
    }
    public PagedGui(Player player, SppPlayer target, String title, int currentPage) {
        super(SIZE, title);
        this.target = target;
        this.currentPage = currentPage;
        buildUi(player, title, currentPage);
    }

    public PagedGui(Player player, SppPlayer target,  String title, int currentPage, Supplier<AbstractGui> backGuiSupplier) {
        super(SIZE, title, backGuiSupplier);
        this.target = target;
        this.currentPage = currentPage;
        buildUi(player, title, currentPage);
    }

    public int getCurrentPage() {
        return currentPage;
    }

    private void buildUi(Player player, String title, int currentPage) {
        int offset = currentPage * 45;
        int count = 0;
        for (ItemStack report : getItems(player, target, offset, 45)) {
            setItem(count, report, getAction());
            count++;
        }

        IAction nextPageAction = new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                CommandUtil.playerAction(player, () -> {
                    getNextUi(player, target, title, currentPage + 1);
                });
            }

            @Override
            public boolean shouldClose(Player player) {
                return false;
            }
        };

        IAction previousPageAction = new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                CommandUtil.playerAction(player, () -> {
                    getNextUi(player, target, title, currentPage - 1);
                });
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


        player.closeInventory();
        player.openInventory(getInventory());
        sessionManager.get(player.getUniqueId()).setCurrentGui(this);
    }

    protected abstract void getNextUi(Player player, SppPlayer target, String title, int page);

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