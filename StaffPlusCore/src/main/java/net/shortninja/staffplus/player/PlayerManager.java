package net.shortninja.staffplus.player;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerManager {

    private final OfflinePlayerProvider offlinePlayerProvider;
    private final Set<String> cachedPlayerNames;
    private final Set<SppPlayer> cachedSppPlayers;

    public PlayerManager(OfflinePlayerProvider offlinePlayerProvider) {
        this.offlinePlayerProvider = offlinePlayerProvider;
        Set<String> playerNames = new HashSet<>();
        Set<SppPlayer> sppPlayers = new HashSet<>();
        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            String name = offlinePlayer.getName();
            playerNames.add(name);
            sppPlayers.add(new SppPlayer(offlinePlayer.getUniqueId(), offlinePlayer.getName()));
        }
        cachedPlayerNames = playerNames;
        cachedSppPlayers = sppPlayers;
    }

    public Optional<SppPlayer> getOfflinePlayer(UUID playerUuid) {
        return offlinePlayerProvider.findUser(playerUuid);
    }

    public Optional<SppPlayer> getOnOrOfflinePlayer(String playerName) {
        Player player = Bukkit.getPlayerExact(playerName);
        if (player == null) {
            return offlinePlayerProvider.findUser(playerName);
        }
        return Optional.of(new SppPlayer(player.getUniqueId(), playerName, player));
    }

    public Set<SppPlayer> getOnAndOfflinePlayers() {
        return cachedSppPlayers;
    }


    public Optional<SppPlayer> getOnOrOfflinePlayer(UUID playerUuid) {
        Player player = Bukkit.getPlayer(playerUuid);
        if (player == null) {
            return offlinePlayerProvider.findUser(playerUuid);
        }
        return Optional.of(new SppPlayer(player.getUniqueId(), player.getName(), player));
    }

    public Optional<SppPlayer> getOnlinePlayer(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return Optional.empty();
        }
        return Optional.of(new SppPlayer(player.getUniqueId(), player.getName(), player));
    }

    public Set<String> getAllPlayerNames() {
        return cachedPlayerNames;
    }

    public void syncPlayer(Player player) {
        cachedPlayerNames.add(player.getName());
        cachedSppPlayers.add(new SppPlayer(player.getUniqueId(), player.getName()));
    }
}
