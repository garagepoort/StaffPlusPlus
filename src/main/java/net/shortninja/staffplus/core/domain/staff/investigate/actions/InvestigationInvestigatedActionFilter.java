package net.shortninja.staffplus.core.domain.staff.investigate.actions;

import net.shortninja.staffplus.core.common.exceptions.ConfigurationException;
import net.shortninja.staffplus.core.domain.actions.ActionFilter;
import net.shortninja.staffplus.core.domain.actions.CreateStoredCommandRequest;
import net.shortninja.staffplusplus.investigate.IInvestigation;

import java.util.Map;

public class InvestigationInvestigatedActionFilter implements ActionFilter {

    private static final String HAS_INVESTIGATED = "hasinvestigated";
    private final IInvestigation investigation;

    public InvestigationInvestigatedActionFilter(IInvestigation investigation) {
        this.investigation = investigation;
    }

    @Override
    public boolean isValidAction(CreateStoredCommandRequest createStoredCommandRequest, Map<String, String> filters) {
        if (!filters.containsKey(HAS_INVESTIGATED)) {
            return true;
        }

        String value = filters.get(InvestigationInvestigatedActionFilter.HAS_INVESTIGATED);
        if (value.equalsIgnoreCase("true")) {
            return investigation.getInvestigatedUuid().isPresent();
        }
        if (value.equalsIgnoreCase("false")) {
            return !investigation.getInvestigatedUuid().isPresent();
        }
        throw new ConfigurationException("Invalid configuration for investigation commands. [hasinvestigated] filter has invalid value [" + value + "]");
    }
}
