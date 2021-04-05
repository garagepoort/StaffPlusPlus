package net.shortninja.staffplus.core.common.utils;

import be.garagepoort.mcioc.IocBean;
import de.tr7zw.nbtapi.*;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;

@IocBean
public final class InventoryFactory {

    private final Options options;
    public InventoryFactory(Options options) {
        this.options = options;
    }

    public Inventory loadEnderchestOffline(Player player, SppPlayer target) {
        try {
            String filename = Bukkit.getWorldContainer() + File.separator + options.mainWorld + File.separator + "playerdata" + File.separator + target.getId() + ".dat";
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

    public void saveEnderchestOffline(SppPlayer target, Inventory inventory) {
        try {
            String filename = Bukkit.getWorldContainer() + File.separator + options.mainWorld + File.separator + "playerdata" + File.separator + target.getId() + ".dat";
            NBTFile file = new NBTFile(new File(filename));
            NBTCompoundList enderItems = file.getCompoundList("EnderItems");
            enderItems.clear();

            for (int i = 0; i < inventory.getContents().length; i++) {
                ItemStack item = inventory.getContents()[i];
                if (item == null || item.getType() == Material.AIR) continue;

                NBTContainer nbtContainer = NBTItem.convertItemtoNBT(item);
                nbtContainer.setByte("Slot", new Integer(i).byteValue());
                enderItems.addCompound(nbtContainer);
            }
            file.save();
        } catch (IOException e) {
            throw new RuntimeException("Player data file could not be loaded", e);
        }
    }

    public Inventory loadInventoryOffline(Player player, SppPlayer target) {
        try {
            String filename = Bukkit.getWorldContainer() + File.separator + options.mainWorld + File.separator + "playerdata" + File.separator + target.getId() + ".dat";
            Inventory inventory = Bukkit.createInventory(player, InventoryType.PLAYER);
            NBTFile file = new NBTFile(new File(filename));
            NBTCompoundList inventoryItems = file.getCompoundList("Inventory");
            for (NBTListCompound item : inventoryItems) {
                ItemStack itemStack = NBTItem.convertNBTtoItem(item);
                int slot = Byte.toUnsignedInt(item.getByte("Slot"));
                if (slot <= 35 && slot >= 0) {
                    inventory.setItem(slot, itemStack);
                } else {
                    if (slot == 100) {
                        inventory.setItem(36, itemStack);
                    }
                    if (slot == 101) {
                        inventory.setItem(37, itemStack);
                    }
                    if (slot == 102) {
                        inventory.setItem(38, itemStack);
                    }
                    if (slot == 103) {
                        inventory.setItem(39, itemStack);
                    }
                    if (slot == -106) {
                        inventory.setItem(40, itemStack);
                    }
                }
            }
            return inventory;

        } catch (IOException e) {
            throw new RuntimeException("Player data file could not be loaded", e);
        }
    }

    public void saveInventoryOffline(SppPlayer target, Inventory inventory) {
        try {
            String filename = Bukkit.getWorldContainer() + File.separator + options.mainWorld + File.separator + "playerdata" + File.separator + target.getId() + ".dat";
            NBTFile file = new NBTFile(new File(filename));
            NBTCompoundList inventoryNbt = file.getCompoundList("Inventory");
            inventoryNbt.clear();

            for (int i = 0; i < inventory.getContents().length; i++) {
                ItemStack item = inventory.getContents()[i];
                if (item == null || item.getType() == Material.AIR) continue;

                NBTContainer nbtContainer = NBTItem.convertItemtoNBT(item);
                if (i <= 35) {
                    nbtContainer.setByte("Slot", new Integer(i).byteValue());
                } else {
                    if (i == 36) {
                        nbtContainer.setByte("Slot", new Integer(100).byteValue());
                    }
                    if (i == 37) {
                        nbtContainer.setByte("Slot", new Integer(101).byteValue());
                    }
                    if (i == 38) {
                        nbtContainer.setByte("Slot", new Integer(102).byteValue());
                    }
                    if (i == 39) {
                        nbtContainer.setByte("Slot", new Integer(103).byteValue());
                    }
                    if (i == 40) {
                        nbtContainer.setByte("Slot", new Integer(-106).byteValue());
                    }
                }
                inventoryNbt.addCompound(nbtContainer);
            }
            file.save();
        } catch (IOException e) {
            throw new RuntimeException("Player data file could not be loaded", e);
        }
    }
}
