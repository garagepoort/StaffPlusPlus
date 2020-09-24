package net.shortninja.staffplus.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public class PlayerManager {

    private final OfflinePlayerProvider offlinePlayerProvider;

    public PlayerManager(OfflinePlayerProvider offlinePlayerProvider) {
        this.offlinePlayerProvider = offlinePlayerProvider;
    }

    public Optional<SppPlayer> getOfflinePlayer(UUID playerUuid) {
        return offlinePlayerProvider.findUser(playerUuid);
    }

    public Optional<SppPlayer> getOnOrOfflinePlayer(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            return offlinePlayerProvider.findUser(playerName);
        }
        return Optional.of(new SppPlayer(player.getUniqueId(), playerName, player));
    }


    public Optional<SppPlayer> getOnOrOfflinePlayer(UUID playerUuid) {
        Player player = Bukkit.getPlayer(playerUuid);
        if (player == null) {
            return offlinePlayerProvider.findUser(playerUuid);
        }
        return Optional.of(new SppPlayer(player.getUniqueId(), player.getName(), player));
    }

    public Optional<SppPlayer> getOnlinePlayer(String tracedPlayerName) {
        Player player = Bukkit.getPlayer(tracedPlayerName);
        if(player == null) {
            return Optional.empty();
        }
        return Optional.of(new SppPlayer(player.getUniqueId(), player.getName(), player));
    }

    public Optional<SppPlayer> getOnlinePlayer(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if(player == null) {
            return Optional.empty();
        }
        return Optional.of(new SppPlayer(player.getUniqueId(), player.getName(), player));
    }
}
