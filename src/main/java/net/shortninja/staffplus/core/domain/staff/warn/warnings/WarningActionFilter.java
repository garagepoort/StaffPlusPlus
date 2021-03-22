package net.shortninja.staffplus.core.domain.staff.warn.warnings;

import net.shortninja.staffplus.core.domain.actions.ActionFilter;
import net.shortninja.staffplus.core.domain.actions.ConfiguredAction;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplusplus.warnings.IWarning;

import java.util.Arrays;

public class WarningActionFilter implements ActionFilter {

    private static final String SEVERITY = "severity";
    private static final String CONTEXT = "context";
    private final String context;
    private IWarning warning;

    public WarningActionFilter(IWarning warning, String context) {
        this.warning = warning;
        this.context = context;
    }

    @Override
    public boolean isValidAction(SppPlayer target, ConfiguredAction configuredAction) {
        return checkFilter(configuredAction, SEVERITY, warning.getSeverity()) && checkFilter(configuredAction, CONTEXT, context);
    }

    private boolean checkFilter(ConfiguredAction configuredAction, String filter, String value) {
        if (configuredAction.getFilters().containsKey(filter)) {
            return Arrays.asList(configuredAction.getFilters().get(filter).split(",")).contains(value.toLowerCase());
        }
        return true;
    }

}
