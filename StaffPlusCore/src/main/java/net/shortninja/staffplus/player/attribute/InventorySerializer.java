package net.shortninja.staffplus.player.attribute;

import net.shortninja.staffplus.StaffPlus;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class InventorySerializer {
    private  File file;
    private YamlConfiguration inventory;
    private StaffPlus staff = StaffPlus.get();
    private UUID uuid;

    public InventorySerializer(UUID uuid){
        this.uuid = uuid;
        file = new File(staff.getDataFolder(), "StaffInv/" + uuid.toString() + ".yml");
        createFile();
    }

    private void createFile(){
        File folder = new File(staff.getDataFolder(),"StaffInv");
        if(!folder.exists()){
            folder.mkdir();
        }
        if(!file.exists()) {
            try {
                file.createNewFile();
                inventory = YamlConfiguration.loadConfiguration(file);
                inventory.createSection("Inventory");
                inventory.createSection("Armor");
                String[] tmp = Bukkit.getServer().getVersion().split("MC: ");
                String version = tmp[tmp.length - 1].substring(0, 3);
                if(!version.equalsIgnoreCase("1.8")||!version.equalsIgnoreCase("1.7"))
                    inventory.createSection("OffHand");
                inventory.save(file);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public boolean shouldLoad(){
        file = new File(staff.getDataFolder()+File.separator+"StaffInv"+File.separator+uuid.toString()+".yml");
        if (file.exists())
            return true;
        else
            return false;
    }

    public void deleteFile(){
        file = new File(staff.getDataFolder()+File.separator+"StaffInv"+File.separator+uuid.toString()+".yml");
        file.delete();
        /*try {
            inventory.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        inventory = null;*/
    }

    public void save(ItemStack[] items, ItemStack[] armor) {
        inventory = YamlConfiguration.loadConfiguration(file);
        for (int i = 0; i <= items.length-1; i++) {
            inventory.set("Inventory." + i, items[i]);
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

    public void save(HashMap<String,ItemStack> items, ItemStack[] armor) {
        inventory = YamlConfiguration.loadConfiguration(file);
        for (String i : items.keySet()) {
            inventory.set("Inventory." + i, items.get(i));
        }
        for (int i = 0; i <= armor.length-1; i++) {
            inventory.set("Armor." + i,armor[i]);
        }
        try{
            inventory.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void save(HashMap<String,ItemStack> items, ItemStack[] armor, ItemStack[] offHand) {
        inventory = YamlConfiguration.loadConfiguration(file);
        for (String i : items.keySet()) {
            inventory.set("Inventory." + i, items.get(i));
        }
        for (int i = 0; i <= armor.length-1; i++) {
            inventory.set("Armor." + i,armor[i]);
        }
        for (int i = 0; i <= offHand.length-1; i++) {
            inventory.set("OffHand." + i,offHand[i]);
        }
        try {
            inventory.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, ItemStack> getContents(){
        HashMap<String,ItemStack> items = new HashMap<>();
        inventory = YamlConfiguration.loadConfiguration(file);
        for(String num : inventory.getConfigurationSection("Inventory").getKeys(false)){
            items.put(num, inventory.getItemStack("Inventory."+num));
        }
        return items;
    }

    public ItemStack[] getArmor(){
        ArrayList<ItemStack> items = new ArrayList<>();
        inventory = YamlConfiguration.loadConfiguration(file);
        for(String num : inventory.getConfigurationSection("Armor").getKeys(false)){
            items.add(inventory.getItemStack("Armor."+num));
        }
        return items.toArray(new ItemStack[0]);
    }

    public ItemStack[] getOffHand(){
        ArrayList<ItemStack> items = new ArrayList<>();
        inventory = YamlConfiguration.loadConfiguration(file);
        for(String num : inventory.getConfigurationSection("OffHand").getKeys(false)){
            items.add(inventory.getItemStack("OffHand."+num));
        }
        return items.toArray(new ItemStack[0]);
    }


}
