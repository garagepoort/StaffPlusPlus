package net.shortninja.staffplus.core.domain.staff.reporting.bungee.dto;

import net.shortninja.staffplus.core.common.SppLocation;
import net.shortninja.staffplus.core.common.bungee.BungeeMessage;
import net.shortninja.staffplusplus.ILocation;
import net.shortninja.staffplusplus.reports.IReport;
import net.shortninja.staffplusplus.reports.ReportStatus;

import java.util.UUID;

public class ReportBungee extends BungeeMessage {

    private int id;
    private UUID culpritUuid;
    private String culpritName;
    private String reason;
    private UUID reporterUuid;
    private String reporterName;
    private String staffName;
    private UUID staffUuid;
    private ReportStatus reportStatus;
    private String closeReason;
    private SppLocation sppLocation;
    private String type;

    public ReportBungee(IReport report) {
        super(report.getServerName());
        this.id = report.getId();
        this.culpritUuid = report.getCulpritUuid();
        this.culpritName = report.getCulpritName();
        this.reason = report.getReason();
        this.reporterUuid = report.getReporterUuid();
        this.reporterName = report.getReporterName();
        this.staffName = report.getStaffName();
        this.staffUuid = report.getStaffUuid();
        this.reportStatus = report.getReportStatus();
        this.closeReason = report.getCloseReason();
        this.sppLocation = (SppLocation) report.getSppLocation().orElse(null);
        this.type = report.getReportType().orElse(null);
    }

    public int getId() {
        return id;
    }

    public UUID getCulpritUuid() {
        return culpritUuid;
    }

    public String getCulpritName() {
        return culpritName;
    }

    public String getReason() {
        return reason;
    }

    public UUID getReporterUuid() {
        return reporterUuid;
    }

    public String getReporterName() {
        return reporterName;
    }

    public String getStaffName() {
        return staffName;
    }

    public UUID getStaffUuid() {
        return staffUuid;
    }

    public ReportStatus getReportStatus() {
        return reportStatus;
    }

    public String getCloseReason() {
        return closeReason;
    }

    public ILocation getSppLocation() {
        return sppLocation;
    }

    public String getType() {
        return type;
    }
}
