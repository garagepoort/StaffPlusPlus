package net.shortninja.staffplus.core.domain.staff.reporting.bungee;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.bungee.BungeeClient;
import net.shortninja.staffplus.core.domain.staff.reporting.bungee.dto.ReportBungeeDto;
import net.shortninja.staffplus.core.domain.staff.reporting.bungee.dto.ReportDeletedBungeeDto;
import net.shortninja.staffplus.core.domain.staff.reporting.bungee.dto.ReportReopenedBungeeDto;
import net.shortninja.staffplusplus.reports.AcceptReportEvent;
import net.shortninja.staffplusplus.reports.CreateReportEvent;
import net.shortninja.staffplusplus.reports.DeleteReportEvent;
import net.shortninja.staffplusplus.reports.IReport;
import net.shortninja.staffplusplus.reports.RejectReportEvent;
import net.shortninja.staffplusplus.reports.ReopenReportEvent;
import net.shortninja.staffplusplus.reports.ResolveReportEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.shortninja.staffplus.core.common.Constants.BUNGEE_REPORT_ACCEPTED_CHANNEL;
import static net.shortninja.staffplus.core.common.Constants.BUNGEE_REPORT_CLOSED_CHANNEL;
import static net.shortninja.staffplus.core.common.Constants.BUNGEE_REPORT_CREATED_CHANNEL;
import static net.shortninja.staffplus.core.common.Constants.BUNGEE_REPORT_DELETED_CHANNEL;
import static net.shortninja.staffplus.core.common.Constants.BUNGEE_REPORT_REOPEN_CHANNEL;

@IocBean
public class ReportBungeeNotifier implements Listener {

    private final BungeeClient bungeeClient;

    public ReportBungeeNotifier(BungeeClient bungeeClient) {
        this.bungeeClient = bungeeClient;
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler
    public void onCreate(CreateReportEvent event) {
        sendBungeeNotification(event.getReport(), BUNGEE_REPORT_CREATED_CHANNEL);
    }

    @EventHandler
    public void onDelete(DeleteReportEvent event) {
        Player player = Bukkit.getOnlinePlayers().iterator().next();
        bungeeClient.sendMessage(player, BUNGEE_REPORT_DELETED_CHANNEL, new ReportDeletedBungeeDto(event.getReport(), event.getDeletedByName()));
    }

    @EventHandler
    public void onAccept(AcceptReportEvent event) {
        sendBungeeNotification(event.getReport(), BUNGEE_REPORT_ACCEPTED_CHANNEL);
    }

    @EventHandler
    public void onResolve(ResolveReportEvent event) {
        sendBungeeNotification(event.getReport(), BUNGEE_REPORT_CLOSED_CHANNEL);
    }

    @EventHandler
    public void onReject(RejectReportEvent event) {
        sendBungeeNotification(event.getReport(), BUNGEE_REPORT_CLOSED_CHANNEL);
    }

    @EventHandler
    public void onReopen(ReopenReportEvent event) {
        Player player = Bukkit.getOnlinePlayers().iterator().next();
        bungeeClient.sendMessage(player, BUNGEE_REPORT_REOPEN_CHANNEL, new ReportReopenedBungeeDto(event.getReport(), event.getReopenedByName()));
    }

    private void sendBungeeNotification(IReport report, String channel) {
        Player player = Bukkit.getOnlinePlayers().iterator().next();
        bungeeClient.sendMessage(player, channel, new ReportBungeeDto(report));
    }

}
