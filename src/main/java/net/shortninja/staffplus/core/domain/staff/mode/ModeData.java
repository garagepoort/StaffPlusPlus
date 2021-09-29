package net.shortninja.staffplus.core.domain.staff.mode;

import net.shortninja.staffplusplus.staffmode.IModeData;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ModeData implements IModeData {
    private final UUID uuid;
    private final ItemStack[] playerInventory;
    private final Location previousLocation;
    private final boolean hasFlight;
    private final GameMode gameMode;
    private final VanishType vanishType;
    private final float xp;
    private final int fireTicks;
    private final List<PotionEffect> potionEffects;

    public ModeData(Player player, net.shortninja.staffplusplus.vanish.VanishType vanishType) {
        this.uuid = player.getUniqueId();
        this.playerInventory = player.getInventory().getContents();

        this.previousLocation = player.getLocation();
        this.hasFlight = player.getAllowFlight();
        this.gameMode = player.getGameMode();
        this.xp = player.getExp();
        this.vanishType = vanishType;
        this.fireTicks = player.getFireTicks();
        this.potionEffects = new ArrayList<>(player.getActivePotionEffects());
    }

    public ModeData(UUID uuid, ItemStack[] playerInventory, Location previousLocation, boolean hasFlight, GameMode gameMode, VanishType vanishType, float xp, int fireTicks, List<PotionEffect> potionEffects) {
        this.uuid = uuid;
        this.playerInventory = playerInventory;
        this.previousLocation = previousLocation;
        this.hasFlight = hasFlight;
        this.gameMode = gameMode;
        this.vanishType = vanishType;
        this.xp = xp;
        this.fireTicks = fireTicks;
        this.potionEffects = potionEffects;
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

    @Override
    public List<PotionEffect> getPotionEffects() {
        return potionEffects;
    }
}