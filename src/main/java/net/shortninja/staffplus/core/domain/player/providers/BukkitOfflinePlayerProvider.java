package net.shortninja.staffplus.core.domain.player.providers;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Optional;
import java.util.UUID;

@IocBean(conditionalOnProperty = "offline-players-mode=true")
public class BukkitOfflinePlayerProvider implements OfflinePlayerProvider {

    @Override
    public Optional<SppPlayer> findUser(String username) {
        OfflinePlayer playerExact = Bukkit.getOfflinePlayer(username);
        if(!playerExact.hasPlayedBefore()) {
            return Optional.empty();
        }

        return Optional.of(new SppPlayer(playerExact.getUniqueId(), playerExact.getName(), playerExact));
    }

    @Override
    public Optional<SppPlayer> findUser(UUID playerUuid) {
        OfflinePlayer playerExact = Bukkit.getOfflinePlayer(playerUuid);
        if(!playerExact.hasPlayedBefore()) {
            return Optional.empty();
        }

        return Optional.of(new SppPlayer(playerExact.getUniqueId(), playerExact.getName(), playerExact));
    }
}
