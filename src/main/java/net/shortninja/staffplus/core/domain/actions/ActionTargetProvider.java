package net.shortninja.staffplus.core.domain.actions;

import net.shortninja.staffplus.core.domain.player.SppPlayer;

public interface ActionTargetProvider {

    SppPlayer getTarget(ConfiguredAction configuredAction);
}
