package net.shortninja.staffplus.core.domain.staff.reporting.actions;

import net.shortninja.staffplus.core.domain.actions.ActionTargetProvider;
import net.shortninja.staffplus.core.domain.actions.ConfiguredAction;
import net.shortninja.staffplusplus.session.SppPlayer;

import java.util.Optional;

public class ReportActionTargetProvider implements ActionTargetProvider {
    private static final String REPORTER_IDENTIFIER = "reporter";
    private static final String ASSIGNED_IDENTIFIER = "assigned";
    private static final String CULPRIT_IDENTIFIER = "culprit";

    private final SppPlayer reporter;
    private final SppPlayer assigned;
    private final SppPlayer culprit;

    public ReportActionTargetProvider(SppPlayer reporter, SppPlayer assigned, SppPlayer culprit) {
        this.reporter = reporter;
        this.assigned = assigned;
        this.culprit = culprit;
    }

    @Override
    public Optional<SppPlayer> getTarget(ConfiguredAction configuredAction) {
        if (configuredAction.getTarget().equalsIgnoreCase(REPORTER_IDENTIFIER)) {
            return Optional.ofNullable(reporter);
        }
        if (configuredAction.getTarget().equalsIgnoreCase(CULPRIT_IDENTIFIER)) {
            return Optional.ofNullable(culprit);
        }
        if (configuredAction.getTarget().equalsIgnoreCase(ASSIGNED_IDENTIFIER)) {
            return Optional.ofNullable(assigned);
        }
        return assigned != null ? Optional.of(assigned) : Optional.ofNullable(reporter);
    }
}
