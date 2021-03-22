package net.shortninja.staffplus.core.domain.actions;

import net.shortninja.staffplus.core.domain.player.SppPlayer;

public interface ActionFilter {

    boolean isValidAction(SppPlayer target, ConfiguredAction configuredAction);
}
