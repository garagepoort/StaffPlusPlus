package net.shortninja.staffplus.core.domain.player.providers;

import net.shortninja.staffplus.core.application.IocBean;
import net.shortninja.staffplus.core.domain.player.SppPlayer;

import java.util.Optional;
import java.util.UUID;

@IocBean(conditionalOnProperty = "offline-players-mode=false")
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
