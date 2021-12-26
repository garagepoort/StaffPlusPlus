package net.shortninja.staffplus.core.domain.staff.reporting.gui.chat;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.domain.staff.reporting.bungee.dto.ReportBungeeDto;
import net.shortninja.staffplusplus.reports.IReport;
import net.shortninja.staffplusplus.reports.ReportStatus;

@IocBean
public class ReportMessageUtil {

    private final Messages messages;

    public ReportMessageUtil(Messages messages) {
        this.messages = messages;
    }

    public String replaceReportPlaceholders(String message, IReport report) {
        return replaceReportPlaceholders(message,
            report.getStaffName(),
            report.getReporterName(),
            report.getCulpritName(),
            report.getReason(),
            report.getReportStatus(),
            report.getCloseReason());
    }

    public String replaceReportPlaceholders(String message, ReportBungeeDto report) {
        return replaceReportPlaceholders(message,
            report.getStaffName(),
            report.getReporterName(),
            report.getCulpritName(),
            report.getReason(),
            report.getReportStatus(),
            report.getCloseReason());
    }

    public String replaceReportPlaceholders(String message, String staff, IReport report) {
        return replaceReportPlaceholders(message,
            staff,
            report.getReporterName(),
            report.getCulpritName(),
            report.getReason(),
            report.getReportStatus(),
            report.getCloseReason());
    }

    public String replaceReportPlaceholders(String message, String staff, ReportBungeeDto report) {
        return replaceReportPlaceholders(message,
            staff,
            report.getReporterName(),
            report.getCulpritName(),
            report.getReason(),
            report.getReportStatus(),
            report.getCloseReason());
    }

    public String replaceReportPlaceholders(String message, String staff, String reporterName, String culpritName, String reason, ReportStatus reportStatus, String closeReason) {
        String result = message;
        if (staff != null) result = result.replace("%staff%", staff);
        if (reporterName != null) result = result.replace("%reporter%", reporterName);
        if (culpritName != null) result = result.replace("%culprit%", culpritName);
        if (culpritName == null) result = result.replace("%culprit%", "Unknown");
        if (reason != null) result = result.replace("%reason%", reason);
        if (reportStatus != null) result = result.replace("%status%", translateStatus(reportStatus));
        if (closeReason != null) result = result.replace("%close_reason%", closeReason);
        return result;
    }

    public String translateStatus(ReportStatus reportStatus) {
        switch (reportStatus) {
            case OPEN:
                return messages.reportOpenStatus;
            case IN_PROGRESS:
                return messages.reportInProgressStatus;
            case RESOLVED:
                return messages.reportResolvedStatus;
            case REJECTED:
                return messages.reportRejectedStatus;
        }
        return "Unknown";
    }
}
