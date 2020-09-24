package net.shortninja.staffplus.session;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.NodeUser;
import net.shortninja.staffplus.player.PlayerManager;
import net.shortninja.staffplus.player.PlayerSession;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.data.Save;
import net.shortninja.staffplus.unordered.AlertType;
import net.shortninja.staffplus.unordered.IPlayerSession;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

import static org.bukkit.Bukkit.getScheduler;

public class SessionLoader {
    private final FileConfiguration dataFile = StaffPlus.get().dataFile.getConfiguration();
    private final PlayerManager playerManager;

    public SessionLoader(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    PlayerSession loadSession(Player player) {
        return loadSession(player.getUniqueId());
    }

    PlayerSession loadSession(UUID playerUuid) {
        return dataFile.contains(playerUuid.toString()) ? buildKnownSession(playerUuid) : buildNewSession(playerUuid);
    }

    private PlayerSession buildNewSession(UUID uuid) {
        Optional<SppPlayer> providedPlayer = playerManager.getOnlinePlayer(uuid);
        if(!providedPlayer.isPresent()) {
            throw new RuntimeException("Trying to instantiate session for offline user");
        }
        return new PlayerSession(uuid, providedPlayer.get().getUsername());
    }

    private PlayerSession buildKnownSession(UUID uuid) {
        String name = dataFile.getString(uuid + ".name");
        short glassColor = (short) dataFile.getInt(uuid + ".glass-color");
        List<String> playerNotes = loadPlayerNotes(uuid);
        Map<AlertType, Boolean> alertOptions = loadAlertOptions(uuid);
        return new PlayerSession(uuid, name, glassColor, playerNotes, alertOptions);
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

    public void saveSession(IPlayerSession playerSession) {
        getScheduler().runTaskAsynchronously(StaffPlus.get(), () -> {
            new Save(new NodeUser(playerSession));
            StaffPlus.get().dataFile.save();
        });
    }
}