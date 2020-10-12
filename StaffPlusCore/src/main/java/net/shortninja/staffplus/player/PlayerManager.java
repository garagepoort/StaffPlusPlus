package net.shortninja.staffplus.player;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerManager {

    private final OfflinePlayerProvider offlinePlayerProvider;
    private final Set<String> cachedPlayerNames;

    public PlayerManager(OfflinePlayerProvider offlinePlayerProvider) {
        this.offlinePlayerProvider = offlinePlayerProvider;
        cachedPlayerNames = Arrays.stream(Bukkit.getOfflinePlayers()).map(OfflinePlayer::getName).collect(Collectors.toSet());
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

    public List<SppPlayer> getOnAndOfflinePlayers() {
        List<SppPlayer> offlinePlayers = Arrays.stream(Bukkit.getOfflinePlayers())
            .map(p -> new SppPlayer(p.getUniqueId(), p.getName()))
            .collect(Collectors.toList());
        return offlinePlayers;
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

    public void syncPlayerName(String name) {
        cachedPlayerNames.add(name);
    }
}
