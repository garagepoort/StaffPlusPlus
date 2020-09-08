package net.shortninja.staffplus.player;

import java.util.Optional;

public interface OfflinePlayerProvider {

    Optional<ProvidedPlayer> findUser(String username);
}
