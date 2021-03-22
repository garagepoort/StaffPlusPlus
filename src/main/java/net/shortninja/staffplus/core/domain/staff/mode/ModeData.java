package net.shortninja.staffplus.core.domain.staff.mode;

import net.shortninja.staffplusplus.vanish.VanishType;
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
    private int fireTicks;

    public ModeData(Player player, net.shortninja.staffplusplus.vanish.VanishType vanishType) {
        this.uuid = player.getUniqueId();
        this.playerInventory = player.getInventory().getContents();

        this.previousLocation = player.getLocation();
        this.hasFlight = player.getAllowFlight();
        this.gameMode = player.getGameMode();
        this.xp = player.getExp();
        this.vanishType = vanishType;
        this.fireTicks = player.getFireTicks();
    }

    public ModeData(UUID uuid, ItemStack[] playerInventory, Location previousLocation, boolean hasFlight, GameMode gameMode, VanishType vanishType, float xp, int fireTicks) {
        this.uuid = uuid;
        this.playerInventory = playerInventory;
        this.previousLocation = previousLocation;
        this.hasFlight = hasFlight;
        this.gameMode = gameMode;
        this.vanishType = vanishType;
        this.xp = xp;
        this.fireTicks = fireTicks;
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

    public int getFireTicks() {
        return fireTicks;
    }
}