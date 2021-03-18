package net.shortninja.staffplus.staff.mode;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.BukkitInventorySerialization;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class ModeDataSerializer {
    private static final String PREVIOUS_LOCATION = "PreviousLocation";
    private static final String XP = "Xp";
    private static final String INVENTORY = "Inventory";
    private static final String UUID_SELECTOR = "uuid";
    private static final String FLIGHT = "Flight";
    private static final String VANISH_TYPE = "VanishType";
    private static final String GAME_MODE = "GameMode";
    private static final String FIRE_TICKS = "FIRE_TICKS";
    private StaffPlus staff = StaffPlus.get();

    private File createFile(String uuid) {
        File file = getFile(uuid);
        File folder = new File(staff.getDataFolder(), "ModeData");
        if (!folder.exists()) {
            folder.mkdir();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
                YamlConfiguration inventory = YamlConfiguration.loadConfiguration(file);
                inventory.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public void delete(String uuid) {
        File file = getFile(uuid);
        if (file.exists()) {
            file.delete();
        }
    }

    public Optional<ModeData> retrieve(UUID uuid) {
        try {
            File file = getFile(uuid.toString());
            if (!file.exists()) {
                return Optional.empty();
            }

            YamlConfiguration dataFile = YamlConfiguration.loadConfiguration(file);
            return Optional.of(new ModeData(
                UUID.fromString(dataFile.getString(UUID_SELECTOR)),
                BukkitInventorySerialization.itemStackArrayFromBase64(dataFile.getString(INVENTORY)),
                dataFile.getLocation(PREVIOUS_LOCATION),
                dataFile.getBoolean(FLIGHT, false),
                GameMode.valueOf(dataFile.getString(GAME_MODE, "SURVIVAL")),
                VanishType.valueOf(dataFile.getString(VANISH_TYPE, "NONE")),
                (float) dataFile.getDouble(XP, 0),
                dataFile.getInt(FIRE_TICKS, 0)));
        } catch (IOException e) {
            throw new RuntimeException("Unable to retrieve mode data for player [" + uuid + "]");
        }
    }

    public void save(ModeData modeData) {
        UUID uuid = modeData.getUuid();
        delete(uuid.toString());
        File dataFile = createFile(uuid.toString());
        YamlConfiguration data = YamlConfiguration.loadConfiguration(dataFile);
        data.set(UUID_SELECTOR, modeData.getUuid().toString());
        data.set(INVENTORY, BukkitInventorySerialization.itemStackArrayToBase64(modeData.getPlayerInventory()));
        data.set(XP, modeData.getXp());
        data.set(PREVIOUS_LOCATION, modeData.getPreviousLocation());
        data.set(FLIGHT, modeData.hasFlight());
        data.set(VANISH_TYPE, modeData.getVanishType().toString());
        data.set(GAME_MODE, modeData.getGameMode().toString());
        data.set(FIRE_TICKS, modeData.getFireTicks());
        try {
            data.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getFile(String uuid) {
        return new File(staff.getDataFolder() + File.separator + "ModeData" + File.separator + uuid + ".yml");
    }

    private ItemStack[] getContents(YamlConfiguration dataFile) {
        if (dataFile.contains(INVENTORY) && dataFile.getList(INVENTORY) != null) {
            return dataFile.getList(INVENTORY).toArray(new ItemStack[]{});
        }
        return new ItemStack[]{};
    }
}
