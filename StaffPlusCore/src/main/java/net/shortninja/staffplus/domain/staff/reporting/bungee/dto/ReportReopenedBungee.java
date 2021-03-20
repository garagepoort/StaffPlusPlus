package net.shortninja.staffplus.domain.staff.reporting.bungee.dto;

import net.shortninja.staffplus.domain.staff.reporting.Report;

public class ReportReopenedBungee {

    private ReportBungee report;
    private String reopenByName;

    public ReportReopenedBungee(Report report, String reopenByName) {
        this.report = new ReportBungee(report);
        this.reopenByName = reopenByName;
    }

    public ReportBungee getReport() {
        return report;
    }

    public String getReopenByName() {
        return reopenByName;
    }
}
