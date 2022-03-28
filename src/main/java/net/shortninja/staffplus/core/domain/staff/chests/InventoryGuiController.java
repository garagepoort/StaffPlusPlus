package net.shortninja.staffplus.core.domain.staff.chests;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiActionReturnType;
import be.garagepoort.mcioc.gui.GuiActionService;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.GuiParam;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.exceptions.PlayerNotFoundException;
import net.shortninja.staffplus.core.common.gui.IAction;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.utils.InventoryFactory;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.examine.config.ExamineConfiguration;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@IocBean
@GuiController
public class InventoryGuiController {

    private final PlayerManager playerManager;
    private final InventoryFactory inventoryFactory;
    private final PermissionHandler permissionHandler;
    private final GuiActionService guiActionService;
    private final ExamineConfiguration examineConfiguration;

    public InventoryGuiController(PlayerManager playerManager,
                                  InventoryFactory inventoryFactory,
                                  PermissionHandler permissionHandler,
                                  GuiActionService guiActionService,
                                  ExamineConfiguration examineConfiguration) {
        this.playerManager = playerManager;
        this.inventoryFactory = inventoryFactory;
        this.permissionHandler = permissionHandler;
        this.guiActionService = guiActionService;
        this.examineConfiguration = examineConfiguration;
    }

    @GuiAction("manage-inventory/open")
    public GuiActionReturnType openInventory(Player staff,
                                             @GuiParam("targetPlayerName") String targetPlayerName) {
        SppPlayer target = playerManager.getOnOrOfflinePlayer(targetPlayerName).orElseThrow(() -> new PlayerNotFoundException(targetPlayerName));
        ChestGUI chestGUI;
        if (target.isOnline()) {
            chestGUI = new ChestGUI(target,
                target.getPlayer().getPlayer().getInventory(),
                45,
                ChestGuiType.PLAYER_INVENTORY_EXAMINE,
                permissionHandler.has(staff, examineConfiguration.getPermissionExamineInventoryInteraction()));
        } else {
            chestGUI = new ChestGUI(target,
                inventoryFactory.loadInventoryOffline(staff,
                    target),
                45,
                ChestGuiType.PLAYER_INVENTORY_EXAMINE,
                permissionHandler.has(staff, examineConfiguration.getPermissionExamineInventoryInteractionOffline()));
        }
        fillEmptyPlaces(chestGUI);
        chestGUI.setItem(44, Items.createDoor("Back", "Go back"), new IAction() {
            @Override
            public void click(Player player, ItemStack item, int slot, ClickType clickType) {
                guiActionService.executeAction(staff, "$$back");
            }

            @Override
            public boolean shouldClose(Player player) {
                return false;
            }
        });
        chestGUI.show(staff);
        return GuiActionReturnType.KEEP_OPEN;
    }

    private void fillEmptyPlaces(ChestGUI chestGUI) {
        for (int i = 41; i <= 43; i++) {
            chestGUI.setItem(i, Items.createRedColoredGlass("Not a player slot", ""), null);
        }
    }
}
