package net.shortninja.staffplus.player.attribute.mode;

import net.shortninja.staffplus.player.attribute.InventorySerializer;
import net.shortninja.staffplus.unordered.VanishType;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class InventoryVault {
    private UUID uuid;
    private HashMap<Integer, ItemStack> items;
    private ItemStack[] armor;
    private ItemStack[] offHand;
    private Location previousLocation;
    private boolean hasFlight;
    private GameMode gameMode;
    private VanishType vanishType;


    private float xp;

    public InventoryVault(UUID uuid, HashMap<Integer, ItemStack> itemHash, ItemStack[] armor, ItemStack[] offHand, Location previousLocation, float xp, boolean hasFlight, GameMode gameMode, VanishType vanishType) {
        this.uuid = uuid;
        this.previousLocation = previousLocation;
        this.hasFlight = hasFlight;
        this.gameMode = gameMode;
        this.vanishType = vanishType;
        this.offHand = offHand;
        InventorySerializer save = new InventorySerializer(uuid);
        save.save(itemHash,armor,offHand,xp);
    }


    public InventoryVault(UUID uuid, HashMap<Integer, ItemStack> itemHash, ItemStack[] armor, ItemStack[] offHand) {
        this.items = itemHash;
        this.armor = armor;
        this.offHand = offHand;
        this.uuid = uuid;
        InventorySerializer save = new InventorySerializer(uuid);
        save.save(itemHash,armor,xp);
    }

    public UUID getUuid() {
        return uuid;
    }

    public HashMap<String, ItemStack> getItems() {
        InventorySerializer serializer = new InventorySerializer(uuid);
        return serializer.getContents();
    }

    public HashMap<Integer, ItemStack> getInventory(){
        return items;
    }

    public ItemStack[] getArmor() {
        return armor;
    }

    public Location getPreviousLocation() {
        return previousLocation;
    }

    public boolean hasFlight() {
        return hasFlight;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public VanishType getVanishType() {
        return vanishType;
    }

    public ItemStack[] getOffHand() {
        return offHand;
    }


    public float getXp(){
        return xp;
    }
}