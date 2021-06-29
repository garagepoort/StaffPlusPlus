package net.shortninja.staffplus.core.domain.staff.reporting.bungee.dto;

import net.shortninja.staffplusplus.reports.IReport;

public class ReportReopenedBungeeDto extends ReportBungeeDto {

    private String reopenByName;

    public ReportReopenedBungeeDto(IReport report, String reopenByName) {
        super(report);
        this.reopenByName = reopenByName;
    }

    public String getReopenByName() {
        return reopenByName;
    }
}
