package net.shortninja.staffplus.core.domain.staff.reporting.bungee;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.bungee.BungeeClient;
import net.shortninja.staffplusplus.reports.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import static net.shortninja.staffplus.core.common.Constants.*;

@IocBean(conditionalOnProperty = "server-sync-module.report-sync=true")
public class ReportBungeeNotifier implements Listener {

    private final BungeeClient bungeeClient;

    public ReportBungeeNotifier(BungeeClient bungeeClient) {
        this.bungeeClient = bungeeClient;
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onCreate(CreateReportEvent event) {
        sendBungeeNotification(event.getReport(), BUNGEE_REPORT_CREATED_CHANNEL);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDelete(DeleteReportEvent event) {
        sendBungeeNotification(event.getReport(), BUNGEE_REPORT_DELETED_CHANNEL);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onAccept(AcceptReportEvent event) {
        sendBungeeNotification(event.getReport(), BUNGEE_REPORT_ACCEPTED_CHANNEL);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onResolve(ResolveReportEvent event) {
        sendBungeeNotification(event.getReport(), BUNGEE_REPORT_CLOSED_CHANNEL);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onReject(RejectReportEvent event) {
        sendBungeeNotification(event.getReport(), BUNGEE_REPORT_CLOSED_CHANNEL);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onReopen(ReopenReportEvent event) {
        sendBungeeNotification(event.getReport(), BUNGEE_REPORT_REOPEN_CHANNEL);
    }

    private void sendBungeeNotification(Object report, String channel) {
        Player player = Bukkit.getOnlinePlayers().iterator().next();
        bungeeClient.sendMessage(player, channel, report);
    }

}
