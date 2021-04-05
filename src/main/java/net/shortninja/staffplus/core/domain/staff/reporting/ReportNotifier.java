package net.shortninja.staffplus.core.domain.staff.reporting;

import be.garagepoort.mcioc.IocBean;
import me.rayzr522.jsonmessage.JSONMessage;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplusplus.reports.ReportStatus;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static net.shortninja.staffplusplus.reports.ReportStatus.REJECTED;
import static net.shortninja.staffplusplus.reports.ReportStatus.RESOLVED;

@IocBean
public class ReportNotifier {

    private final PermissionHandler permission;
    private final Options options;
    private final Messages messages;
    private final PlayerManager playerManager;

    public ReportNotifier(PermissionHandler permission, Options options, Messages messages, PlayerManager playerManager) {
        this.permission = permission;
        this.options = options;
        this.messages = messages;
        this.playerManager = playerManager;
    }

    public void notifyStaffReportDeleted(String deletedByName, String reporterName) {
        messages.sendGroupMessage(deletedByName + " deleted report from " + reporterName, options.permissionReportUpdateNotifications, messages.prefixReports);
    }

    public void notifyReportReopen(String player, String reporterName) {
        messages.sendGroupMessage(player + " reopened report from " + reporterName, options.permissionReportUpdateNotifications, messages.prefixReports);
    }

    public void sendCreatedMessages(String reporterName, String culpritName, String reason) {
        messages.sendGroupMessage(messages.reportedStaff
            .replace("%target%", reporterName)
            .replace("%player%", culpritName == null ? "Unknown" : culpritName)
            .replace("%reason%", reason), options.permissionReportUpdateNotifications, messages.prefixReports);
        options.reportConfiguration.getSound().ifPresent(s -> s.playForGroup(options.permissionReportUpdateNotifications));
    }

    public void sendAcceptedMessages(String staffName, String reporterName, UUID reporterUuid) {
        messages.sendGroupMessage(staffName + " accepted report from " + reporterName, options.permissionReportUpdateNotifications, messages.prefixReports);
        if (options.reportConfiguration.getReporterNotifyStatuses().contains(ReportStatus.IN_PROGRESS)) {
            Optional<SppPlayer> reporter = playerManager.getOnlinePlayer(reporterUuid);
            reporter.ifPresent(sppPlayer -> buildMessage(sppPlayer.getPlayer(), "Your report has been accepted by " + staffName));
        }
    }

    public void sendClosedMessages(String staffName, ReportStatus reportStatus, String reporterName, UUID reporterUuid) {
        messages.sendGroupMessage(staffName + " changed report status to " + reportStatus + ". Reporter: " + reporterName, options.permissionReportUpdateNotifications, messages.prefixReports);

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
