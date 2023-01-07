package net.shortninja.staffplus.core.domain.staff.reporting;

import net.shortninja.staffplusplus.reports.ReportStatus;

public class CloseReportRequest {

    private final int reportId;
    private final ReportStatus status;
    private String closeReason;

    public CloseReportRequest(int reportId, ReportStatus status, String closeReason) {
        this.reportId = reportId;
        this.status = status;
        this.closeReason = closeReason;
    }

    public int getReportId() {
        return reportId;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public String getCloseReason() {
        return closeReason;
    }

    public void setCloseReason(String closeReason) {
        this.closeReason = closeReason;
    }
}
