package net.shortninja.staffplus.core.domain.staff.reporting.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.gui.AsyncGui;
import be.garagepoort.mcioc.gui.GuiAction;
import be.garagepoort.mcioc.gui.GuiController;
import be.garagepoort.mcioc.gui.GuiParam;
import be.garagepoort.mcioc.gui.GuiParams;
import be.garagepoort.mcioc.gui.templates.GuiTemplate;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.chatchannels.ChatChannel;
import net.shortninja.staffplus.core.domain.chatchannels.ChatChannelService;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.reporting.CloseReportRequest;
import net.shortninja.staffplus.core.domain.staff.reporting.ManageReportService;
import net.shortninja.staffplus.core.domain.staff.reporting.Report;
import net.shortninja.staffplus.core.domain.staff.reporting.ReportService;
import net.shortninja.staffplus.core.domain.staff.reporting.chatchannels.ReportChatChannelService;
import net.shortninja.staffplus.core.domain.staff.reporting.config.ManageReportConfiguration;
import net.shortninja.staffplus.core.domain.staff.reporting.config.ReportConfiguration;
import net.shortninja.staffplus.core.domain.staff.reporting.gui.cmd.ReportFiltersMapper;
import net.shortninja.staffplusplus.chatchannels.ChatChannelType;
import net.shortninja.staffplusplus.reports.ReportFilters;
import net.shortninja.staffplusplus.reports.ReportStatus;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import static be.garagepoort.mcioc.gui.AsyncGui.async;
import static be.garagepoort.mcioc.gui.templates.GuiTemplate.template;

@IocBean
@GuiController
public class ReportsGuiController {

    @ConfigProperty("%lang%:reports.resolve-confirmation-question")
    private String resolveConfirmationLines;
    @ConfigProperty("%lang%:reports.resolve-cancelled")
    private String resolveCancelled;
    @ConfigProperty("%lang%:reports.reject-confirmation-question")
    private String rejectConfirmationLines;
    @ConfigProperty("%lang%:reports.reject-cancelled")
    private String rejectCancelled;

    private static final int PAGE_SIZE = 45;
    private static final String CANCEL = "cancel";

    private final PermissionHandler permissionHandler;
    private final ManageReportConfiguration manageReportConfiguration;
    private final ReportService reportService;
    private final BukkitUtils bukkitUtils;
    private final Messages messages;
    private final ManageReportService manageReportService;
    private final OnlineSessionsManager sessionManager;
    private final ReportFiltersMapper reportFiltersMapper;
    private final PlayerManager playerManager;
    private final ChatChannelService chatChannelService;
    private final ReportChatChannelService reportChatChannelService;
    private final ReportConfiguration reportConfiguration;

    public ReportsGuiController(PermissionHandler permissionHandler,
                                ManageReportConfiguration manageReportConfiguration,
                                ReportService reportService,
                                BukkitUtils bukkitUtils,
                                Messages messages,
                                ManageReportService manageReportService,
                                OnlineSessionsManager sessionManager,
                                ReportFiltersMapper reportFiltersMapper,
                                PlayerManager playerManager,
                                ChatChannelService chatChannelService,
                                ReportChatChannelService reportChatChannelService,
                                ReportConfiguration reportConfiguration) {
        this.permissionHandler = permissionHandler;
        this.manageReportConfiguration = manageReportConfiguration;
        this.reportService = reportService;
        this.bukkitUtils = bukkitUtils;
        this.messages = messages;
        this.manageReportService = manageReportService;

        this.sessionManager = sessionManager;
        this.reportFiltersMapper = reportFiltersMapper;
        this.playerManager = playerManager;
        this.chatChannelService = chatChannelService;
        this.reportChatChannelService = reportChatChannelService;
        this.reportConfiguration = reportConfiguration;
    }

    @GuiAction("manage-reports/view/overview")
    public GuiTemplate manageReportsOverview(Player player) {
        permissionHandler.validate(player, manageReportConfiguration.permissionView);
        return template("gui/reports/manage-reports.ftl", new HashMap<>());
    }

