package net.shortninja.staffplus.core.domain.staff.reporting.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.gui.CurrentAction;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.GuiParam;
import be.garagepoort.mcioc.gui.GuiParams;
import be.garagepoort.mcioc.gui.TubingGui;
import be.garagepoort.mcioc.gui.templates.GuiTemplateResolver;
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
    private final GuiTemplateResolver guiTemplateResolver;

    public ReportsGuiController(PermissionHandler permissionHandler,
                                ManageReportConfiguration manageReportConfiguration,
                                ReportService reportService,
                                Options options,
                                Messages messages,
                                ManageReportService manageReportService,
                                SessionManagerImpl sessionManager,
                                ReportFiltersMapper reportFiltersMapper,
                                GuiTemplateResolver guiTemplateResolver) {
        this.permissionHandler = permissionHandler;
        this.manageReportConfiguration = manageReportConfiguration;
        this.reportService = reportService;
        this.options = options;
        this.messages = messages;
        this.manageReportService = manageReportService;

        this.sessionManager = sessionManager;
        this.reportFiltersMapper = reportFiltersMapper;
        this.guiTemplateResolver = guiTemplateResolver;
    }

    @GuiAction("manage-reports/view/overview")
    public TubingGui manageReportsOverview(Player player, @CurrentAction String currentAction) {
        permissionHandler.validate(player, manageReportConfiguration.permissionView);

        Map<String, Object> params = new HashMap<>();
        params.put("currentAction", currentAction);
        return guiTemplateResolver.resolve("gui/reports/manage-reports.ftl", params);
    }

    @GuiAction("manage-reports/view/find-reports")
    public TubingGui viewFindReports(@GuiParam(value = "page", defaultValue = "0") int page,
                                           @CurrentAction String currentAction,
                                           @GuiParams Map<String, String> allParams) {
        ReportFilters.ReportFiltersBuilder reportFiltersBuilder = new ReportFilters.ReportFiltersBuilder();
        allParams.forEach((k, v) -> reportFiltersMapper.map(k, v, reportFiltersBuilder));

        Map<String, Object> params = new HashMap<>();
        params.put("title", "Find reports");
        params.put("reports", reportService.findReports(reportFiltersBuilder.build(), page * PAGE_SIZE, PAGE_SIZE));
        addPageParams(page, currentAction, null, params);

        return guiTemplateResolver.resolve("gui/reports/find-reports.ftl", params);
    }

    @GuiAction("manage-reports/view/open")
    public TubingGui openReportsGui(@GuiParam(value = "page", defaultValue = "0") int page,
                                    @CurrentAction String currentAction,
                                    @GuiParam("backAction") String backAction) {
        Map<String, Object> params = new HashMap<>();
        params.put("title", "Open reports");
        params.put("reports", reportService.getUnresolvedReports(PAGE_SIZE * page, PAGE_SIZE));
        addPageParams(page, currentAction, backAction, params);

        return guiTemplateResolver.resolve("gui/reports/open-reports.ftl", params);
    }

    @GuiAction("manage-reports/view/detail")
    public TubingGui goToManageReportView(Player player,
                                          @GuiParam("reportId") int reportId,
                                          @GuiParam("backAction") String backAction,
                                          @CurrentAction String currentAction) {
        Map<String, Object> params = new HashMap<>();
        params.put("player", player);
        params.put("report", reportService.getReport(reportId));
        params.put("backAction", backAction);
        params.put("currentAction", currentAction);

        return guiTemplateResolver.resolve("gui/reports/report-detail.ftl", params);
    }

    @GuiAction("manage-reports/view/assigned")
    public TubingGui allAssignedReportsGui(@GuiParam(value = "page", defaultValue = "0") int page,
                                           @CurrentAction String currentAction,
                                           @GuiParam("backAction") String backAction) {

        Map<String, Object> params = new HashMap<>();
        params.put("title", "Reports in progress");
        params.put("reports", reportService.getAllAssignedReports(PAGE_SIZE * page, PAGE_SIZE));
        addPageParams(page, currentAction, backAction, params);

        return guiTemplateResolver.resolve("gui/reports/reports.ftl", params);
    }

    @GuiAction("manage-reports/view/closed")
    public TubingGui closedReportsGui(@GuiParam(value = "page", defaultValue = "0") int page,
                                      @CurrentAction String currentAction,
                                      @GuiParam("backAction") String backAction) {
        Map<String, Object> params = new HashMap<>();
        params.put("title", "Closed reports");
        params.put("reports", manageReportService.getClosedReports(PAGE_SIZE * page, PAGE_SIZE));
        addPageParams(page, currentAction, backAction, params);

        return guiTemplateResolver.resolve("gui/reports/reports.ftl", params);
    }


    @GuiAction("manage-reports/view/my-assigned")
    public TubingGui myAssignedReportsGui(Player player,
                                          @GuiParam(value = "page", defaultValue = "0") int page,
                                          @CurrentAction String currentAction,
                                          @GuiParam("backAction") String backAction) {
        Map<String, Object> params = new HashMap<>();
        params.put("title", "Reports assigned to you");
        params.put("reports", reportService.getAssignedReports(player.getUniqueId(), PAGE_SIZE * page, PAGE_SIZE));
        addPageParams(page, currentAction, backAction, params);

        return guiTemplateResolver.resolve("gui/reports/reports.ftl", params);
    }

    @GuiAction("my-reports/view")
    public TubingGui myReportsGui(Player player,
                                  @GuiParam(value = "page", defaultValue = "0") int page,
                                  @CurrentAction String currentAction) {
        Map<String, Object> params = new HashMap<>();
        params.put("reports", reportService.getMyReports(player.getUniqueId(), PAGE_SIZE * page, PAGE_SIZE));
        addPageParams(page, currentAction, null, params);

        return guiTemplateResolver.resolve("gui/reports/my-reports.ftl", params);
    }


    @GuiAction("manage-reports/accept")
    public void acceptReport(Player player,
                             @GuiParam("reportId") int reportId) {
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


    private void addPageParams(int page, String currentAction, String backAction, Map<String, Object> params) {
        params.put("page", page);
        params.put("backAction", backAction);
        params.put("currentAction", currentAction);
    }

}