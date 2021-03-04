package net.shortninja.staffplus.staff.reporting;

import me.rayzr522.jsonmessage.JSONMessage;
import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.PlayerManager;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.util.Permission;
import net.shortninja.staffplusplus.reports.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.Optional;

public class ReportChangeReporterNotifier implements Listener {

    private final Options options = IocContainer.getOptions();
    private final PlayerManager playerManager = IocContainer.getPlayerManager();
    private Permission permission = IocContainer.getPermissionHandler();

    public ReportChangeReporterNotifier() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }



    @EventHandler(priority = EventPriority.NORMAL)
    public void handleAcceptReport(AcceptReportEvent event) {
        if (!options.reportConfiguration.getReporterNotifyStatuses().contains(ReportStatus.IN_PROGRESS)) {
            return;
        }

        IReport report = event.getReport();
        Optional<SppPlayer> reporter = playerManager.getOnlinePlayer(report.getReporterUuid());
        reporter.ifPresent(sppPlayer -> buildMessage(sppPlayer.getPlayer(), "Your report has been accepted by " + report.getStaffName()));
    }


    @EventHandler(priority = EventPriority.NORMAL)
    public void handleRejectReport(RejectReportEvent event) {
        if (!options.reportConfiguration.getReporterNotifyStatuses().contains(ReportStatus.REJECTED)) {
            return;
        }

        IReport report = event.getReport();
        Optional<SppPlayer> reporter = playerManager.getOnlinePlayer(report.getReporterUuid());
        reporter.ifPresent(sppPlayer -> buildMessage(sppPlayer.getPlayer(), "Your report has been rejected by " + report.getStaffName()));
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleResolveReport(ResolveReportEvent event) {
        if (!options.reportConfiguration.getReporterNotifyStatuses().contains(ReportStatus.RESOLVED)) {
            return;
        }

        IReport report = event.getReport();
        Optional<SppPlayer> reporter = playerManager.getOnlinePlayer(report.getReporterUuid());
        reporter.ifPresent(sppPlayer -> buildMessage(sppPlayer.getPlayer(), "Your report has been resolved by " + report.getStaffName()));
    }

    private void buildMessage(Player player, String title) {
        JSONMessage message = JSONMessage.create(title)
            .color(ChatColor.GOLD);

        if (permission.has(player, options.reportConfiguration.getMyReportsPermission())) {
            message.then(" View your reports!")
                .color(ChatColor.BLUE)
                .tooltip("Click to view your reports")
                .runCommand("/" + options.reportConfiguration.getMyReportsCmd());
        }

        message.send(player);
    }
}
