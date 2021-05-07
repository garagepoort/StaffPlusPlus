package net.shortninja.staffplus.core.domain.staff.reporting.actions;

import net.shortninja.staffplus.core.common.exceptions.ConfigurationException;
import net.shortninja.staffplus.core.domain.actions.ActionFilter;
import net.shortninja.staffplus.core.domain.actions.ConfiguredAction;
import net.shortninja.staffplusplus.reports.IReport;
import net.shortninja.staffplusplus.session.SppPlayer;

public class ReportCulpritActionFilter implements ActionFilter {

    private static final String HAS_CULPRIT = "hasculprit";
    private final IReport report;

    public ReportCulpritActionFilter(IReport report) {
        this.report = report;
    }

    @Override
    public boolean isValidAction(SppPlayer target, ConfiguredAction configuredAction) {
        if (!configuredAction.getFilters().containsKey(HAS_CULPRIT)) {
            return true;
        }

        String value = configuredAction.getFilters().get(ReportCulpritActionFilter.HAS_CULPRIT);
        if (value.equalsIgnoreCase("true")) {
            return report.getCulpritUuid() != null;
        }
        if (value.equalsIgnoreCase("false")) {
            return report.getCulpritUuid() == null;
        }
        throw new ConfigurationException("Invalid configuration for report commands. [hasculprit] filter has invalid value [" + value + "]");
    }
}
