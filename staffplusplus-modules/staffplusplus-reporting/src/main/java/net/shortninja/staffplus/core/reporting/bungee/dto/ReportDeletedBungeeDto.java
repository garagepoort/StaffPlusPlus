package net.shortninja.staffplus.core.reporting.bungee.dto;

import net.shortninja.staffplusplus.reports.IReport;

public class ReportDeletedBungeeDto extends ReportBungeeDto {

    private String deletedByName;

    public ReportDeletedBungeeDto(IReport report, String deletedByName) {
        super(report);
        this.deletedByName = deletedByName;
    }

    public String getDeletedByName() {
        return deletedByName;
    }
}
