package net.shortninja.staffplus.player.attribute;

import net.shortninja.staffplus.StaffPlus;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This class is used to save inventories to a file so users in mode
 * do not lose their inventory if the server crashes
 */
public class InventorySerializer {
    private File file;
    private YamlConfiguration inventory;
    private StaffPlus staff = StaffPlus.get();
    private UUID uuid;


    public InventorySerializer(UUID uuid) {
        this.uuid = uuid;
        file = new File(staff.getDataFolder(), "Invs/" + uuid.toString() + ".yml");
        createFile();
    }

    private void createFile() {
        File folder = new File(staff.getDataFolder(), "Invs");
        if (!folder.exists()) {
            folder.mkdir();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
                inventory = YamlConfiguration.loadConfiguration(file);
                inventory.createSection("Inventory_V2");
                inventory.createSection("Armor_V2");
                inventory.createSection("OffHand_V2");
                inventory.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean shouldLoad() {
        file = new File(staff.getDataFolder() + File.separator + "Invs" + File.separator + uuid.toString() + ".yml");
        if (file.exists()) {
            Player p = Bukkit.getPlayer(uuid);
            Inventory inv = Bukkit.createInventory(p, InventoryType.PLAYER);
            inv.addItem(getContents());
            return areInvsSame(p.getInventory(), inv);
        } else
            return false;
    }

    public void deleteFile() {
        file = new File(staff.getDataFolder() + File.separator + "Invs" + File.separator + uuid.toString() + ".yml");
        if (file.exists()) {
            file.delete();
        }
    }

    public void save(ItemStack[] items, ItemStack[] armor, ItemStack[] offHand, float xp) {
        inventory = YamlConfiguration.loadConfiguration(file);
        inventory.set("Inventory_V2", items);
        inventory.set("Armor_V2", armor);
        inventory.set("OffHand_V2", offHand);
        inventory.set("Xp", xp);
        try {
            inventory.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ItemStack[] getContents() {
        inventory = YamlConfiguration.loadConfiguration(file);
        if (inventory.contains("Inventory_V2") && inventory.getList("Inventory_V2") != null) {
            return inventory.getList("Inventory_V2").toArray(new ItemStack[]{});
        }
        if (inventory.contains("Inventory")) {
            //legacy
            List<ItemStack> items = new ArrayList<>();
            for (String num : inventory.getConfigurationSection("Inventory").getKeys(false)) {
                items.add(inventory.getItemStack("Inventory." + num));
            }
            return items.toArray(new ItemStack[]{});
        }
        return new ItemStack[]{};
    }

    public ItemStack[] getArmor() {
        inventory = YamlConfiguration.loadConfiguration(file);
        if (inventory.contains("Armor_V2") && inventory.getList("Armor_V2") != null) {
            return inventory.getList("Armor_V2").toArray(new ItemStack[]{});
        }
        if (inventory.contains("Armor")) {
            //legacy
            List<ItemStack> items = new ArrayList<>();
            for (String num : inventory.getConfigurationSection("Armor").getKeys(false)) {
                items.add(inventory.getItemStack("Armor." + num));
            }
            return items.toArray(new ItemStack[]{});
        }

        return new ItemStack[]{};
    }

    public ItemStack[] getOffHand() {
        inventory = YamlConfiguration.loadConfiguration(file);

        if (inventory.contains("OffHand_V2") && inventory.getList("OffHand_V2") != null) {
            return inventory.getList("OffHand_V2").toArray(new ItemStack[]{});
        }
        if (inventory.contains("OffHand")) {
            //legacy
            List<ItemStack> items = new ArrayList<>();
            for (String num : inventory.getConfigurationSection("OffHand").getKeys(false)) {
                items.add(inventory.getItemStack("OffHand." + num));
            }
            return items.toArray(new ItemStack[]{});
        }

        return new ItemStack[]{};
    }


    public float getXp() {
        inventory = YamlConfiguration.loadConfiguration(file);
        return (float) inventory.getDouble("Xp");
    }

    private boolean areInvsSame(Inventory pInv, Inventory inv) {
        for (int i = 0; i < pInv.getSize(); i++) {
            if (pInv.getItem(i) != null && inv.getItem(i) != null)
                if (!pInv.getItem(i).equals(inv.getItem(i)))
                    return false;
        }
        return true;
    }


}
