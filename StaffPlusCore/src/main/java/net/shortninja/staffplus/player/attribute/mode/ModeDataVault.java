package net.shortninja.staffplus.player.attribute.mode;

import net.shortninja.staffplus.player.attribute.InventorySerializer;
import net.shortninja.staffplus.player.attribute.mode.handler.VanishHandler.VanishType;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ModeDataVault {
    private UUID uuid;
    private ItemStack[] items;
    private ItemStack[] armor;
    private ItemStack[] offHand;
    private Location previousLocation;
    private boolean hasFlight;
    private GameMode gameMode;
    private VanishType vanishType;

    public ModeDataVault(UUID uuid, ItemStack[] items, ItemStack[] armor, Location previousLocation, boolean hasFlight, GameMode gameMode, VanishType vanishType) {
        this.uuid = uuid;
        this.previousLocation = previousLocation;
        this.hasFlight = hasFlight;
        this.gameMode = gameMode;
        this.vanishType = vanishType;
        InventorySerializer save = new InventorySerializer(uuid);
        save.save(items,armor);
    }

    public ModeDataVault(UUID uuid, ItemStack[] items, ItemStack[] armor, ItemStack[] offHand, Location previousLocation, boolean hasFlight, GameMode gameMode, VanishType vanishType) {
        this.uuid = uuid;
        this.previousLocation = previousLocation;
        this.hasFlight = hasFlight;
        this.gameMode = gameMode;
        this.vanishType = vanishType;
        this.offHand = offHand;
        InventorySerializer save = new InventorySerializer(uuid);
        save.save(items,armor,offHand);
    }

    public ModeDataVault(UUID uuid, ItemStack[] items, ItemStack[] armor) {
        this.uuid = uuid;
        this.items = items;
        this.armor = armor;
    }

    public ModeDataVault(UUID uuid, ItemStack[] items, ItemStack[] armor, ItemStack[] offHand) {
        this.uuid = uuid;
        this.items = items;
        this.armor = armor;
        this.offHand = offHand;
    }

    public UUID getUuid() {
        return uuid;
    }

    public ItemStack[] getItems() {
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
}