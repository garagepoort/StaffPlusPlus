package net.shortninja.staffplus.core.domain.staff.reporting;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.exceptions.NoPermissionException;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.reporting.database.ReportRepository;
import net.shortninja.staffplusplus.reports.*;
import org.bukkit.entity.Player;

import java.util.List;

import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;
import static org.bukkit.Bukkit.getScheduler;

@IocBean
public class ManageReportService {

    private final PermissionHandler permission;
    private final Options options;
    private final Messages messages;
    private final ReportService reportService;
    private final ReportRepository reportRepository;

    public ManageReportService(PermissionHandler permission, Options options, ReportRepository reportRepository, Messages messages, ReportService reportService) {
        this.permission = permission;
        this.options = options;
        this.reportRepository = reportRepository;
        this.messages = messages;
        this.reportService = reportService;
    }

    public void clearReports(SppPlayer player) {
        reportRepository.removeReports(player.getId());
    }


    public void acceptReport(Player player, int reportId) {
        if (!permission.has(player, options.manageReportConfiguration.getPermissionReject())) {
            throw new NoPermissionException();
        }

        getScheduler().runTaskAsynchronously(StaffPlus.get(), () -> {
            Report report = reportRepository.findOpenReport(reportId)
                .orElseThrow(() -> new BusinessException("&CReport with id [" + reportId + "] not found", messages.prefixReports));

            report.setReportStatus(net.shortninja.staffplusplus.reports.ReportStatus.IN_PROGRESS);
            report.setStaffUuid(player.getUniqueId());
            report.setStaffName(player.getName());
            reportRepository.updateReport(report);
            sendEvent(new AcceptReportEvent(report));
        });

    }

    public void reopenReport(Player player, int reportId) {
        Report report = reportService.getReport(reportId);
        if (!report.getStaffUuid().equals(player.getUniqueId()) && !permission.has(player, options.manageReportConfiguration.getPermissionReopenOther())) {
            throw new BusinessException("&CYou cannot change the status of a report you are not assigned to", messages.prefixReports);
        }
        getScheduler().runTaskAsynchronously(StaffPlus.get(), () -> {

            report.setStaffUuid(null);
            report.setStaffName(null);
            report.setReportStatus(ReportStatus.OPEN);
            reportRepository.updateReport(report);
            sendEvent(new ReopenReportEvent(report, player.getName()));
        });
    }

    public void closeReport(Player player, CloseReportRequest closeReportRequest) {
        getScheduler().runTaskAsynchronously(StaffPlus.get(), () -> {
            Report report = reportService.getReport(closeReportRequest.getReportId());
            closedReport(player, report, closeReportRequest.getStatus(), closeReportRequest.getCloseReason());
            sendEvent(closeReportRequest.getStatus() == ReportStatus.REJECTED ? new RejectReportEvent(report) : new ResolveReportEvent(report));
        });
    }

    private void closedReport(Player player, Report report, ReportStatus status, String closeReason) {
        if (!report.getStaffUuid().equals(player.getUniqueId())) {
            throw new BusinessException("&CYou cannot change the status of a report you are not assigned to", messages.prefixReports);
        }

        if (status == ReportStatus.REJECTED && !permission.has(player, options.manageReportConfiguration.getPermissionReject())) {
            throw new NoPermissionException();
        }
        if (status == ReportStatus.RESOLVED && !permission.has(player, options.manageReportConfiguration.getPermissionResolve())) {
            throw new NoPermissionException();
        }
        report.setReportStatus(status);
        report.setCloseReason(closeReason);
        reportRepository.updateReport(report);
    }

    public List<Report> getClosedReports(int offset, int amount) {
        return reportRepository.getClosedReports(offset, amount);
    }

    public void deleteReport(Player player, int reportId) {
        if (!permission.has(player, options.manageReportConfiguration.getPermissionDelete())) {
            throw new NoPermissionException();
        }
        Report report = reportService.getReport(reportId);
        reportRepository.markReportDeleted(report);
        sendEvent(new DeleteReportEvent(report, player.getName()));
    }
}
