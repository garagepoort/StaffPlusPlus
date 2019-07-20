package net.shortninja.staffplus.util.factory;

import net.shortninja.staffplus.StaffPlus;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.io.File;
import java.io.IOException;

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
    public static boolean isInventoryEmpty(Inventory inv){
        for(ItemStack stack : inv.getContents())
            if (stack == null)
                return false;
        return true;
    }

    public static void saveEnderChest(Player player){
        File file = new File(StaffPlus.get().getDataFolder()+"/EnderChests.yml");
        if(!file.exists()){
            try {
                file.createNewFile();
                YamlConfiguration enderChests = YamlConfiguration.loadConfiguration(file);
                int i = 0;
                for(ItemStack stack : player.getEnderChest().getContents()){
                    if(stack !=null){
                        enderChests.set(player.getUniqueId().toString()+"."+i,stack);
                        i++;
                    }
                }
                enderChests.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Inventory createVirtualEnderChest(OfflinePlayer p){
        File file = new File(StaffPlus.get().getDataFolder()+"/EnderChests.yml");
        Inventory eChest = Bukkit.createInventory(null,InventoryType.ENDER_CHEST);
        if(!file.exists()) {
            try {
                file.createNewFile();
                YamlConfiguration enderChests = YamlConfiguration.loadConfiguration(file);
                for(String key : enderChests.getConfigurationSection(p.getUniqueId().toString()).getKeys(false)){
                    ItemStack stack = enderChests.getItemStack(p.getUniqueId().toString()+"."+key);
                    eChest.addItem(stack);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return eChest;
    }
}
