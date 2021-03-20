package net.shortninja.staffplus.domain.staff.reporting.bungee.dto;

import net.shortninja.staffplus.domain.staff.reporting.Report;

public class ReportDeletedBungee {

    private ReportBungee report;
    private String deletedByName;

    public ReportDeletedBungee(Report report, String deletedByName) {
        this.report = new ReportBungee(report);
        this.deletedByName = deletedByName;
    }

    public ReportBungee getReport() {
        return report;
    }

    public String getDeletedByName() {
        return deletedByName;
    }
}
