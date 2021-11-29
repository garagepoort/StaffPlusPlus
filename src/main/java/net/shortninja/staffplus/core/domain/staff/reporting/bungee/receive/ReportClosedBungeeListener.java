package net.shortninja.staffplus.core.domain.staff.reporting.bungee.receive;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMessageListener;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.common.bungee.BungeeClient;
import net.shortninja.staffplus.core.domain.staff.reporting.bungee.dto.ReportBungeeDto;
import net.shortninja.staffplus.core.domain.staff.reporting.bungee.events.ReportClosedBungeeEvent;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.Optional;

import static net.shortninja.staffplus.core.common.Constants.BUNGEE_CORD_CHANNEL;

@IocBean(conditionalOnProperty = "isNotEmpty(server-sync-module.report-sync)")
@IocMessageListener(channel = BUNGEE_CORD_CHANNEL)
public class ReportClosedBungeeListener implements PluginMessageListener {

    private final BungeeClient bungeeClient;
    private final ServerSyncConfiguration serverSyncConfiguration;

    public ReportClosedBungeeListener(BungeeClient bungeeClient, ServerSyncConfiguration serverSyncConfiguration) {
        this.bungeeClient = bungeeClient;
        this.serverSyncConfiguration = serverSyncConfiguration;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        Optional<ReportBungeeDto> reportCreatedBungee = bungeeClient.handleReceived(channel, Constants.BUNGEE_REPORT_CLOSED_CHANNEL, message, ReportBungeeDto.class);

        if (reportCreatedBungee.isPresent() && serverSyncConfiguration.reportSyncServers.matchesServer(reportCreatedBungee.get().getServerName())) {
            Bukkit.getPluginManager().callEvent(new ReportClosedBungeeEvent(reportCreatedBungee.get()));
        }
    }
}
