package net.shortninja.staffplus.core.domain.staff.reporting;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.reporting.config.ManageReportConfiguration;
import net.shortninja.staffplus.core.domain.staff.reporting.database.ReportRepository;
import net.shortninja.staffplusplus.reports.AcceptReportEvent;
import net.shortninja.staffplusplus.reports.DeleteReportEvent;
import net.shortninja.staffplusplus.reports.RejectReportEvent;
import net.shortninja.staffplusplus.reports.ReopenReportEvent;
import net.shortninja.staffplusplus.reports.ReportStatus;
import net.shortninja.staffplusplus.reports.ResolveReportEvent;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.entity.Player;

import java.util.List;

import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;

@IocBean
public class ManageReportService {

    private final PermissionHandler permission;
    private final Messages messages;
    private final ReportService reportService;
    private final ReportRepository reportRepository;
    private final ManageReportConfiguration manageReportConfiguration;

    public ManageReportService(PermissionHandler permission, ReportRepository reportRepository, Messages messages, ReportService reportService, ManageReportConfiguration manageReportConfiguration) {
        this.permission = permission;
        this.reportRepository = reportRepository;
        this.messages = messages;
        this.reportService = reportService;
        this.manageReportConfiguration = manageReportConfiguration;
    }

    public void clearReports(SppPlayer player) {
        reportRepository.removeReports(player.getId());
    }

    public void acceptReport(SppPlayer player, int reportId) {
        Report report = reportRepository.findOpenReport(reportId)
            .orElseThrow(() -> new BusinessException("&CReport with id [" + reportId + "] not found", messages.prefixReports));

        report.setReportStatus(net.shortninja.staffplusplus.reports.ReportStatus.IN_PROGRESS);
        report.setStaffUuid(player.getId());
        report.setStaffName(player.getUsername());
        reportRepository.updateReport(report);
        sendEvent(new AcceptReportEvent(report));
    }

    public void reopenReport(Player player, int reportId) {
        Report report = reportService.getReport(reportId);
        if (!report.getStaffUuid().equals(player.getUniqueId()) && !permission.has(player, manageReportConfiguration.permissionReopenOther)) {
            throw new BusinessException("&CYou cannot change the status of a report you are not assigned to", messages.prefixReports);
        }

        report.setStaffUuid(null);
        report.setStaffName(null);
        report.setReportStatus(ReportStatus.OPEN);
        reportRepository.updateReport(report);
        sendEvent(new ReopenReportEvent(report, player.getName()));
    }

    public void closeReport(SppPlayer player, CloseReportRequest closeReportRequest) {
        Report report = reportService.getReport(closeReportRequest.getReportId());
        closeReport(player, report, closeReportRequest.getStatus(), closeReportRequest.getCloseReason());
        sendEvent(closeReportRequest.getStatus() == ReportStatus.REJECTED ? new RejectReportEvent(report) : new ResolveReportEvent(report));
    }

    public void acceptAndClose(SppPlayer player, CloseReportRequest closeReportRequest) {
        Report report = reportRepository.findOpenReport(closeReportRequest.getReportId())
            .orElseThrow(() -> new BusinessException("&CReport with id [" + closeReportRequest.getReportId() + "] not found", messages.prefixReports));

        report.setReportStatus(net.shortninja.staffplusplus.reports.ReportStatus.IN_PROGRESS);
        report.setStaffUuid(player.getId());
        report.setStaffName(player.getUsername());
        closeReport(player, report, closeReportRequest.getStatus(), closeReportRequest.getCloseReason());
        sendEvent(closeReportRequest.getStatus() == ReportStatus.REJECTED ? new RejectReportEvent(report) : new ResolveReportEvent(report));
    }

    private void closeReport(SppPlayer player, Report report, ReportStatus status, String closeReason) {
        if (!report.getStaffUuid().equals(player.getId())) {
            throw new BusinessException("&CYou cannot change the status of a report you are not assigned to", messages.prefixReports);
        }
        report.setReportStatus(status);
        report.setCloseReason(closeReason);
        reportRepository.updateReport(report);
    }

    public List<Report> getClosedReports(int offset, int amount) {
        return reportRepository.getClosedReports(offset, amount);
    }

    public void deleteReport(SppPlayer player, int reportId) {
        Report report = reportService.getReport(reportId);
        reportRepository.markReportDeleted(report);
        sendEvent(new DeleteReportEvent(report, player.getUsername()));
    }
}
