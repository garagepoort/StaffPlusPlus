package net.shortninja.staffplus.core.domain.staff.reporting.bungee.dto;

import net.shortninja.staffplus.core.domain.staff.reporting.Report;

public class ReportReopenedBungee extends ReportBungee {

    private String reopenByName;

    public ReportReopenedBungee(Report report, String reopenByName) {
        super(report);
        this.reopenByName = reopenByName;
    }

    public String getReopenByName() {
        return reopenByName;
    }
}
