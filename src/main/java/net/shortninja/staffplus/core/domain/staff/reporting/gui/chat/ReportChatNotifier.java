package net.shortninja.staffplus.core.domain.staff.reporting.gui.chat;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import me.rayzr522.jsonmessage.JSONMessage;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.reporting.bungee.dto.ReportBungeeDto;
import net.shortninja.staffplus.core.domain.staff.reporting.bungee.dto.ReportDeletedBungeeDto;
import net.shortninja.staffplus.core.domain.staff.reporting.bungee.dto.ReportReopenedBungeeDto;
import net.shortninja.staffplus.core.domain.staff.reporting.bungee.events.ReportAcceptedBungeeEvent;
import net.shortninja.staffplus.core.domain.staff.reporting.bungee.events.ReportClosedBungeeEvent;
import net.shortninja.staffplus.core.domain.staff.reporting.bungee.events.ReportCreatedBungeeEvent;
import net.shortninja.staffplus.core.domain.staff.reporting.bungee.events.ReportDeletedBungeeEvent;
import net.shortninja.staffplus.core.domain.staff.reporting.bungee.events.ReportReopenedBungeeEvent;
import net.shortninja.staffplusplus.reports.AcceptReportEvent;
import net.shortninja.staffplusplus.reports.CreateReportEvent;
import net.shortninja.staffplusplus.reports.DeleteReportEvent;
import net.shortninja.staffplusplus.reports.IReport;
import net.shortninja.staffplusplus.reports.RejectReportEvent;
import net.shortninja.staffplusplus.reports.ReopenReportEvent;
import net.shortninja.staffplusplus.reports.ReportStatus;
import net.shortninja.staffplusplus.reports.ResolveReportEvent;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Optional;
import java.util.UUID;

import static net.shortninja.staffplusplus.reports.ReportStatus.IN_PROGRESS;
import static net.shortninja.staffplusplus.reports.ReportStatus.REJECTED;
import static net.shortninja.staffplusplus.reports.ReportStatus.RESOLVED;

@IocBean
@IocListener
public class ReportChatNotifier implements Listener {

    @ConfigProperty("permissions:report-update-notifications")
    private String permissionReportUpdateNotifications;

    private final Messages messages;
    private final Options options;
    private final PlayerManager playerManager;
    private final PermissionHandler permission;
    private final ReportMessageUtil reportMessageUtil;

    public ReportChatNotifier(Messages messages, Options options, PlayerManager playerManager, PermissionHandler permission, ReportMessageUtil reportMessageUtil) {
        this.messages = messages;
        this.options = options;
        this.playerManager = playerManager;
        this.permission = permission;
        this.reportMessageUtil = reportMessageUtil;
    }

    // CREATION
    @EventHandler
    public void handleReportCreation(CreateReportEvent event) {
        IReport report = event.getReport();

        String message = reportMessageUtil.replaceReportPlaceholders(report.getCulpritUuid() == null ? messages.reporterReportCreated : messages.reporterReportPlayerCreated, report);
        playerManager.getOnlinePlayer(report.getReporterUuid())
            .ifPresent(sppPlayer -> messages.send(sppPlayer.getPlayer(), message, messages.prefixReports));

        String notificationMessage = report.getCulpritUuid() == null ? messages.reportCreatedNotification : messages.reportCulpritCreatedNotification;
        sendStaffNotification(report, notificationMessage);
        options.reportConfiguration.getSound().ifPresent(s -> s.playForGroup(permissionReportUpdateNotifications));
    }

    @EventHandler
    public void handleReportCreationBungee(ReportCreatedBungeeEvent event) {
        ReportBungeeDto report = event.getReport();

        String notificationMessage = reportMessageUtil.replaceReportPlaceholders(report.getCulpritUuid() == null ? messages.reportCreatedNotification : messages.reportCulpritCreatedNotification, report);
        sendStaffNotification(report, notificationMessage);
        options.reportConfiguration.getSound().ifPresent(s -> s.playForGroup(permissionReportUpdateNotifications));
    }

    // DELETION
    @EventHandler
    public void handleReportDeletion(DeleteReportEvent event) {
        IReport report = event.getReport();
        String message = reportMessageUtil.replaceReportPlaceholders(messages.reportDeletedNotification, event.getDeletedByName(), report);
        messages.sendGroupMessage(message, permissionReportUpdateNotifications, messages.prefixReports);
    }

