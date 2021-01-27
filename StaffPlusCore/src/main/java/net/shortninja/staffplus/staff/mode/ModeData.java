package net.shortninja.staffplus.staff.mode;

import net.shortninja.staffplus.unordered.VanishType;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ModeData {
    private UUID uuid;
    private ItemStack[] playerInventory;
    private Location previousLocation;
    private boolean hasFlight;
    private GameMode gameMode;
    private VanishType vanishType;
    private float xp;

    public ModeData(Player player, VanishType vanishType) {
        this.uuid = player.getUniqueId();
        this.playerInventory = player.getInventory().getContents();

        this.previousLocation = player.getLocation();
        this.hasFlight = player.getAllowFlight();
        this.gameMode = player.getGameMode();
        this.xp = player.getExp();
        this.vanishType = vanishType;
    }

    public ModeData(UUID uuid, ItemStack[] playerInventory, Location previousLocation, boolean hasFlight, GameMode gameMode, VanishType vanishType, float xp) {
        this.uuid = uuid;
        this.playerInventory = playerInventory;
        this.previousLocation = previousLocation;
        this.hasFlight = hasFlight;
        this.gameMode = gameMode;
        this.vanishType = vanishType;
        this.xp = xp;
    }

    public UUID getUuid() {
        return uuid;
    }

    public ItemStack[] getPlayerInventory() {
        return playerInventory;
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

    public float getXp(){
        return xp;
    }

}