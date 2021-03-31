package net.shortninja.staffplus.core.domain.staff.reporting.bungee.dto;

import net.shortninja.staffplus.core.domain.staff.reporting.Report;

public class ReportDeletedBungee extends ReportBungee {

    private String deletedByName;

    public ReportDeletedBungee(Report report, String deletedByName) {
        super(report);
        this.deletedByName = deletedByName;
    }

    public String getDeletedByName() {
        return deletedByName;
    }
}
