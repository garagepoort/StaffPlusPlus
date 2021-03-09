package net.shortninja.staffplus.staff.reporting;

import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplusplus.reports.ReportStatus;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ReportFilters {

    private List<ReportFilter> reportFilters;

    private ReportFilters(List<ReportFilter> reportFilters) {
        this.reportFilters = reportFilters;
    }

    public List<ReportFilter> getReportFilters() {
        return reportFilters;
    }

    public static class ReportFiltersBuilder {

        private List<ReportFilter> reportFilters = new ArrayList<>();

        public ReportFiltersBuilder id(int id) {
            this.reportFilters.add(new ReportFilter<>(id, Types.INTEGER, "sp_reports.id"));
            return this;
        }
        
        public ReportFiltersBuilder reportStatus(ReportStatus reportStatus) {
            this.reportFilters.add(new ReportFilter<>(reportStatus.name(), Types.VARCHAR, "status"));
            return this;
        }

        public ReportFiltersBuilder culprit(SppPlayer culprit) {
            this.reportFilters.add(new ReportFilter<>(culprit.getId().toString(), Types.VARCHAR, "player_uuid"));
            return this;
        }

        public ReportFiltersBuilder reporter(SppPlayer reporter) {
            this.reportFilters.add(new ReportFilter<>(reporter.getId().toString(), Types.VARCHAR, "reporter_uuid"));
            return this;
        }

        public ReportFiltersBuilder assignee(SppPlayer assignee) {
            this.reportFilters.add(new ReportFilter<>(assignee.getId().toString(), Types.VARCHAR, "staff_uuid"));
            return this;
        }

        public ReportFiltersBuilder type(String type) {
            this.reportFilters.add(new ReportFilter<>(type, Types.VARCHAR, "type"));
            return this;
        }

        public ReportFilters build() {
            return new ReportFilters(reportFilters);
        }
    }
}