    @GuiAction("manage-reports/view/find-reports")
    public AsyncGui<GuiTemplate> viewFindReports(@GuiParam(value = "page", defaultValue = "0") int page,
                                                 @GuiParam(value = "backAction") String backAction,
                                                 @GuiParams Map<String, String> allParams) {
        return async(() -> {
            ReportFilters.ReportFiltersBuilder reportFiltersBuilder = new ReportFilters.ReportFiltersBuilder();
            allParams.forEach((k, v) -> reportFiltersMapper.map(k, v, reportFiltersBuilder));

            Map<String, Object> params = new HashMap<>();
            params.put("backAction", backAction);
            params.put("reports", reportService.findReports(reportFiltersBuilder.build(), page * PAGE_SIZE, PAGE_SIZE));

            return template("gui/reports/find-reports.ftl", params);
        });
    }

    @GuiAction("manage-reports/view/open")
    public AsyncGui<GuiTemplate> openReportsGui(@GuiParam(value = "page", defaultValue = "0") int page) {
        return async(() -> {
            Map<String, Object> params = new HashMap<>();
            params.put("reports", reportService.getUnresolvedReports(PAGE_SIZE * page, PAGE_SIZE));

            return template("gui/reports/open-reports.ftl", params);
        });
    }

    @GuiAction("manage-reports/view/detail")
    public AsyncGui<GuiTemplate> goToManageReportView(Player player, @GuiParam("reportId") int reportId) {
        return async(() -> {
            Map<String, Object> params = new HashMap<>();
            params.put("player", player);
            params.put("report", reportService.getReport(reportId));

            Optional<ChatChannel> channel = chatChannelService.findChannel(String.valueOf(reportId), ChatChannelType.REPORT);
            params.put("channelPresent", channel.isPresent());
            params.put("isMemberOfChannel", channel.isPresent() && channel.get().getMembers().contains(player.getUniqueId()));

            return template("gui/reports/report-detail.ftl", params);
        });
    }

    @GuiAction("manage-reports/view/assigned")
    public AsyncGui<GuiTemplate> allAssignedReportsGui(@GuiParam(value = "page", defaultValue = "0") int page) {
        return async(() -> {
            Map<String, Object> params = new HashMap<>();
            params.put("reports", reportService.getAllAssignedReports(PAGE_SIZE * page, PAGE_SIZE));

            return template("gui/reports/assigned-reports.ftl", params);
        });
    }

    @GuiAction("manage-reports/view/closed")
    public AsyncGui<GuiTemplate> closedReportsGui(@GuiParam(value = "page", defaultValue = "0") int page) {
        return async(() -> {
            Map<String, Object> params = new HashMap<>();
            params.put("reports", manageReportService.getClosedReports(PAGE_SIZE * page, PAGE_SIZE));

            return template("gui/reports/closed-reports.ftl", params);
        });
    }

    @GuiAction("manage-reports/view/my-assigned")
    public AsyncGui<GuiTemplate> myAssignedReportsGui(Player player,
                                                      @GuiParam(value = "page", defaultValue = "0") int page) {
        return async(() -> {
            Map<String, Object> params = new HashMap<>();
            params.put("reports", reportService.getAssignedReports(player.getUniqueId(), PAGE_SIZE * page, PAGE_SIZE));

            return template("gui/reports/my-assigned-reports.ftl", params);
        });
    }

    @GuiAction("my-reports/view")
    public AsyncGui<GuiTemplate> myReportsGui(Player player, @GuiParam(value = "page", defaultValue = "0") int page) {
        return async(() -> {
            Map<String, Object> params = new HashMap<>();
            params.put("reports", reportService.getMyReports(player.getUniqueId(), PAGE_SIZE * page, PAGE_SIZE));

            return template("gui/reports/my-reports.ftl", params);
        });
    }