    @EventHandler
    public void handleReportDeletionBungee(ReportDeletedBungeeEvent event) {
        ReportDeletedBungeeDto report = event.getReportDeleted();
        String message = reportMessageUtil.replaceReportPlaceholders(messages.reportDeletedNotification, report.getDeletedByName(), report);
        messages.sendGroupMessage(message, permissionReportUpdateNotifications, messages.prefixReports);
    }

    // ACCEPT
    @EventHandler
    public void handleReportAccepted(AcceptReportEvent event) {
        IReport report = event.getReport();
        sendStaffNotification(report, messages.reportAcceptedNotification);
        notifyReporter(report.getReporterUuid(), reportMessageUtil.replaceReportPlaceholders(messages.reporterReportAccepted, report), IN_PROGRESS);
    }

    @EventHandler
    public void handleReportAcceptedBungee(ReportAcceptedBungeeEvent event) {
        ReportBungeeDto report = event.getReport();
        sendStaffNotification(report, messages.reportAcceptedNotification);
        notifyReporter(report.getReporterUuid(), reportMessageUtil.replaceReportPlaceholders(messages.reporterReportAccepted, report), IN_PROGRESS);
    }

    // REJECTED / RESOLVED
    @EventHandler
    public void handleReportRejected(RejectReportEvent event) {
        IReport report = event.getReport();
        sendStaffNotification(report, messages.reportClosedNotification);
        notifyReporter(report.getReporterUuid(), reportMessageUtil.replaceReportPlaceholders(messages.reporterReportRejected, report), REJECTED);
    }

    @EventHandler
    public void handleReportResolved(ResolveReportEvent event) {
        IReport report = event.getReport();
        sendStaffNotification(report, messages.reportClosedNotification);
        notifyReporter(report.getReporterUuid(), reportMessageUtil.replaceReportPlaceholders(messages.reporterReportResolved, report), RESOLVED);
    }

    @EventHandler
    public void handleReportClosedBungee(ReportClosedBungeeEvent event) {
        ReportBungeeDto report = event.getReport();
        ReportStatus reportStatus = report.getReportStatus();

        sendStaffNotification(report, messages.reportClosedNotification);

        String message = reportStatus == REJECTED ? messages.reporterReportRejected : messages.reporterReportResolved;
        notifyReporter(report.getReporterUuid(), reportMessageUtil.replaceReportPlaceholders(message, report), reportStatus);
    }

    // REOPENED
    @EventHandler
    public void handleReportReopened(ReopenReportEvent event) {
        IReport report = event.getReport();
        String message = reportMessageUtil.replaceReportPlaceholders(messages.reportReopenedNotification, event.getReopenedByName(), report);
        messages.sendGroupMessage(message, permissionReportUpdateNotifications, messages.prefixReports);
    }

    @EventHandler
    public void handleReportReopenedBungee(ReportReopenedBungeeEvent event) {
        ReportReopenedBungeeDto report = event.getReopenedBungeeDto();
        String message = reportMessageUtil.replaceReportPlaceholders(messages.reportReopenedNotification, report.getReopenByName(), report);
        messages.sendGroupMessage(message, permissionReportUpdateNotifications, messages.prefixReports);
    }

    private void sendStaffNotification(IReport report, String reportClosedNotification) {
        messages.sendGroupMessage(reportMessageUtil.replaceReportPlaceholders(reportClosedNotification, report), permissionReportUpdateNotifications, messages.prefixReports);
    }

    private void sendStaffNotification(ReportBungeeDto report, String reportClosedNotification) {
        messages.sendGroupMessage(reportMessageUtil.replaceReportPlaceholders(reportClosedNotification, report), permissionReportUpdateNotifications, messages.prefixReports);
    }

    private void notifyReporter(UUID reporterUuid, String title, ReportStatus reportStatus) {
        if (!options.reportConfiguration.getReporterNotifyStatuses().contains(reportStatus)) {
            return;
        }

        Optional<SppPlayer> reporter = playerManager.getOnlinePlayer(reporterUuid);
        reporter.ifPresent(sppPlayer -> {
            JSONMessage message = JavaUtils.buildClickableMessage(
                messages.prefixReports + " " + title,
                messages.reporterViewReportsButton,
                messages.reporterViewReportsButtonTooltip,
                options.reportConfiguration.getMyReportsCmd(),
                permission.has(sppPlayer.getPlayer(), options.reportConfiguration.getMyReportsPermission()));
            message.send(sppPlayer.getPlayer());
        });
    }

}
