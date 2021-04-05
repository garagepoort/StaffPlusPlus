package net.shortninja.staffplus.core.domain.actions;

import net.shortninja.staffplusplus.session.SppPlayer;

public interface ActionTargetProvider {

    SppPlayer getTarget(ConfiguredAction configuredAction);
}
