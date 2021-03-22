package net.shortninja.staffplus.core.application.data;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.session.PlayerSession;
import net.shortninja.staffplusplus.alerts.AlertType;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DataFile {
    private static final String DATA_YML = "data.yml";
    private static YamlConfiguration configuration;

    private DataFile(){}

    public static void init() {
        File file = new File(StaffPlus.get().getDataFolder(), DATA_YML);
        if (!file.exists()) {
            StaffPlus.get().saveResource(DATA_YML, false);
        }
        configuration = YamlConfiguration.loadConfiguration(file);
    }

    public static synchronized void save(PlayerSession session) {
        try {
            File file = new File(StaffPlus.get().getDataFolder(), DATA_YML);
            YamlConfiguration configuration = new YamlConfiguration();
            configuration.set(session.getUuid() + ".name", session.getName());
            configuration.set(session.getUuid() + ".glass-color", session.getGlassColor() != null ? session.getGlassColor().name() : Material.WHITE_STAINED_GLASS_PANE);
            configuration.set(session.getUuid() + ".notes", new ArrayList<>(session.getPlayerNotes()));
            configuration.set(session.getUuid() + ".alert-options", alertOptions(session));
            configuration.set(session.getUuid() + ".vanish-type", session.getVanishType() != null ? session.getVanishType().name() : VanishType.NONE.name());
            configuration.set(session.getUuid() + ".staff-mode", session.isInStaffMode());
            configuration.save(file);

            DataFile.configuration = YamlConfiguration.loadConfiguration(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private static List<String> alertOptions(PlayerSession session) {
        return Arrays.stream(AlertType.values())
            .map(alertType -> alertType.name() + ";" + session.shouldNotify(alertType))
            .collect(Collectors.toList());
    }


    public static FileConfiguration getConfiguration() {
        return configuration;
    }
}