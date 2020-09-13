package net.shortninja.staffplus.player;

import java.util.Optional;
import java.util.UUID;

public interface OfflinePlayerProvider {

    Optional<ProvidedPlayer> findUser(String username);

    Optional<ProvidedPlayer> findUser(UUID playerUuid);
}
