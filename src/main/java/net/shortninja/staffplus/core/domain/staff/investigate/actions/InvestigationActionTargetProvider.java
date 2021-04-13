package net.shortninja.staffplus.core.domain.staff.investigate.actions;

import net.shortninja.staffplus.core.domain.actions.ActionTargetProvider;
import net.shortninja.staffplus.core.domain.actions.ConfiguredAction;
import net.shortninja.staffplusplus.session.SppPlayer;

import java.util.Optional;

public class InvestigationActionTargetProvider implements ActionTargetProvider {
    private static final String INVESTIGATED_IDENTIFIER = "investigated";

    private final SppPlayer investigator;
    private final SppPlayer investigated;

    public InvestigationActionTargetProvider(SppPlayer investigator, SppPlayer investigated) {
        this.investigator = investigator;
        this.investigated = investigated;
    }

    @Override
    public Optional<SppPlayer> getTarget(ConfiguredAction configuredAction) {
        if(configuredAction.getTarget().equalsIgnoreCase(INVESTIGATED_IDENTIFIER)) {
            return Optional.ofNullable(investigated);
        }
        return Optional.ofNullable(investigator);
    }
}
