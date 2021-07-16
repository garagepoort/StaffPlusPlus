package net.shortninja.staffplus.core.domain.staff.chests;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.utils.InventoryFactory;
import net.shortninja.staffplus.core.domain.staff.chests.config.EnderchestsConfiguration;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

@IocBean
public class EnderChestService {

    private final PermissionHandler permissionHandler;
    private final InventoryFactory inventoryFactory;
    private final EnderchestsConfiguration enderchestsConfiguration;

    public EnderChestService(PermissionHandler permissionHandler, InventoryFactory inventoryFactory, EnderchestsConfiguration enderchestsConfiguration) {
        this.permissionHandler = permissionHandler;
        this.inventoryFactory = inventoryFactory;
        this.enderchestsConfiguration = enderchestsConfiguration;
    }

    public void openEnderChest(Player staff, SppPlayer target) {
        if (target.isOnline()) {
            if (!permissionHandler.has(staff, enderchestsConfiguration.permissionViewOnline)) {
                throw new BusinessException("&CYou are not allowed to view the enderchest of an online player");
            }
            new ChestGUI(target,
                target.getPlayer().getEnderChest(),
                InventoryType.ENDER_CHEST,
                ChestGuiType.ENDER_CHEST_EXAMINE,
                permissionHandler.has(staff, enderchestsConfiguration.permissionInteract)).show(staff);
        } else {
            if (!permissionHandler.has(staff, enderchestsConfiguration.permissionViewOffline)) {
                throw new BusinessException("&CYou are not allowed to view the enderchest of an offline player");
            }

            Inventory offlineEnderchest = inventoryFactory.loadEnderchestOffline(staff, target);
            new ChestGUI(target, offlineEnderchest, InventoryType.ENDER_CHEST, ChestGuiType.ENDER_CHEST_EXAMINE, permissionHandler.has(staff, enderchestsConfiguration.permissionInteract))
                .show(staff);
        }
    }
}
