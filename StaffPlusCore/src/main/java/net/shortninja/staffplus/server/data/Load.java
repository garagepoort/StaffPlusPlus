package net.shortninja.staffplus.server.data;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.User;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.server.AlertCoordinator;
import net.shortninja.staffplus.unordered.AlertType;
import net.shortninja.staffplus.unordered.IWarning;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

//import net.shortninja.staffplus.player.attribute.SecurityHandler;

public class Load {
    private FileConfiguration dataFile = StaffPlus.get().dataFile.getConfiguration();
    private UserManager userManager = IocContainer.getUserManager();
    //private SecurityHandler securityHandler = StaffPlus.get().securityHandler;
    private AlertCoordinator alertCoordinator = StaffPlus.get().alertCoordinator;

    public User load(Player player) {
        User user;
        if (dataFile.contains(player.getUniqueId().toString())) {
            user = loadUser(player.getName(), player.getUniqueId());
        } else user = new User(player.getUniqueId(), player.getName());

        userManager.add(user);
        return user;
    }

    public User build(UUID playerUuid, String name) {
        return dataFile.contains(playerUuid.toString()) ? loadUser(name, playerUuid) : new User(playerUuid, name);
    }

    public User getUser(String playerName, UUID uuid) {

        String name = dataFile.getString(uuid + ".name");
        String password = dataFile.getString(uuid + ".password");
        short glassColor = (short) dataFile.getInt(uuid + ".glass-color");
        List<IWarning> warnings = loadWarnings(uuid);
        List<String> playerNotes = loadPlayerNotes(uuid);
        Map<AlertType, Boolean> alertOptions = loadAlertOptions(uuid);

//        if (password != null && !password.isEmpty()) {
//            securityHandler.setPassword(uuid, password, false);
//        }

        if (!playerName.equals(name)) {
            alertCoordinator.onNameChange(name, playerName);
        }

        return new User(uuid, name, glassColor, warnings, playerNotes, alertOptions);
    }


    private User loadUser(String playerName, UUID uuid) {
        String name = dataFile.getString(uuid + ".name");
        String password = dataFile.getString(uuid + ".password");
        short glassColor = (short) dataFile.getInt(uuid + ".glass-color");
        List<IWarning> warnings = loadWarnings(uuid);
        List<String> playerNotes = loadPlayerNotes(uuid);
        Map<AlertType, Boolean> alertOptions = loadAlertOptions(uuid);

//        if (password != null && !password.isEmpty()) {
//            securityHandler.setPassword(uuid, password, false);
//        }

        if (!playerName.equals(name)) {
            alertCoordinator.onNameChange(name,playerName);
        }

        return new User(uuid, name, glassColor, warnings, playerNotes, alertOptions);
    }

    private List<IWarning> loadWarnings(UUID uuid) {
        /*List<IWarning> warnings = new ArrayList<>();

        for (String string : dataFile.getStringList(prefix + "warnings")) {

            String[] parts = string.split(";");
            UUID issuerUuid = UUID.fromString(parts[2]);
            String offlineName = getOfflineName(issuerUuid);
            String issuerName = offlineName == null ? parts[1] : offlineName;

            warnings.add(new Warning(uuid, name, parts[0], issuerName, issuerUuid, Long.valueOf(parts[3])));

        }

        return warnings;

        }*/

        if (IocContainer.getStorage().getWarnings(uuid) == null)
            return new ArrayList<IWarning>();
        else
            return IocContainer.getStorage().getWarnings(uuid);
    }

    private Map<AlertType, Boolean> loadAlertOptions(UUID uuid) {
        Map<AlertType, Boolean> alertOptions = new HashMap<AlertType, Boolean>();

        for (String string : dataFile.getStringList(uuid + ".alert-options")) {
            String[] parts = string.split(";");

            alertOptions.put(AlertType.valueOf(parts[0]), Boolean.valueOf(parts[1]));
        }

        return alertOptions;
    }

    private List<String> loadPlayerNotes(UUID uuid) {
        List<String> playerNotes = new ArrayList<String>();

        for (String string : dataFile.getStringList(uuid + ".notes")) {
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