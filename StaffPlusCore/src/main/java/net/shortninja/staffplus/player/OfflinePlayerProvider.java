package net.shortninja.staffplus.player;

import java.util.Optional;
import java.util.UUID;

public interface OfflinePlayerProvider {

    Optional<SppPlayer> findUser(String username);

    Optional<SppPlayer> findUser(UUID playerUuid);
}
