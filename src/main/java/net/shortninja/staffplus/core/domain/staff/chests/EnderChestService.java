package net.shortninja.staffplus.core.domain.staff.chests;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.utils.InventoryFactory;
import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

@IocBean
public class EnderChestService {

    private final PermissionHandler permissionHandler;
    private final Options options;
    private final InventoryFactory inventoryFactory;

    public EnderChestService(PermissionHandler permissionHandler, Options options, InventoryFactory inventoryFactory) {
        this.permissionHandler = permissionHandler;
        this.options = options;
        this.inventoryFactory = inventoryFactory;
    }

    public void openEnderChest(Player staff, SppPlayer target) {
        if (target.isOnline()) {
            if (!permissionHandler.has(staff, options.enderchestsConfiguration.getPermissionViewOnline())) {
                throw new BusinessException("&CYou are not allowed to view the enderchest of an online player");
            }
            new ChestGUI(target,
                target.getPlayer().getEnderChest(),
                InventoryType.ENDER_CHEST,
                ChestGuiType.ENDER_CHEST_EXAMINE,
                permissionHandler.has(staff, options.enderchestsConfiguration.getPermissionInteract())).show(staff);
        } else {
            if (!permissionHandler.has(staff, options.enderchestsConfiguration.getPermissionViewOffline())) {
                throw new BusinessException("&CYou are not allowed to view the enderchest of an offline player");
            }

            Inventory offlineEnderchest = inventoryFactory.loadEnderchestOffline(staff, target);
            new ChestGUI(target, offlineEnderchest, InventoryType.ENDER_CHEST, ChestGuiType.ENDER_CHEST_EXAMINE, permissionHandler.has(staff, options.enderchestsConfiguration.getPermissionInteract()))
                .show(staff);
        }
    }
}
