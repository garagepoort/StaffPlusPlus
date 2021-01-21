package net.shortninja.staffplus.staff.chests;

import net.shortninja.staffplus.common.exceptions.BusinessException;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.util.Permission;
import net.shortninja.staffplus.util.factory.InventoryFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class EnderChestService {

    private final Permission permissionHandler;
    private final Options options;

    public EnderChestService(Permission permissionHandler, Options options) {
        this.permissionHandler = permissionHandler;
        this.options = options;
    }

    public void openEnderChest(Player staff, SppPlayer target) {
        if (target.isOnline()) {
            if (!permissionHandler.has(staff, options.enderchestsConfiguration.getPermissionViewOnline())) {
                throw new BusinessException("You are not allowed to view the enderchest of an online player");
            }
            new ChestGUI(staff, target, target.getPlayer().getEnderChest(), InventoryType.ENDER_CHEST, ChestGuiType.ENDER_CHEST_EXAMINE, permissionHandler.has(staff, options.enderchestsConfiguration.getPermissionInteract()));
        } else {
            if (!permissionHandler.has(staff, options.enderchestsConfiguration.getPermissionViewOffline())) {
                throw new BusinessException("You are not allowed to view the enderchest of an offline player");
            }

            Inventory offlineEnderchest = InventoryFactory.loadEnderchestOffline(staff, target);
            new ChestGUI(staff, target, offlineEnderchest, InventoryType.ENDER_CHEST, ChestGuiType.ENDER_CHEST_EXAMINE, permissionHandler.has(staff, options.enderchestsConfiguration.getPermissionInteract()));
        }
    }
}
