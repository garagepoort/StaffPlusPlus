package net.shortninja.staffplus.domain.actions;

import net.shortninja.staffplus.domain.player.SppPlayer;

public interface ActionFilter {

    boolean isValidAction(SppPlayer target, ConfiguredAction configuredAction);
}
