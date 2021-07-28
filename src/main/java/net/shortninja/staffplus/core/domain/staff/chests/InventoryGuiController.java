package net.shortninja.staffplus.core.domain.staff.chests;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiActionReturnType;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.GuiParam;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.Items;
import net.shortninja.staffplus.core.common.exceptions.PlayerNotFoundException;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.utils.InventoryFactory;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.entity.Player;

@IocBean
@GuiController
public class InventoryGuiController {

    private final PlayerManager playerManager;
    private final InventoryFactory inventoryFactory;
    private final PermissionHandler permissionHandler;
    private final Options options;

    public InventoryGuiController(PlayerManager playerManager, InventoryFactory inventoryFactory, PermissionHandler permissionHandler, Options options) {
        this.playerManager = playerManager;
        this.inventoryFactory = inventoryFactory;
        this.permissionHandler = permissionHandler;
        this.options = options;
    }

    @GuiAction("manage-inventory/open")
    public GuiActionReturnType openInventory(Player staff, @GuiParam("targetPlayerName") String targetPlayerName) {
        SppPlayer target = playerManager.getOnOrOfflinePlayer(targetPlayerName).orElseThrow(() -> new PlayerNotFoundException(targetPlayerName));
        ChestGUI chestGUI;
        if (target.isOnline()) {
            chestGUI = new ChestGUI(target, target.getPlayer().getPlayer().getInventory(), 45, ChestGuiType.PLAYER_INVENTORY_EXAMINE, permissionHandler.has(staff, options.examineConfiguration.getPermissionExamineInventoryInteraction()));
        } else {
            chestGUI = new ChestGUI(target, inventoryFactory.loadInventoryOffline(staff, target), 45, ChestGuiType.PLAYER_INVENTORY_EXAMINE, permissionHandler.has(staff, options.examineConfiguration.getPermissionExamineInventoryInteractionOffline()));
        }
        fillEmptyPlaces(chestGUI);
        chestGUI.show(staff);
        return GuiActionReturnType.KEEP_OPEN;
    }

    private void fillEmptyPlaces(ChestGUI chestGUI) {
        for (int i = 41; i <= 44; i++) {
            chestGUI.setItem(i, Items.createRedColoredGlass("Not a player slot", ""), null);
        }
    }
}
