package net.shortninja.staffplus.core.domain.actions;

import net.shortninja.staffplusplus.session.SppPlayer;

public interface ActionFilter {

    boolean isValidAction(SppPlayer target, ConfiguredAction configuredAction);
}
