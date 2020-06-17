package net.shortninja.staffplus.player.attribute.mode;

import net.shortninja.staffplus.player.attribute.InventorySerializer;
import net.shortninja.staffplus.unordered.VanishType;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class ModeDataVault {
    private UUID uuid;
    private HashMap<Integer, ItemStack> items;
    private ItemStack[] armor;
    private ItemStack[] offHand;
    private Location previousLocation;
    private boolean hasFlight;
    private GameMode gameMode;
    private VanishType vanishType;


    private float xp;

    public ModeDataVault(UUID uuid, HashMap<Integer, ItemStack> itemHash, ItemStack[] armor, ItemStack[] offHand, Location previousLocation, float xp, boolean hasFlight, GameMode gameMode, VanishType vanishType) {
        this.uuid = uuid;
        this.previousLocation = previousLocation;
        this.hasFlight = hasFlight;
        this.gameMode = gameMode;
        this.vanishType = vanishType;
        this.offHand = offHand;
        InventorySerializer save = new InventorySerializer(uuid);
        save.save(itemHash,armor,offHand,xp);
    }

    public ModeDataVault(UUID uuid, HashMap<Integer, ItemStack> itemHash, ItemStack[] armor, Location previousLocation, boolean hasFlight, GameMode gameMode, VanishType vanishType) {
        this.xp = xp;
        InventorySerializer save = new InventorySerializer(uuid);
        save.save(itemHash,armor,offHand,xp);
    }

    public ModeDataVault(UUID uuid, HashMap<Integer, ItemStack> itemHash, ItemStack[] armor, Location previousLocation, float xp, boolean hasFlight, GameMode gameMode, VanishType vanishType) {
        this.uuid = uuid;
        this.previousLocation = previousLocation;
        this.hasFlight = hasFlight;
        this.gameMode = gameMode;
        this.vanishType = vanishType;
        InventorySerializer save = new InventorySerializer(uuid);
        save.save(itemHash,armor, xp);
    }

    public ModeDataVault(UUID uuid,HashMap<Integer, ItemStack> items, ItemStack[] armor) {
        this.uuid = uuid;
        this.items = items;
        this.armor = armor;
    }

    public ModeDataVault(UUID uuid, HashMap<Integer, ItemStack> itemHash, ItemStack[] armor, ItemStack[] offHand) {
        this.xp = xp;
        InventorySerializer save = new InventorySerializer(uuid);
        save.save(itemHash,armor,xp);
    }

    public ModeDataVault(UUID uuid,HashMap<Integer, ItemStack> items, ItemStack[] armor, float xp) {
        this.uuid = uuid;
        this.items = items;
        this.armor = armor;
        this.xp = xp;
    }

    public ModeDataVault(UUID uuid, HashMap<Integer, ItemStack> itemHash, ItemStack[] armor, ItemStack[] offHand, float xp) {
        this.uuid = uuid;
        this.offHand = offHand;
        this.items = itemHash;
        this.armor = armor;
        this.xp = xp;
    }

    public UUID getUuid() {
        return uuid;
    }

    public HashMap<String, ItemStack> getItems() {
        InventorySerializer serializer = new InventorySerializer(uuid);
        return serializer.getContents();
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