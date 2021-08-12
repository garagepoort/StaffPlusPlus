package net.shortninja.staffplus.core.domain.staff.reporting;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import me.rayzr522.jsonmessage.JSONMessage;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.StaffPlusPlusJoinedEvent;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.stream.Collectors;

import static org.bukkit.Bukkit.getScheduler;

@IocBean
@IocListener
public class ReportListener implements Listener {

    private final ReportService reportService;
    private final Options options;
    private final PermissionHandler permission;

    public ReportListener(ReportService reportService, Options options, PermissionHandler permission) {
        this.reportService = reportService;
        this.options = options;
        this.permission = permission;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void notifyReports(StaffPlusPlusJoinedEvent event) {
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
