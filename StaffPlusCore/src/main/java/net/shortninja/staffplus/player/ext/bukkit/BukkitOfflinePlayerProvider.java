package net.shortninja.staffplus.player.ext.bukkit;

import net.shortninja.staffplus.player.OfflinePlayerProvider;
import net.shortninja.staffplus.player.SppPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Optional;
import java.util.UUID;

public class BukkitOfflinePlayerProvider implements OfflinePlayerProvider {

    @Override
    public Optional<SppPlayer> findUser(String username) {
        OfflinePlayer playerExact = Bukkit.getOfflinePlayer(username);
        if(!playerExact.hasPlayedBefore()) {
            return Optional.empty();
        }

        return Optional.of(new SppPlayer(playerExact.getUniqueId(), playerExact.getName()));
    }

    @Override
    public Optional<SppPlayer> findUser(UUID playerUuid) {
        OfflinePlayer playerExact = Bukkit.getOfflinePlayer(playerUuid);
        if(!playerExact.hasPlayedBefore()) {
            return Optional.empty();
        }

        return Optional.of(new SppPlayer(playerExact.getUniqueId(), playerExact.getName()));
    }
}
