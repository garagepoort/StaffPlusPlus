package net.shortninja.staffplus.staff.examine.items;

import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.chests.ChestGUI;
import net.shortninja.staffplus.staff.chests.ChestGuiType;
import net.shortninja.staffplus.staff.examine.ExamineGui;
import net.shortninja.staffplus.staff.examine.ExamineGuiItemProvider;
import net.shortninja.staffplus.staff.mode.config.modeitems.examine.ExamineModeConfiguration;
import net.shortninja.staffplus.common.IAction;
import net.shortninja.staffplus.util.PermissionHandler;
import net.shortninja.staffplus.util.factory.InventoryFactory;
import net.shortninja.staffplus.common.Items;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryExamineGuiProvider implements ExamineGuiItemProvider {

    private final Messages messages;
    private final ExamineModeConfiguration examineModeConfiguration;
    private final Options options;
    private PermissionHandler permissionHandler;

    public InventoryExamineGuiProvider(Messages messages, Options options, PermissionHandler permissionHandler) {
        this.messages = messages;
        this.options = options;
        this.permissionHandler = permissionHandler;
        examineModeConfiguration = this.options.modeConfiguration.getExamineModeConfiguration();
    }

    @Override
    public ItemStack getItem(SppPlayer player) {
        return locationItem();
    }

    @Override
    public IAction getClickAction(ExamineGui examineGui, Player staff, SppPlayer target) {
        return new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot) {
                ChestGUI chestGUI;
                if (target.isOnline()) {
                    chestGUI = new ChestGUI(target, target.getPlayer().getPlayer().getInventory(), 54, ChestGuiType.PLAYER_INVENTORY_EXAMINE, permissionHandler.has(player, options.examineConfiguration.getPermissionExamineInventoryInteraction()));
                } else {
                    chestGUI = new ChestGUI(target, InventoryFactory.loadInventoryOffline(staff, target), 54, ChestGuiType.PLAYER_INVENTORY_EXAMINE, permissionHandler.has(player, options.examineConfiguration.getPermissionExamineInventoryInteractionOffline()));
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
        for (int i = 41; i <= 53; i++) {
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
