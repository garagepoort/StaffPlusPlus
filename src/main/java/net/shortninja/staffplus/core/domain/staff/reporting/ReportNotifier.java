package net.shortninja.staffplus.core.domain.staff.reporting;

import me.rayzr522.jsonmessage.JSONMessage;
import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.bungee.BungeeClient;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.utils.MessageCoordinator;
import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.reporting.bungee.dto.ReportBungee;
import net.shortninja.staffplus.core.domain.staff.reporting.bungee.dto.ReportDeletedBungee;
import net.shortninja.staffplus.core.domain.staff.reporting.bungee.dto.ReportReopenedBungee;
import net.shortninja.staffplusplus.reports.ReportStatus;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static net.shortninja.staffplus.core.common.Constants.*;
import static net.shortninja.staffplusplus.reports.ReportStatus.REJECTED;
import static net.shortninja.staffplusplus.reports.ReportStatus.RESOLVED;

@IocBean
public class ReportNotifier {

    private final PermissionHandler permission;
    private final BungeeClient bungeeClient;
    private final Options options;
    private final Messages messages;
    private final MessageCoordinator message;
    private final PlayerManager playerManager;


    public ReportNotifier(PermissionHandler permission, BungeeClient bungeeClient, Options options, Messages messages, MessageCoordinator message, PlayerManager playerManager) {
        this.permission = permission;
        this.bungeeClient = bungeeClient;
        this.options = options;
        this.messages = messages;
        this.message = message;
        this.playerManager = playerManager;
    }

    public void notifyStaffReportCreated(ReportBungee report) {
        sendCreatedMessages(report.getReporterName(), report.getCulpritName(), report.getReason());
    }

    public void notifyStaffReportCreated(Report report, boolean sendOnBungee) {
        if (sendOnBungee) {
            sendBungeeNotification(report, BUNGEE_REPORT_CREATED_CHANNEL);
        }
        sendCreatedMessages(report.getReporterName(), report.getCulpritName(), report.getReason());
    }

    public void notifyStaffReportDeleted(String deletedByName, ReportBungee report) {
        message.sendGroupMessage(deletedByName + " deleted report from " + report.getReporterName(), options.permissionReportUpdateNotifications, messages.prefixReports);
    }

    public void notifyStaffReportDeleted(String deletedByName, Report report, boolean sendOnBungee) {
        if (sendOnBungee) {
            sendBungeeNotification(new ReportDeletedBungee(report, deletedByName), BUNGEE_REPORT_DELETED_CHANNEL);
        }
        message.sendGroupMessage(deletedByName + " deleted report from " + report.getReporterName(), options.permissionReportUpdateNotifications, messages.prefixReports);
    }

    public void notifyReportAccepted(ReportBungee report) {
        sendAcceptedMessages(report.getStaffName(), report.getReporterName(), report.getReporterUuid());
    }

    public void notifyReportAccepted(Report report, boolean sendOnBungee) {
        if (sendOnBungee) {
            sendBungeeNotification(report, BUNGEE_REPORT_ACCEPTED_CHANNEL);
        }
        sendAcceptedMessages(report.getStaffName(), report.getReporterName(), report.getReporterUuid());
    }

    private void sendCreatedMessages(String reporterName, String culpritName, String reason) {
        message.sendGroupMessage(messages.reportedStaff
            .replace("%target%", reporterName)
            .replace("%player%", culpritName == null ? "Unknown" : culpritName)
            .replace("%reason%", reason), options.permissionReportUpdateNotifications, messages.prefixReports);
        options.reportConfiguration.getSound().ifPresent(s -> s.playForGroup(options.permissionReportUpdateNotifications));
    }

    private void sendAcceptedMessages(String staffName, String reporterName, UUID reporterUuid) {
        message.sendGroupMessage(staffName + " accepted report from " + reporterName, options.permissionReportUpdateNotifications, messages.prefixReports);
        if (options.reportConfiguration.getReporterNotifyStatuses().contains(ReportStatus.IN_PROGRESS)) {
            Optional<SppPlayer> reporter = playerManager.getOnlinePlayer(reporterUuid);
            reporter.ifPresent(sppPlayer -> buildMessage(sppPlayer.getPlayer(), "Your report has been accepted by " + staffName));
        }
    }

    public void notifyReportClosed(ReportBungee report) {
        sendClosedMessages(report.getStaffName(), report.getReportStatus(), report.getReporterName(), report.getReporterUuid());
    }

    public void notifyReportClosed(Report report, boolean sendOnBungee) {
        if (sendOnBungee) {
            sendBungeeNotification(report, BUNGEE_REPORT_CLOSED_CHANNEL);
        }
        sendClosedMessages(report.getStaffName(), report.getReportStatus(), report.getReporterName(), report.getReporterUuid());
    }

    private void sendClosedMessages(String staffName, ReportStatus reportStatus, String reporterName, UUID reporterUuid) {
        message.sendGroupMessage(staffName + " changed report status to " + reportStatus + ". Reporter: " + reporterName, options.permissionReportUpdateNotifications, messages.prefixReports);

        Optional<SppPlayer> reporter = playerManager.getOnlinePlayer(reporterUuid);
        reporter.ifPresent(sppPlayer -> {
            List<ReportStatus> reporterNotifyStatuses = options.reportConfiguration.getReporterNotifyStatuses();
            if (reporterNotifyStatuses.contains(REJECTED) && reportStatus == REJECTED) {
                buildMessage(sppPlayer.getPlayer(), "Your report has been rejected by " + staffName);
            } else if (reporterNotifyStatuses.contains(RESOLVED) && reportStatus == RESOLVED) {
                buildMessage(sppPlayer.getPlayer(), "Your report has been resolved by " + staffName);
            }
        });
    }


    public void notifyReportReopen(String player, ReportBungee report) {
        message.sendGroupMessage(player + " reopened report from " + report.getReporterName(), options.permissionReportUpdateNotifications, messages.prefixReports);
    }

    public void notifyReportReopen(String player, Report report, boolean sendOnBungee) {
        if (sendOnBungee) {
            sendBungeeNotification(new ReportReopenedBungee(report, player), BUNGEE_REPORT_REOPEN_CHANNEL);
        }
        message.sendGroupMessage(player + " reopened report from " + report.getReporterName(), options.permissionReportUpdateNotifications, messages.prefixReports);
    }

    private void sendBungeeNotification(Object report, String channel) {
        Player player = Bukkit.getOnlinePlayers().iterator().next();
        bungeeClient.sendMessage(player, channel, report);
    }

    private void buildMessage(Player player, String title) {
        JSONMessage message = JavaUtils.buildClickableMessage(
            title,
            "View your reports!",
            "Click to view your reports",
            options.reportConfiguration.getMyReportsCmd(),
            permission.has(player, options.reportConfiguration.getMyReportsPermission()));
        message.send(player);
    }
}
