package net.shortninja.staffplus.core.domain.staff.reporting;

import me.rayzr522.jsonmessage.JSONMessage;
import net.shortninja.staffplus.core.StaffPlus;
import be.garagepoort.mcioc.IocContainer;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;
import java.util.stream.Collectors;

import static org.bukkit.Bukkit.getScheduler;

public class ReportListener implements Listener {

    private final ReportService reportService = IocContainer.get(ReportService.class);
    private final Options options = IocContainer.get(Options.class);
    private final PermissionHandler permission = IocContainer.get(PermissionHandler.class);

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
                JSONMessage message = JavaUtils.buildClickableMessage(
                    "You have " + openReports.size() + " open reports",
                    "View your reports!",
                    "Click to view your reports",
                    options.reportConfiguration.getMyReportsCmd(),
                    permission.has(event.getPlayer(), options.reportConfiguration.getMyReportsPermission()));
                message.send(event.getPlayer());
            }
        });
    }
}
