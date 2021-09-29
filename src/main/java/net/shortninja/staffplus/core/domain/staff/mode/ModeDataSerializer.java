package net.shortninja.staffplus.core.domain.staff.mode;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.BukkitInventorySerialization;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;

public class ModeDataSerializer {
    private static final String PREVIOUS_LOCATION = "PreviousLocation";
    private static final String XP = "Xp";
    private static final String INVENTORY = "Inventory";
    private static final String UUID_SELECTOR = "uuid";
    private static final String FLIGHT = "Flight";
    private static final String VANISH_TYPE = "VanishType";
    private static final String GAME_MODE = "GameMode";
    private static final String FIRE_TICKS = "FIRE_TICKS";
    private static final String POTION_EFFECTS = "PotionEffects";

    private File createFile(String uuid) {
        File file = getFile(uuid);
        File folder = new File(StaffPlus.get().getDataFolder(), "ModeData");
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
            List<String> potionEffectConfig = dataFile.getStringList(POTION_EFFECTS);
            List<PotionEffect> potionEffects = potionEffectConfig.stream()
                .map(pConfig -> {
                    String[] split = pConfig.split(";");
                    return new PotionEffect(PotionEffectType.getByName(split[0]),
                        parseInt(split[1]),
                        parseInt(split[2]),
                        parseBoolean(split[3]),
                        parseBoolean(split[4]));
                }).collect(Collectors.toList());

            return Optional.of(new ModeData(
                UUID.fromString(dataFile.getString(UUID_SELECTOR)),
                BukkitInventorySerialization.itemStackArrayFromBase64(dataFile.getString(INVENTORY)),
                dataFile.getSerializable(PREVIOUS_LOCATION, Location.class),
                dataFile.getBoolean(FLIGHT, false),
                GameMode.valueOf(dataFile.getString(GAME_MODE, "SURVIVAL")),
                VanishType.valueOf(dataFile.getString(VANISH_TYPE, "NONE")),
                (float) dataFile.getDouble(XP, 0),
                dataFile.getInt(FIRE_TICKS, 0),
                potionEffects));
        } catch (IOException e) {
            throw new RuntimeException("Unable to retrieve mode data for player [" + uuid + "]");
        }
    }

    public synchronized void save(ModeData modeData) {
        List<String> parsedPotionEffects = modeData.getPotionEffects().stream()
            .map(p -> p.getType().getName() + ";" + p.getDuration() + ";" + p.getAmplifier() + ";" + p.isAmbient() + ";" + p.hasParticles())
            .collect(Collectors.toList());

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
        data.set(POTION_EFFECTS, parsedPotionEffects);
        try {
            data.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getFile(String uuid) {
        return new File(StaffPlus.get().getDataFolder() + File.separator + "ModeData" + File.separator + uuid + ".yml");
    }
}
