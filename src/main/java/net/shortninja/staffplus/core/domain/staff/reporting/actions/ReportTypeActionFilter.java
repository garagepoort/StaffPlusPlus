package net.shortninja.staffplus.core.domain.staff.reporting.actions;

import net.shortninja.staffplus.core.domain.actions.ActionFilter;
import net.shortninja.staffplus.core.domain.actions.CreateStoredCommandRequest;
import net.shortninja.staffplusplus.reports.IReport;

import java.util.Arrays;
import java.util.Map;

public class ReportTypeActionFilter implements ActionFilter {

    private static final String TYPE = "type";
    private final IReport report;

    public ReportTypeActionFilter(IReport report) {
        this.report = report;
    }

    @Override
    public boolean isValidAction(CreateStoredCommandRequest createStoredCommandRequest, Map<String, String> filters) {
        if (!filters.containsKey(TYPE)) {
            return true;
        }
        if (!report.getReportType().isPresent()) {
            return false;
        }

        return checkFilter(filters, report.getReportType().get());
    }

    private boolean checkFilter(Map<String, String> filters, String value) {
        return Arrays.asList(filters.get(ReportTypeActionFilter.TYPE).split(",")).contains(value.toLowerCase());
    }
}
