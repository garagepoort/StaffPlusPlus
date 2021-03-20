package net.shortninja.staffplus.domain.player.providers;

import net.shortninja.staffplus.domain.player.SppPlayer;

import java.util.Optional;
import java.util.UUID;

public interface OfflinePlayerProvider {

    Optional<SppPlayer> findUser(String username);

    Optional<SppPlayer> findUser(UUID playerUuid);
}
