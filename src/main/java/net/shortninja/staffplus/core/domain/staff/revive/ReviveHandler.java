package net.shortninja.staffplus.core.domain.staff.revive;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.utils.MessageCoordinator;
import net.shortninja.staffplus.core.domain.staff.mode.StaffModeService;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@IocBean
public class ReviveHandler {
    private final static Map<UUID, InventoryVault> savedInventories = new HashMap<UUID, InventoryVault>();
    private final MessageCoordinator message;
    private final Messages messages;

    public ReviveHandler(MessageCoordinator message, Messages messages) {
        this.message = message;
        this.messages = messages;
    }

    public boolean hasSavedInventory(UUID uuid) {
        return savedInventories.containsKey(uuid);
    }

    public void cacheInventory(Player player) {
        UUID uuid = player.getUniqueId();
        InventoryVault inventoryVault;

        inventoryVault = new InventoryVault(uuid, StaffModeService.getContents(player), player.getInventory().getArmorContents(), player.getInventory().getExtraContents());

        savedInventories.put(uuid, inventoryVault);
    }

    public void restoreInventory(Player player) {
        UUID uuid = player.getUniqueId();
        InventoryVault inventoryVault = savedInventories.get(uuid);

        JavaUtils.clearInventory(player);

        getItems(player, inventoryVault);
        player.getInventory().setArmorContents(inventoryVault.getArmor());
        player.getInventory().setExtraContents(inventoryVault.getOffHand());
        message.send(player, messages.revivedUser, messages.prefixGeneral);
        savedInventories.remove(uuid);
    }

    private void getItems(Player p, InventoryVault inventoryVault) {
        ItemStack[] items = inventoryVault.getInventory();
        for (int i = 0; i < items.length; i++) {
            p.getInventory().setItem(i, items[i]);
        }
    }
}