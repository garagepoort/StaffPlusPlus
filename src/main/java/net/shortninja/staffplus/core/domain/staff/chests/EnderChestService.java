package net.shortninja.staffplus.core.domain.staff.chests;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.GuiActionService;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.utils.InventoryFactory;
import net.shortninja.staffplus.core.domain.staff.chests.config.EnderchestsConfiguration;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@IocBean
public class EnderChestService {

    private final PermissionHandler permissionHandler;
    private final InventoryFactory inventoryFactory;
    private final EnderchestsConfiguration enderchestsConfiguration;
    private final GuiActionService guiActionService;

    public EnderChestService(PermissionHandler permissionHandler, InventoryFactory inventoryFactory, EnderchestsConfiguration enderchestsConfiguration, GuiActionService guiActionService) {
        this.permissionHandler = permissionHandler;
        this.inventoryFactory = inventoryFactory;
        this.enderchestsConfiguration = enderchestsConfiguration;
        this.guiActionService = guiActionService;
    }

    public void openEnderChest(Player staff, SppPlayer target, String backAction) {
        int containerSize = StringUtils.isNotBlank(backAction) ? 36 : 27;
        if (target.isOnline()) {
            if (!permissionHandler.has(staff, enderchestsConfiguration.permissionViewOnline)) {
                throw new BusinessException("&CYou are not allowed to view the enderchest of an online player");
            }
            ChestGUI chestGUI = new ChestGUI(target,
                target.getPlayer().getEnderChest(),
                containerSize,
                ChestGuiType.ENDER_CHEST_EXAMINE,
                permissionHandler.has(staff, enderchestsConfiguration.permissionInteract));
            setBackRow(chestGUI, staff, backAction);
            chestGUI.show(staff);
        } else {
            if (!permissionHandler.has(staff, enderchestsConfiguration.permissionViewOffline)) {
                throw new BusinessException("&CYou are not allowed to view the enderchest of an offline player");
            }

            Inventory offlineEnderchest = inventoryFactory.loadEnderchestOffline(staff, target);
            ChestGUI chestGUI = new ChestGUI(target, offlineEnderchest,
                containerSize,
                ChestGuiType.ENDER_CHEST_EXAMINE,
                permissionHandler.has(staff, enderchestsConfiguration.permissionInteract));
            setBackRow(chestGUI, staff, backAction);
            chestGUI.show(staff);
        }
    }

    private void setBackRow(ChestGUI chestGUI, Player staff, String backAction) {
        if (StringUtils.isNotBlank(backAction)) {
            fillEmptyPlaces(chestGUI);
            chestGUI.setItem(31, Items.createDoor("Back", "Go back"), new IAction() {
                @Override
                public void click(Player player, ItemStack item, int slot, ClickType clickType) {
                    guiActionService.executeAction(staff, backAction);
                }

                @Override
                public boolean shouldClose(Player player) {
                    return false;
                }
            });
        }
    }

    private void fillEmptyPlaces(ChestGUI chestGUI) {
        for (int i = 27; i <= 30; i++) {
            chestGUI.setItem(i, Items.createRedColoredGlass("Not a player slot", ""), null);
        }
        for (int i = 32; i <= 35; i++) {
            chestGUI.setItem(i, Items.createRedColoredGlass("Not a player slot", ""), null);
        }
    }
}
