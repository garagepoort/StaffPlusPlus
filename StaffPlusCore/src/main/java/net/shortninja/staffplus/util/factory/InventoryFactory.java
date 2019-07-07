package net.shortninja.staffplus.util.factory;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;

public final class InventoryFactory {

    private InventoryFactory() { }

    public static Inventory createInventory(Player player) {
        PlayerInventory playerInv = player.getInventory();
        Inventory inv = Bukkit.createInventory(player, InventoryType.CHEST);
        inv.setContents(playerInv.getContents());

        return playerInv;
    }

    public static Inventory createEnderchestInventory(Player player) {
        Inventory ecInv = player.getEnderChest();
        Inventory inv = Bukkit.createInventory(player, InventoryType.ENDER_CHEST);
        inv.setContents(ecInv.getContents());

        return inv;
    }
}
