package net.shortninja.staffplus.core.domain.staff.warn.warnings;

import net.shortninja.staffplus.core.domain.actions.ActionFilter;
import net.shortninja.staffplus.core.domain.actions.CreateStoredCommandRequest;
import net.shortninja.staffplusplus.warnings.IWarning;

import java.util.Arrays;
import java.util.Map;

public class WarningActionFilter implements ActionFilter {

    private static final String SEVERITY = "severity";
    private static final String CONTEXT = "context";
    private final String context;
    private final IWarning warning;

    public WarningActionFilter(IWarning warning, String context) {
        this.warning = warning;
        this.context = context;
    }

    @Override
    public boolean isValidAction(CreateStoredCommandRequest createStoredCommandRequest, Map<String, String> filters) {
        return checkFilter(filters, SEVERITY, warning.getSeverity()) && checkFilter(filters, CONTEXT, context);
    }

    private boolean checkFilter(Map<String, String> filters, String filter, String value) {
        if (filters.containsKey(filter)) {
            return Arrays.asList(filters.get(filter).split(",")).contains(value.toLowerCase());
        }
        return true;
    }

}
