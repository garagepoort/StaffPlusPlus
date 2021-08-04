package net.shortninja.staffplus.core.domain.staff.reporting.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.GuiParam;
import be.garagepoort.mcioc.gui.GuiParams;
import be.garagepoort.mcioc.gui.templates.GuiTemplate;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.application.session.SessionManagerImpl;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.reporting.CloseReportRequest;
import net.shortninja.staffplus.core.domain.staff.reporting.ManageReportService;
import net.shortninja.staffplus.core.domain.staff.reporting.Report;
import net.shortninja.staffplus.core.domain.staff.reporting.ReportService;
import net.shortninja.staffplus.core.domain.staff.reporting.cmd.ReportFiltersMapper;
import net.shortninja.staffplus.core.domain.staff.reporting.config.ManageReportConfiguration;
import net.shortninja.staffplusplus.reports.ReportFilters;
import net.shortninja.staffplusplus.reports.ReportStatus;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

import static be.garagepoort.mcioc.gui.templates.GuiTemplate.template;

@IocBean
@GuiController
public class ReportsGuiController {

    private static final int PAGE_SIZE = 45;
    private static final String CANCEL = "cancel";

    private final PermissionHandler permissionHandler;
    private final ManageReportConfiguration manageReportConfiguration;
    private final ReportService reportService;
    private final Options options;
    private final Messages messages;
    private final ManageReportService manageReportService;
    private final SessionManagerImpl sessionManager;
    private final ReportFiltersMapper reportFiltersMapper;

    public ReportsGuiController(PermissionHandler permissionHandler,
                                ManageReportConfiguration manageReportConfiguration,
                                ReportService reportService,
                                Options options,
                                Messages messages,
                                ManageReportService manageReportService,
                                SessionManagerImpl sessionManager,
                                ReportFiltersMapper reportFiltersMapper) {
        this.permissionHandler = permissionHandler;
        this.manageReportConfiguration = manageReportConfiguration;
        this.reportService = reportService;
        this.options = options;
        this.messages = messages;
        this.manageReportService = manageReportService;

        this.sessionManager = sessionManager;
        this.reportFiltersMapper = reportFiltersMapper;
    }

    @GuiAction("manage-reports/view/overview")
    public GuiTemplate manageReportsOverview(Player player) {
        permissionHandler.validate(player, manageReportConfiguration.permissionView);
        return template("gui/reports/manage-reports.ftl", new HashMap<>());
    }

    @GuiAction("manage-reports/view/find-reports")
    public GuiTemplate viewFindReports(@GuiParam(value = "page", defaultValue = "0") int page,
                                            @GuiParams Map<String, String> allParams) {
        ReportFilters.ReportFiltersBuilder reportFiltersBuilder = new ReportFilters.ReportFiltersBuilder();
        allParams.forEach((k, v) -> reportFiltersMapper.map(k, v, reportFiltersBuilder));

        Map<String, Object> params = new HashMap<>();
        params.put("title", "Find reports");
        params.put("reports", reportService.findReports(reportFiltersBuilder.build(), page * PAGE_SIZE, PAGE_SIZE));

        return template("gui/reports/find-reports.ftl", params);
    }

    @GuiAction("manage-reports/view/open")
    public GuiTemplate openReportsGui(@GuiParam(value = "page", defaultValue = "0") int page) {
        Map<String, Object> params = new HashMap<>();
        params.put("title", "Open reports");
        params.put("reports", reportService.getUnresolvedReports(PAGE_SIZE * page, PAGE_SIZE));

        return template("gui/reports/open-reports.ftl", params);
    }

    @GuiAction("manage-reports/view/detail")
    public GuiTemplate goToManageReportView(Player player, @GuiParam("reportId") int reportId) {
        Map<String, Object> params = new HashMap<>();
        params.put("player", player);
        params.put("report", reportService.getReport(reportId));

        return template("gui/reports/report-detail.ftl", params);
    }

    @GuiAction("manage-reports/view/assigned")
    public GuiTemplate allAssignedReportsGui(@GuiParam(value = "page", defaultValue = "0") int page) {

        Map<String, Object> params = new HashMap<>();
        params.put("title", "Reports in progress");
        params.put("reports", reportService.getAllAssignedReports(PAGE_SIZE * page, PAGE_SIZE));

        return template("gui/reports/reports.ftl", params);
    }

    @GuiAction("manage-reports/view/closed")
    public GuiTemplate closedReportsGui(@GuiParam(value = "page", defaultValue = "0") int page) {
        Map<String, Object> params = new HashMap<>();
        params.put("title", "Closed reports");
        params.put("reports", manageReportService.getClosedReports(PAGE_SIZE * page, PAGE_SIZE));

        return template("gui/reports/reports.ftl", params);
    }


