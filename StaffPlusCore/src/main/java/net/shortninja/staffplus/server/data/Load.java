package net.shortninja.staffplus.server.data;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.User;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.player.attribute.infraction.Report;
import net.shortninja.staffplus.player.attribute.infraction.Warning;
import net.shortninja.staffplus.server.AlertCoordinator;
import net.shortninja.staffplus.unordered.AlertType;
import net.shortninja.staffplus.unordered.IReport;
import net.shortninja.staffplus.unordered.IWarning;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

//import net.shortninja.staffplus.player.attribute.SecurityHandler;

public class Load {
    private FileConfiguration dataFile = StaffPlus.get().dataFile.getConfiguration();
    private UserManager userManager = StaffPlus.get().userManager;
//    private SecurityHandler securityHandler = StaffPlus.get().securityHandler;
    private AlertCoordinator alertCoordinator = StaffPlus.get().alertCoordinator;
    private String name;
    private UUID uuid;
    private String uuidString;
    private String prefix;

    public Load(Player player) {
        User user = null;

        this.name = player.getName();
        this.uuid = player.getUniqueId();
        this.uuidString = uuid.toString();
        this.prefix = uuidString + ".";

        if (dataFile.contains(uuidString)) {
            user = loadUser();
        } else user = new User(uuid, name);

        userManager.add(user);
    }

    private User loadUser() {
        String name = dataFile.getString(prefix + "name");
        String password = dataFile.getString(prefix + "password");
        short glassColor = (short) dataFile.getInt(prefix + "glass-color");
        List<IReport> reports = loadReports();
        List<IWarning> warnings = loadWarnings();
        List<String> playerNotes = loadPlayerNotes();
        Map<AlertType, Boolean> alertOptions = loadAlertOptions();

//        if (password != null && !password.isEmpty()) {
//            securityHandler.setPassword(uuid, password, false);
//        }

        if (!this.name.equals(name)) {
            alertCoordinator.onNameChange(name, this.name);
        }

        return new User(uuid, name, glassColor, reports, warnings, playerNotes, alertOptions);
    }

    private List<IReport> loadReports() {
        List<IReport> reports = new ArrayList<>();

        for (String string : dataFile.getStringList(prefix + "reports")) {
            String[] parts = string.split(";");
            UUID reporterUuid = UUID.fromString(parts[2]);
            String offlineName = getOfflineName(reporterUuid);
            String reporterName = offlineName == null ? parts[1] : offlineName;

            reports.add(new Report(uuid, name, parts[0], reporterName, reporterUuid));
        }

        return reports;
    }

    private List<IWarning> loadWarnings() {
        List<IWarning> warnings = new ArrayList<>();

        for (String string : dataFile.getStringList(prefix + "warnings")) {
            String[] parts = string.split(";");
            UUID issuerUuid = UUID.fromString(parts[2]);
            String offlineName = getOfflineName(issuerUuid);
            String issuerName = offlineName == null ? parts[1] : offlineName;

            warnings.add(new Warning(uuid, name, parts[0], issuerName, issuerUuid, Long.valueOf(parts[3])));
        }

        return warnings;
    }

    private Map<AlertType, Boolean> loadAlertOptions() {
        Map<AlertType, Boolean> alertOptions = new HashMap<AlertType, Boolean>();

        for (String string : dataFile.getStringList(prefix + "alert-options")) {
            String[] parts = string.split(";");

            alertOptions.put(AlertType.valueOf(parts[0]), Boolean.valueOf(parts[1]));
        }

        return alertOptions;
    }

    private List<String> loadPlayerNotes() {
        List<String> playerNotes = new ArrayList<String>();

        for (String string : dataFile.getStringList(prefix + "notes")) {
            if (string.contains("&7")) {
                continue;
            }

            playerNotes.add("&7" + string);
        }

        return playerNotes;
    }

    private String getOfflineName(UUID uuid) {
        return Bukkit.getOfflinePlayer(uuid).getName();
    }
}