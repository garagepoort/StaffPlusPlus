package net.shortninja.staffplus.player.attribute.mode;

import net.shortninja.staffplus.player.attribute.mode.handler.VanishHandler.VanishType;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ModeDataVault {
    private UUID uuid;
    private ItemStack[] items;
    private ItemStack[] armor;
    private Location previousLocation;
    private boolean hasFlight;
    private GameMode gameMode;
    private VanishType vanishType;

    public ModeDataVault(UUID uuid, ItemStack[] items, ItemStack[] armor, Location previousLocation, boolean hasFlight, GameMode gameMode, VanishType vanishType) {
        this.uuid = uuid;
        this.items = items;
        this.armor = armor;
        this.previousLocation = previousLocation;
        this.hasFlight = hasFlight;
        this.gameMode = gameMode;
        this.vanishType = vanishType;
    }

    public ModeDataVault(UUID uuid, ItemStack[] items, ItemStack[] armor) {
        this.uuid = uuid;
        this.items = items;
        this.armor = armor;
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
}