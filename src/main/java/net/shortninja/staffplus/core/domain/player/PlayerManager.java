package net.shortninja.staffplus.core.domain.player;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import com.google.common.collect.Sets;
import net.shortninja.staffplus.core.domain.player.database.PlayerRepository;
import net.shortninja.staffplus.core.domain.player.database.StoredPlayer;
import net.shortninja.staffplus.core.domain.player.providers.OfflinePlayerProvider;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@IocBean
public class PlayerManager {

    @ConfigProperty("server-name")
    private String serverName;

    private final OfflinePlayerProvider offlinePlayerProvider;
    private final Set<String> cachedPlayerNames;
    private final Set<SppPlayer> cachedSppPlayers;
    private final PlayerRepository playerRepository;

    public PlayerManager(OfflinePlayerProvider offlinePlayerProvider, PlayerRepository playerRepository) {
        this.offlinePlayerProvider = offlinePlayerProvider;
        this.playerRepository = playerRepository;
        Set<String> playerNames = new HashSet<>();
        Set<SppPlayer> sppPlayers = new HashSet<>();
        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            String name = offlinePlayer.getName();
            if (StringUtils.isNotEmpty(name)) {
                playerNames.add(name);
                sppPlayers.add(new SppPlayer(offlinePlayer.getUniqueId(), offlinePlayer.getName(), offlinePlayer));
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
        return new HashSet<>(cachedSppPlayers);
    }

    public List<SppPlayer> getOfflinePlayers() {
        return cachedSppPlayers.stream()
            .filter(p -> !p.isOnline())
            .collect(Collectors.toList());
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

    public Optional<SppPlayer> getOnlinePlayer(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            return Optional.empty();
        }
        return Optional.of(new SppPlayer(player.getUniqueId(), player.getName(), player));
    }

    public Set<String> getAllPlayerNames() {
        return new HashSet<>(cachedPlayerNames);
    }

    public void syncPlayer(Player player) {
        cachedPlayerNames.add(player.getName());

        List<SppPlayer> existingCache = cachedSppPlayers.stream().filter(p -> p.getId().equals(player.getUniqueId())).collect(Collectors.toList());
        cachedSppPlayers.removeAll(existingCache);
        cachedSppPlayers.add(new SppPlayer(player.getUniqueId(), player.getName(), player));
    }

    public void storePlayer(Player player) {
        Optional<StoredPlayer> storedPlayer = playerRepository.findPlayer(player.getUniqueId());
        if (storedPlayer.isPresent()) {
            if (!storedPlayer.get().getServers().contains(serverName)) {
                storedPlayer.get().addServer(serverName);
                playerRepository.update(storedPlayer.get());
            }
        } else {
            playerRepository.save(new StoredPlayer(player.getUniqueId(), player.getName(), Sets.newHashSet(serverName)));
        }
    }

    public Collection<? extends Player> getOnlinePlayers() {
        return Bukkit.getOnlinePlayers();
    }

    public List<SppPlayer> getOnlineSppPlayers() {
        return Bukkit.getOnlinePlayers().stream().map(p -> new SppPlayer(p.getUniqueId(), p.getName(), p)).collect(Collectors.toList());
    }
}
