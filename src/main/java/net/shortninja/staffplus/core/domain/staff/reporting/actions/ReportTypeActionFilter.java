package net.shortninja.staffplus.core.domain.staff.reporting.actions;

import net.shortninja.staffplus.core.domain.actions.ActionFilter;
import net.shortninja.staffplus.core.domain.actions.ConfiguredAction;
import net.shortninja.staffplusplus.reports.IReport;
import net.shortninja.staffplusplus.session.SppPlayer;

import java.util.Arrays;

public class ReportTypeActionFilter implements ActionFilter {

    private static final String TYPE = "type";
    private final IReport report;

    public ReportTypeActionFilter(IReport report) {
        this.report = report;
    }

    @Override
    public boolean isValidAction(SppPlayer target, ConfiguredAction configuredAction) {
        if (!configuredAction.getFilters().containsKey(TYPE)) {
            return true;
        }
        if (!report.getReportType().isPresent()) {
            return false;
        }

        return checkFilter(configuredAction, report.getReportType().get());
    }

    private boolean checkFilter(ConfiguredAction configuredAction, String value) {
        return Arrays.asList(configuredAction.getFilters().get(ReportTypeActionFilter.TYPE).split(",")).contains(value.toLowerCase());
    }
}
