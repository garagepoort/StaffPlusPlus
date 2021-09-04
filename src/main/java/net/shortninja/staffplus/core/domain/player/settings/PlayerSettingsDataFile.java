package net.shortninja.staffplus.core.domain.player.settings;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@IocBean
public class PlayerSettingsDataFile {
    private static final String DATA_YML = "data.yml";

    private final YamlConfiguration configuration;

    public PlayerSettingsDataFile() {
        File file = new File(StaffPlus.get().getDataFolder(), DATA_YML);
        if (!file.exists()) {
            StaffPlus.get().saveResource(DATA_YML, false);
        }
        configuration = YamlConfiguration.loadConfiguration(file);
    }

    public synchronized void save(PlayerSettings settings) {
        try {
            File file = new File(StaffPlus.get().getDataFolder(), DATA_YML);
            updateSettings(settings);
            configuration.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public synchronized void save(Collection<PlayerSettings> settings) {
        try {
            File file = new File(StaffPlus.get().getDataFolder(), DATA_YML);
            settings.forEach(this::updateSettings);
            configuration.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private synchronized void updateSettings(PlayerSettings settings) {
        configuration.set(settings.getUuid() + ".name", settings.getName());
        configuration.set(settings.getUuid() + ".glass-color", settings.getGlassColor() != null ? settings.getGlassColor().name() : Material.WHITE_STAINED_GLASS_PANE);
        configuration.set(settings.getUuid() + ".alert-options", alertOptions(settings));
        configuration.set(settings.getUuid() + ".vanish-type", settings.getVanishType() != null ? settings.getVanishType().name() : VanishType.NONE.name());
        configuration.set(settings.getUuid() + ".staff-mode", settings.isInStaffMode());
        configuration.set(settings.getUuid() + ".staff-mode-name", settings.getModeName().orElse(null));
    }

    private List<String> alertOptions(PlayerSettings session) {
        return session.getAlertOptions().stream()
            .map(alertType -> alertType.name() + ";" + true)
            .collect(Collectors.toList());
    }


    public FileConfiguration getConfiguration() {
        return configuration;
    }
}