package net.shortninja.staffplus.core.domain.staff.reporting.actions;

import net.shortninja.staffplus.core.domain.actions.ActionTargetProvider;
import net.shortninja.staffplus.core.domain.actions.ConfiguredCommand;
import net.shortninja.staffplusplus.session.SppPlayer;

import java.util.Optional;

public class ReportActionTargetExecutionerProvider implements ActionTargetProvider {
    private static final String REPORTER_IDENTIFIER = "reporter";
    private static final String ASSIGNED_IDENTIFIER = "assigned";
    private static final String CULPRIT_IDENTIFIER = "culprit";

    private final SppPlayer reporter;
    private final SppPlayer assigned;
    private final SppPlayer culprit;

    public ReportActionTargetExecutionerProvider(SppPlayer reporter, SppPlayer assigned, SppPlayer culprit) {
        this.reporter = reporter;
        this.assigned = assigned;
        this.culprit = culprit;
    }

    @Override
    public Optional<SppPlayer> getTarget(ConfiguredCommand configuredCommand) {
        if(!configuredCommand.getTarget().isPresent()) {
            return Optional.empty();
        }
        if (configuredCommand.getTarget().get().equalsIgnoreCase(REPORTER_IDENTIFIER)) {
            return Optional.ofNullable(reporter);
        }
        if (configuredCommand.getTarget().get().equalsIgnoreCase(CULPRIT_IDENTIFIER)) {
            return Optional.ofNullable(culprit);
        }
        if (configuredCommand.getTarget().get().equalsIgnoreCase(ASSIGNED_IDENTIFIER)) {
            return Optional.ofNullable(assigned);
        }
        return Optional.empty();
    }
}
