package net.shortninja.staffplus.core.common.gui;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.application.session.SessionManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public abstract class AbstractGui implements IGui {
    protected final Messages messages = StaffPlus.get().getIocContainer().get(Messages.class);
    protected final SessionManagerImpl sessionManager = StaffPlus.get().getIocContainer().get(SessionManagerImpl.class);
    protected final Options options = StaffPlus.get().getIocContainer().get(Options.class);

    private String title;
    protected Supplier<AbstractGui> previousGuiSupplier;
    private Inventory inventory;
    private Map<Integer, IAction> actions = new HashMap<>();

    public AbstractGui(String title, InventoryType inventoryType) {
        this.title = title;
        inventory = Bukkit.createInventory(null, inventoryType);
    }

    public AbstractGui(int size, String title) {
        this.title = title;
        inventory = Bukkit.createInventory(null, size, messages.colorize(title));
    }

    public AbstractGui(int size, String title, Supplier<AbstractGui> previousGuiSupplier) {
        this.title = title;
        inventory = Bukkit.createInventory(null, size, messages.colorize(title));
        this.previousGuiSupplier = previousGuiSupplier;
    }

    public abstract void buildGui();

    public void show(Player player) {
        buildGui();
        if (previousGuiSupplier != null) {
            ItemStack item = Items.editor(Items.createDoor("Back", "Go back"))
                .setAmount(1)
                .build();
            setItem(getBackButtonSlot(), item, new IAction() {
                @Override
                public void click(Player player, ItemStack item, int slot, ClickType clickType) {
                    previousGuiSupplier.get().show(player);
                }

                @Override
                public boolean shouldClose(Player player) {
                    return false;
                }
            });
        }

        player.closeInventory();
        player.openInventory(getInventory());
        sessionManager.get(player.getUniqueId()).setCurrentGui(this);
    }

    public Supplier<AbstractGui> getPreviousGuiSupplier() {
        return previousGuiSupplier;
    }

    public String getTitle() {
        return title;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public IAction getAction(int slot) {
        return actions.get(slot);
    }

    public void setItem(int slot, ItemStack item, IAction action) {
        inventory.setItem(slot, item);

        if (action != null) {
            actions.put(slot, action);
        }
    }

    protected int getBackButtonSlot() {
        return 49;
    }

    public void setGlass(PlayerSession user) {
        ItemStack item = glassItem(user.getGlassColor());

        for (int i = 0; i < 3; i++) {
            int slot = 9 * i;

            setItem(slot, item, null);
            setItem(slot + 8, item, null);
        }
    }

    private ItemStack glassItem(Material data) {
        return Items.builder()
            .setMaterial(data)
            .setName("&bColor #" + data)
            .addLore("&7Color changing not supported in this version!")
            .build();
    }
}