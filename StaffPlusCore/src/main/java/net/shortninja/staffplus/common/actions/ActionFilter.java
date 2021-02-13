package net.shortninja.staffplus.common.actions;

import net.shortninja.staffplus.player.SppPlayer;

public interface ActionFilter {

    boolean isValidAction(SppPlayer target, ConfiguredAction configuredAction);
}
