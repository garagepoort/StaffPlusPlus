package net.shortninja.staffplus.core.domain.player;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.domain.player.providers.OfflinePlayerProvider;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

@IocBean
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
            if (StringUtils.isNotEmpty(name)) {
                playerNames.add(name);
                sppPlayers.add(new SppPlayer(offlinePlayer.getUniqueId(), offlinePlayer.getName()));
            }
        }
        cachedPlayerNames = playerNames;
        cachedSppPlayers = sppPlayers;
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

        List<SppPlayer> existingCache = cachedSppPlayers.stream().filter(p -> p.getId().equals(player.getUniqueId())).collect(Collectors.toList());
        cachedSppPlayers.removeAll(existingCache);
        cachedSppPlayers.add(new SppPlayer(player.getUniqueId(), player.getName()));
    }

    public Collection<? extends Player> getOnlinePlayers() {
        return Bukkit.getOnlinePlayers();
    }

    public Collection<OfflinePlayer> getAllPLayers() {
        return Arrays.asList(Bukkit.getOfflinePlayers());
    }
}
