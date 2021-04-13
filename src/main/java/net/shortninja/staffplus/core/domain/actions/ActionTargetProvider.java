package net.shortninja.staffplus.core.domain.actions;

import net.shortninja.staffplusplus.session.SppPlayer;

import java.util.Optional;

public interface ActionTargetProvider {

    Optional<SppPlayer> getTarget(ConfiguredAction configuredAction);
}
