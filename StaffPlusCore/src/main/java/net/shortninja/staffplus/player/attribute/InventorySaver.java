package net.shortninja.staffplus.player.attribute;

import net.shortninja.staffplus.StaffPlus;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class InventorySaver {
    private  File file;
    private YamlConfiguration inventory;
    private StaffPlus staff = StaffPlus.get();
    private UUID uuid;

    public InventorySaver(UUID uuid){
        this.uuid = uuid;
        file = new File(staff.getDataFolder()+File.separator+"StaffInv"+File.separator+uuid.toString()+".yml");
        createFile();
    }

    private void createFile(){
        File folder = new File(staff.getDataFolder()+File.separator+"StaffInv");
        if(!folder.exists()){
            folder.mkdir();
        }
        if(!file.exists()) {
            try {
                file.createNewFile();
                inventory = YamlConfiguration.loadConfiguration(file);
                inventory.createSection("Inventory");
                inventory.createSection("Armor");
                inventory.save(file);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public boolean shouldLoad(){
        file = new File(staff.getDataFolder()+File.separator+"StaffInv"+File.separator+uuid.toString()+".yml");
        return !file.exists();
    }

    public void deleteFile(){
        file.delete();
        try {
            inventory.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        inventory = null;
    }

    public void save(Player p, ItemStack[] items, ItemStack[] armor) {
        inventory = YamlConfiguration.loadConfiguration(file);
        for (int i = 0; i <= p.getInventory().getContents().length-1; i++) {
            inventory.set("Inventory." + i, p.getInventory().getContents()[i]);
        }
        for (int i = 0; i <= armor.length-1; i++) {
            inventory.set("Armor." + i,armor[i]);
        }
        try {
            inventory.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ItemStack[] getContents(){
        ArrayList<ItemStack> items = new ArrayList<>();
        inventory = YamlConfiguration.loadConfiguration(file);
        for(String num : inventory.getConfigurationSection("Inventory").getKeys(false)){
            items.add(inventory.getItemStack("Inventory."+num));
        }
        return items.toArray(new ItemStack[0]);
    }

    public ItemStack[] getArmor(){
        ArrayList<ItemStack> items = new ArrayList<>();
        inventory = YamlConfiguration.loadConfiguration(file);
        for(String num : inventory.getConfigurationSection("Armor").getKeys(false)){
            items.add(inventory.getItemStack("Armor."+num));
        }
        return items.toArray(new ItemStack[0]);
    }

}