    @GuiAction("manage-reports/view/my-assigned")
    public GuiTemplate myAssignedReportsGui(Player player,
                                                 @GuiParam(value = "page", defaultValue = "0") int page) {
        Map<String, Object> params = new HashMap<>();
        params.put("title", "Reports assigned to you");
        params.put("reports", reportService.getAssignedReports(player.getUniqueId(), PAGE_SIZE * page, PAGE_SIZE));

        return template("gui/reports/reports.ftl", params);
    }

    @GuiAction("my-reports/view")
    public GuiTemplate myReportsGui(Player player, @GuiParam(value = "page", defaultValue = "0") int page) {
        Map<String, Object> params = new HashMap<>();
        params.put("reports", reportService.getMyReports(player.getUniqueId(), PAGE_SIZE * page, PAGE_SIZE));

        return template("gui/reports/my-reports.ftl", params);
    }


    @GuiAction("manage-reports/accept")
    public void acceptReport(Player player, @GuiParam("reportId") int reportId) {
        permissionHandler.validate(player, manageReportConfiguration.permissionAccept);
        manageReportService.acceptReport(player, reportId);
    }

    @GuiAction("manage-reports/delete")
    public void deleteReport(Player player,
                             @GuiParam("reportId") int reportId) {
        permissionHandler.validate(player, manageReportConfiguration.permissionDelete);
        manageReportService.deleteReport(player, reportId);
    }

    @GuiAction("manage-reports/reopen")
    public void reopenReport(Player player,
                             @GuiParam("reportId") int reportId) {
        manageReportService.reopenReport(player, reportId);
    }

    @GuiAction("manage-reports/teleport")
    public void teleportToReport(Player player,
                                 @GuiParam("reportId") int reportId) {
        Report report = reportService.getReport(reportId);
        if (report.getLocation().isPresent()) {
            reportService.goToReportLocation(player, report.getId());
        } else {
            messages.send(player, "&cLocation not known for this report.", messages.prefixReports);
        }
    }

    @GuiAction("manage-reports/reject")
    public void rejectReport(Player player,
                             @GuiParam("reportId") int reportId) {
        if (options.reportConfiguration.isClosingReasonEnabled()) {
            messages.send(player, "&1==================================================", messages.prefixReports);
            messages.send(player, "&6        You have chosen to reject this report", messages.prefixReports);
            messages.send(player, "&6Type your closing reason in chat to reject the report", messages.prefixReports);
            messages.send(player, "&6        Type \"cancel\" to cancel closing the report ", messages.prefixReports);
            messages.send(player, "&1==================================================", messages.prefixReports);
            PlayerSession playerSession = sessionManager.get(player.getUniqueId());
            playerSession.setChatAction((player1, message) -> {
                if (message.equalsIgnoreCase(CANCEL)) {
                    messages.send(player, "&CYou have cancelled rejecting this report", messages.prefixReports);
                    return;
                }
                manageReportService.closeReport(player, new CloseReportRequest(reportId, ReportStatus.REJECTED, message));
            });
        } else {
            manageReportService.closeReport(player, new CloseReportRequest(reportId, ReportStatus.REJECTED, null));
        }
    }

    @GuiAction("manage-reports/resolve")
    public void resolveReport(Player player,
                              @GuiParam("reportId") int reportId) {
        permissionHandler.validate(player, manageReportConfiguration.permissionResolve);
        if (options.reportConfiguration.isClosingReasonEnabled()) {
            messages.send(player, "&1===================================================", messages.prefixReports);
            messages.send(player, "&6       You have chosen to resolve this report", messages.prefixReports);
            messages.send(player, "&6Type your closing reason in chat to resolve the report", messages.prefixReports);
            messages.send(player, "&6      Type \"cancel\" to cancel closing the report ", messages.prefixReports);
            messages.send(player, "&1===================================================", messages.prefixReports);
            PlayerSession playerSession = sessionManager.get(player.getUniqueId());
            playerSession.setChatAction((player1, message) -> {
                if (message.equalsIgnoreCase(CANCEL)) {
                    messages.send(player, "&CYou have cancelled rejecting this report", messages.prefixReports);
                    return;
                }
                manageReportService.closeReport(player, new CloseReportRequest(reportId, ReportStatus.RESOLVED, message));
            });
        } else {
            manageReportService.closeReport(player, new CloseReportRequest(reportId, ReportStatus.RESOLVED, null));
        }
    }

}