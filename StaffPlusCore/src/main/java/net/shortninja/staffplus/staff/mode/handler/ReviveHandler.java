package net.shortninja.staffplus.staff.mode.handler;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.staff.mode.ModeCoordinator;
import net.shortninja.staffplus.staff.mode.InventoryVault;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.lib.JavaUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReviveHandler {
    private final static Map<UUID, InventoryVault> savedInventories = new HashMap<UUID, InventoryVault>();
    private final MessageCoordinator message = IocContainer.getMessage();
    private final Messages messages = IocContainer.getMessages();

    public boolean hasSavedInventory(UUID uuid) {
        return savedInventories.containsKey(uuid);
    }

    public void cacheInventory(Player player) {
        UUID uuid = player.getUniqueId();
        InventoryVault inventoryVault;

        inventoryVault = new InventoryVault(uuid, ModeCoordinator.getContents(player), player.getInventory().getArmorContents(), player.getInventory().getExtraContents());

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