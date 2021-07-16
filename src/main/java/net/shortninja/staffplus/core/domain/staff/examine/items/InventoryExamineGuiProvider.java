package net.shortninja.staffplus.core.domain.staff.examine.items;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.utils.InventoryFactory;
import net.shortninja.staffplus.core.domain.staff.chests.ChestGUI;
import net.shortninja.staffplus.core.domain.staff.chests.ChestGuiType;
import net.shortninja.staffplus.core.domain.staff.examine.gui.ExamineGui;
import net.shortninja.staffplus.core.domain.staff.examine.gui.ExamineGuiItemProvider;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@IocBean
@IocMultiProvider(ExamineGuiItemProvider.class)
public class InventoryExamineGuiProvider implements ExamineGuiItemProvider {

    private final Options options;
    private PermissionHandler permissionHandler;
    private InventoryFactory inventoryFactory;

    public InventoryExamineGuiProvider(Options options, PermissionHandler permissionHandler, InventoryFactory inventoryFactory) {
        this.options = options;
        this.permissionHandler = permissionHandler;
        this.inventoryFactory = inventoryFactory;
    }

    @Override
    public ItemStack getItem(SppPlayer player) {
        return locationItem();
    }

    @Override
    public IAction getClickAction(ExamineGui examineGui, Player staff, SppPlayer target) {
        return new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot, ClickType clickType) {
                ChestGUI chestGUI;
                if (target.isOnline()) {
                    chestGUI = new ChestGUI(target, target.getPlayer().getPlayer().getInventory(), 45, ChestGuiType.PLAYER_INVENTORY_EXAMINE, permissionHandler.has(player, options.examineConfiguration.getPermissionExamineInventoryInteraction()));
                } else {
                    chestGUI = new ChestGUI(target, inventoryFactory.loadInventoryOffline(staff, target), 45, ChestGuiType.PLAYER_INVENTORY_EXAMINE, permissionHandler.has(player, options.examineConfiguration.getPermissionExamineInventoryInteractionOffline()));
                }
                fillEmptyPlaces(chestGUI);
                chestGUI.show(staff);
            }

            @Override
            public boolean shouldClose(Player player) {
                return false;
            }
        };
    }

    private void fillEmptyPlaces(ChestGUI chestGUI) {
        for (int i = 41; i <= 44; i++) {
            chestGUI.setItem(i, Items.createRedColoredGlass("Not a player slot", ""), null);
        }
    }

    @Override
    public boolean enabled(Player staff, SppPlayer player) {
        if (player.isOnline()) {
            return permissionHandler.has(staff, options.examineConfiguration.getPermissionExamineViewInventory());
        } else {
            return permissionHandler.has(staff, options.examineConfiguration.getPermissionExamineViewInventoryOffline());
        }
    }

    @Override
    public int getSlot() {
        return 20;
    }

    private ItemStack locationItem() {
        ItemStack item = Items.builder()
            .setMaterial(Material.CHEST).setAmount(1)
            .setName("&bPlayer's inventory")
            .addLore("View player's inventory")
            .build();

        return item;
    }
}
