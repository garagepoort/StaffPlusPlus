package net.shortninja.staffplus.util.factory;

import de.tr7zw.nbtapi.*;
import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.SppPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.io.File;
import java.io.IOException;

public final class InventoryFactory {

    private InventoryFactory() {
    }

    public static Inventory createInventory(Player player) {
        PlayerInventory playerInv = player.getInventory();
        Inventory inv = Bukkit.createInventory(player, InventoryType.CHEST);
        inv.setContents(playerInv.getContents());

        return playerInv;
    }

    public static Inventory loadEnderchestOffline(Player player, SppPlayer target) {
        try {
            String filename = Bukkit.getWorldContainer() + File.separator + IocContainer.getOptions().mainWorld + File.separator + "playerdata" + File.separator + target.getId() + ".dat";
            Inventory inventory = Bukkit.createInventory(player, InventoryType.ENDER_CHEST);
            NBTFile file = new NBTFile(new File(filename));
            NBTCompoundList enderItems = file.getCompoundList("EnderItems");
            for (NBTListCompound enderItem : enderItems) {
                ItemStack itemStack = NBTItem.convertNBTtoItem(enderItem);
                inventory.setItem(Byte.toUnsignedInt(enderItem.getByte("Slot")), itemStack);
            }
            return inventory;

        } catch (IOException e) {
            throw new RuntimeException("Player data file could not be loaded", e);
        }
    }

    public static void saveEnderchestOffline(Player player, SppPlayer target, Inventory inventory) {
        try {
            String filename = Bukkit.getWorldContainer() + File.separator + IocContainer.getOptions().mainWorld + File.separator + "playerdata" + File.separator + target.getId() + ".dat";
            NBTFile file = new NBTFile(new File(filename));
            NBTCompoundList enderItems = file.getCompoundList("EnderItems");
            enderItems.clear();

            for (int i = 0; i < inventory.getContents().length; i++) {
                ItemStack item = inventory.getContents()[i];
                if(item == null || item.getType() == Material.AIR) continue;

                NBTContainer nbtContainer = NBTItem.convertItemtoNBT(item);
                nbtContainer.setByte("Slot", new Integer(i).byteValue());
                enderItems.addCompound(nbtContainer);
            }
            file.save();
        } catch (IOException e) {
            throw new RuntimeException("Player data file could not be loaded", e);
        }
    }

    public static boolean isInventoryEmpty(Inventory inv) {
        for (ItemStack stack : inv.getContents())
            if (stack != null)
                return false;
        return true;
    }

    public static void saveEnderChest(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(StaffPlus.get(), () -> {
            File file = new File(StaffPlus.get().getDataFolder(), "EnderChests.yml");
            try {
                if (!file.exists())
                    file.createNewFile();
                YamlConfiguration enderChests = YamlConfiguration.loadConfiguration(file);
                int i = 0;
                for (ItemStack stack : player.getEnderChest().getContents()) {
                    if (stack != null) {
                        enderChests.set(player.getUniqueId().toString() + "." + i, stack);
                        i++;
                    }
                    enderChests.save(file);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