    @GuiAction("manage-reports/accept")
    public AsyncGui<String> acceptReport(Player player, @GuiParam("reportId") int reportId, @GuiParam("backAction") String backAction) {
        permissionHandler.validate(player, manageReportConfiguration.permissionAccept);
        return async(() -> {
            SppPlayer sppPlayer = playerManager.getOnOrOfflinePlayer(player.getUniqueId())
                .orElseThrow(() -> new BusinessException(messages.playerNotRegistered));
            manageReportService.acceptReport(sppPlayer, reportId);
            return backAction;
        });
    }

    @GuiAction("manage-reports/delete")
    public void deleteReport(Player player,
                             @GuiParam("reportId") int reportId) {
        permissionHandler.validate(player, manageReportConfiguration.permissionDelete);
        SppPlayer sppPlayer = playerManager.getOnOrOfflinePlayer(player.getUniqueId())
            .orElseThrow(() -> new BusinessException(messages.playerNotRegistered));
        bukkitUtils.runTaskAsync(player, () -> manageReportService.deleteReport(sppPlayer, reportId));
    }

    @GuiAction("manage-reports/reopen")
    public void reopenReport(Player player,
                             @GuiParam("reportId") int reportId) {
        permissionHandler.validate(player, manageReportConfiguration.permissionReopenOther);
        SppPlayer sppPlayer = playerManager.getOnOrOfflinePlayer(player.getUniqueId())
            .orElseThrow(() -> new BusinessException(messages.playerNotRegistered));
        bukkitUtils.runTaskAsync(player, () -> manageReportService.reopenReport(sppPlayer, reportId));
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

    @GuiAction("manage-reports/join-chatchannel")
    public AsyncGui<String> joinChatChannel(Player player,
                                @GuiParam("reportId") int reportId,
                                @GuiParam("backAction") String backAction) {
        return async(() -> {
            SppPlayer sppPlayer = playerManager.getOnOrOfflinePlayer(player.getUniqueId())
                .orElseThrow(() -> new BusinessException(messages.playerNotRegistered));

            chatChannelService.joinChannel(sppPlayer, String.valueOf(reportId), ChatChannelType.REPORT);
            return backAction;
        });
    }

    @GuiAction("manage-reports/leave-chatchannel")
    public AsyncGui<String> leaveChatChannel(Player player,
                                             @GuiParam("reportId") int reportId,
                                             @GuiParam("backAction") String backAction) {
        return async(() -> {
            SppPlayer sppPlayer = playerManager.getOnOrOfflinePlayer(player.getUniqueId())
                .orElseThrow(() -> new BusinessException(messages.playerNotRegistered));

            chatChannelService.leaveChannel(sppPlayer, String.valueOf(reportId), ChatChannelType.REPORT);
            return backAction;
        });
    }

    @GuiAction("manage-reports/open-chatchannel")
    public AsyncGui<String> openChatChannel(Player player,
                                            @GuiParam("reportId") int reportId,
                                            @GuiParam("backAction") String backAction) {
        return async(() -> {
            SppPlayer sppPlayer = playerManager.getOnOrOfflinePlayer(player.getUniqueId())
                .orElseThrow(() -> new BusinessException(messages.playerNotRegistered));

            Report report = reportService.getReport(reportId);
            reportChatChannelService.openChannel(sppPlayer, report);
            return backAction;
        });
    }

    @GuiAction("manage-reports/close-chatchannel")
    public void closeChatChannel(Player player,
                                 @GuiParam("reportId") int reportId) {
        bukkitUtils.runTaskAsync(player, () -> {
            chatChannelService.closeChannel(String.valueOf(reportId), ChatChannelType.REPORT);
        });
    }

    @GuiAction("manage-reports/reject")
    public AsyncGui<String> rejectReport(Player player,
                                         @GuiParam("reportId") int reportId,
                                         @GuiParam("backAction") String backAction) {
        permissionHandler.validate(player, manageReportConfiguration.permissionReject);
        return async(() -> {
            SppPlayer sppPlayer = playerManager.getOnOrOfflinePlayer(player.getUniqueId())
                .orElseThrow(() -> new BusinessException(messages.playerNotRegistered));

            if (reportConfiguration.isClosingReasonEnabled()) {
                showCloseReasonGui(player, (message) -> manageReportService.closeReport(sppPlayer, new CloseReportRequest(reportId, ReportStatus.REJECTED, message)), rejectCancelled, rejectConfirmationLines);
                return null;
            }
            manageReportService.closeReport(sppPlayer, new CloseReportRequest(reportId, ReportStatus.REJECTED, null));
            return backAction;
        });
    }

    @GuiAction("manage-reports/accept-and-reject")
    public AsyncGui<String> acceptAndReject(Player player,
                                            @GuiParam("reportId") int reportId,
                                            @GuiParam("backAction") String backAction) {
        permissionHandler.validate(player, manageReportConfiguration.permissionAccept);
        permissionHandler.validate(player, manageReportConfiguration.permissionReject);
        return async(() -> {
            SppPlayer sppPlayer = playerManager.getOnOrOfflinePlayer(player.getUniqueId())
                .orElseThrow(() -> new BusinessException(messages.playerNotRegistered));

            if (reportConfiguration.isClosingReasonEnabled()) {
                showCloseReasonGui(player, (message) -> manageReportService.acceptAndClose(sppPlayer, new CloseReportRequest(reportId, ReportStatus.REJECTED, message)), rejectCancelled, rejectConfirmationLines);
                return null;
            }

            manageReportService.acceptAndClose(sppPlayer, new CloseReportRequest(reportId, ReportStatus.REJECTED, null));
            return backAction;
        });
    }

    @GuiAction("manage-reports/resolve")
    public AsyncGui<String> resolveReport(Player player, @GuiParam("reportId") int reportId, @GuiParam("backAction") String backAction) {
        permissionHandler.validate(player, manageReportConfiguration.permissionResolve);
        return async(() -> {
            SppPlayer sppPlayer = playerManager.getOnOrOfflinePlayer(player.getUniqueId())
                .orElseThrow(() -> new BusinessException(messages.playerNotRegistered));

            if (reportConfiguration.isClosingReasonEnabled()) {
                showCloseReasonGui(player, (message) -> manageReportService.closeReport(sppPlayer, new CloseReportRequest(reportId, ReportStatus.RESOLVED, message)), resolveCancelled, resolveConfirmationLines);
                return null;
            }

            manageReportService.closeReport(sppPlayer, new CloseReportRequest(reportId, ReportStatus.RESOLVED, null));
            return backAction;
        });
    }

    @GuiAction("manage-reports/accept-and-resolve")
    public AsyncGui<String> acceptAndResolve(Player player,
                                             @GuiParam("reportId") int reportId,
                                             @GuiParam("backAction") String backAction) {
        permissionHandler.validate(player, manageReportConfiguration.permissionAccept);
        permissionHandler.validate(player, manageReportConfiguration.permissionResolve);
        return async(() -> {
            SppPlayer sppPlayer = playerManager.getOnOrOfflinePlayer(player.getUniqueId())
                .orElseThrow(() -> new BusinessException(messages.playerNotRegistered));

            if (reportConfiguration.isClosingReasonEnabled()) {
                showCloseReasonGui(player, (message) -> manageReportService.acceptAndClose(sppPlayer, new CloseReportRequest(reportId, ReportStatus.RESOLVED, message)), resolveCancelled, resolveConfirmationLines);
                return null;
            }

            manageReportService.acceptAndClose(sppPlayer, new CloseReportRequest(reportId, ReportStatus.RESOLVED, null));
            return backAction;
        });
    }

    private void showCloseReasonGui(Player player, Consumer<String> onClose, String rejectCancelled, String messageLines) {
        messages.send(player, messageLines, messages.prefixReports);
        OnlinePlayerSession playerSession = sessionManager.get(player);
        playerSession.setChatAction((player1, message) -> {
            if (message.equalsIgnoreCase(CANCEL)) {
                messages.send(player, rejectCancelled, messages.prefixReports);
                return;
            }
            bukkitUtils.runTaskAsync(player, () -> onClose.accept(message));
        });
    }
}