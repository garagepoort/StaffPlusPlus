package net.shortninja.staffplus.core.domain.staff.reporting;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import me.rayzr522.jsonmessage.JSONMessage;
import net.shortninja.staffplus.core.StaffPlusPlus;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.StaffPlusPlusJoinedEvent;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.reporting.config.ReportConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.stream.Collectors;

import static org.bukkit.Bukkit.getScheduler;

@IocBukkitListener
public class ReportListener implements Listener {

    private final ReportService reportService;
    private final PermissionHandler permission;
    private final ReportConfiguration reportConfiguration;

    public ReportListener(ReportService reportService, PermissionHandler permission, ReportConfiguration reportConfiguration) {
        this.reportService = reportService;
        this.permission = permission;
        this.reportConfiguration = reportConfiguration;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void notifyReports(StaffPlusPlusJoinedEvent event) {
        if (!reportConfiguration.isNotifyReporterOnJoin()) {
            return;
        }
        getScheduler().runTaskAsynchronously(StaffPlusPlus.get(), () -> {
            List<Report> reports = reportService.getMyReports(event.getPlayer().getUniqueId());
            List<Report> openReports = reports.stream().filter(r -> !r.getReportStatus().isClosed()).collect(Collectors.toList());

            if (!openReports.isEmpty()) {
                JSONMessage message = JavaUtils.buildClickableMessage(
                    "You have " + openReports.size() + " open reports",
                    "View your reports!",
                    "Click to view your reports",
                    reportConfiguration.getMyReportsCmd(),
                    permission.has(event.getPlayer(), reportConfiguration.getMyReportsPermission()));
                message.send(event.getPlayer());
            }
        });
    }
}
