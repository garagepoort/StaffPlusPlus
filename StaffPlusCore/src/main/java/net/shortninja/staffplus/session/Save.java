package net.shortninja.staffplus.session;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplusplus.alerts.AlertType;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Save {
    private final FileConfiguration dataFile = StaffPlus.get().dataFile.getConfiguration();
    private final PlayerSession session;

    public Save(PlayerSession session) {
        this.session = session;
        saveSession();
    }

    private void saveSession() {
        dataFile.set(session.getUuid() + ".name", session.getName());
        dataFile.set(session.getUuid() + ".glass-color", session.getGlassColor() != null ? session.getGlassColor().name() : Material.STAINED_GLASS_PANE);
        dataFile.set(session.getUuid() + ".notes", new ArrayList<>(session.getPlayerNotes()));
        dataFile.set(session.getUuid() + ".alert-options", alertOptions());
        dataFile.set(session.getUuid() + ".vanish-type", session.getVanishType() != null ? session.getVanishType().name() : VanishType.NONE.name());
        dataFile.set(session.getUuid() + ".staff-mode", session.isInStaffMode());
    }


    private List<String> alertOptions() {
        return Arrays.stream(AlertType.values())
            .map(alertType -> alertType.name() + ";" + session.shouldNotify(alertType))
            .collect(Collectors.toList());
    }
}