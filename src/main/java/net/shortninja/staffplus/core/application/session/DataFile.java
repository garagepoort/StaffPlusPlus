package net.shortninja.staffplus.core.application.session;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.domain.staff.mode.config.GeneralModeConfiguration;
import net.shortninja.staffplusplus.alerts.AlertType;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@IocBean
public class DataFile {
    private static final String DATA_YML = "data.yml";

    private final YamlConfiguration configuration;

    public DataFile(){
        File file = new File(StaffPlus.get().getDataFolder(), DATA_YML);
        if (!file.exists()) {
            StaffPlus.get().saveResource(DATA_YML, false);
        }
        configuration = YamlConfiguration.loadConfiguration(file);
    }

    public synchronized void save(PlayerSession session) {
        try {
            File file = new File(StaffPlus.get().getDataFolder(), DATA_YML);
            updateSessionInfo(session);
            configuration.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public synchronized void save(Collection<PlayerSession> sessions) {
        try {
            File file = new File(StaffPlus.get().getDataFolder(), DATA_YML);
            for (PlayerSession session : sessions) {
                updateSessionInfo(session);
            }
            configuration.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private synchronized  void updateSessionInfo(PlayerSession session) {
        configuration.set(session.getUuid() + ".name", session.getName());
        configuration.set(session.getUuid() + ".glass-color", session.getGlassColor() != null ? session.getGlassColor().name() : Material.WHITE_STAINED_GLASS_PANE);
        configuration.set(session.getUuid() + ".notes", new ArrayList<>(session.getPlayerNotes()));
        configuration.set(session.getUuid() + ".alert-options", alertOptions(session));
        configuration.set(session.getUuid() + ".vanish-type", session.getVanishType() != null ? session.getVanishType().name() : VanishType.NONE.name());
        configuration.set(session.getUuid() + ".staff-mode", session.isInStaffMode());
        configuration.set(session.getUuid() + ".staff-mode-name", session.getModeConfiguration().map(GeneralModeConfiguration::getName).orElse(null));
    }

    private List<String> alertOptions(PlayerSession session) {
        return Arrays.stream(AlertType.values())
            .map(alertType -> alertType.name() + ";" + session.shouldNotify(alertType))
            .collect(Collectors.toList());
    }


    public FileConfiguration getConfiguration() {
        return configuration;
    }
}