package net.shortninja.staffplus.core.domain.player.providers;

import net.shortninja.staffplusplus.session.SppPlayer;

import java.util.Optional;
import java.util.UUID;

public interface OfflinePlayerProvider {

    Optional<SppPlayer> findUser(String username);

    Optional<SppPlayer> findUser(UUID playerUuid);
}
