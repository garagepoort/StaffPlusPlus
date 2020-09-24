package net.shortninja.staffplus.player.ext.bukkit;

import net.shortninja.staffplus.player.OfflinePlayerProvider;
import net.shortninja.staffplus.player.SppPlayer;

import java.util.Optional;
import java.util.UUID;

public class NoopOfflinePlayerProvider implements OfflinePlayerProvider {

    @Override
    public Optional<SppPlayer> findUser(String username) {
        return Optional.empty();
    }

    @Override
    public Optional<SppPlayer> findUser(UUID playerUuid) {
        return Optional.empty();
    }
}
