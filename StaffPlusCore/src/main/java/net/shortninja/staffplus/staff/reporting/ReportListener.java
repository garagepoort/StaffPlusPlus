package net.shortninja.staffplus.staff.reporting;

import me.rayzr522.jsonmessage.JSONMessage;
import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.util.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;
import java.util.stream.Collectors;

import static org.bukkit.Bukkit.getScheduler;

public class ReportListener implements Listener {

    private final ReportService reportService = IocContainer.getReportService();
    private final Options options = IocContainer.getOptions();
    private final Permission permission = IocContainer.getPermissionHandler();

    public ReportListener() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void notifyReports(PlayerJoinEvent event) {
        if (!options.reportConfiguration.isNotifyReporterOnJoin()) {
            return;
        }
        getScheduler().runTaskAsynchronously(StaffPlus.get(), () -> {
            List<Report> reports = reportService.getMyReports(event.getPlayer().getUniqueId());
            List<Report> openReports = reports.stream().filter(r -> !r.getReportStatus().isClosed()).collect(Collectors.toList());

            if (!openReports.isEmpty()) {
                JSONMessage message = JSONMessage.create("You have " + openReports.size() + " open reports")
                    .color(ChatColor.GOLD);

                if (permission.has(event.getPlayer(), options.reportConfiguration.getMyReportsPermission())) {
                    message.then(" View your reports!")
                        .color(ChatColor.BLUE)
                        .tooltip("Click to view your reports")
                        .runCommand("/" + options.reportConfiguration.getMyReportsCmd());
                }
                message.send(event.getPlayer());
            }
        });
    }
}
