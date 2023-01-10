package net.shortninja.staffplus.core.reporting.bungee.receive;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitMessageListener;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.common.bungee.BungeeClient;
import net.shortninja.staffplus.core.reporting.bungee.dto.ReportReopenedBungeeDto;
import net.shortninja.staffplus.core.reporting.bungee.events.ReportReopenedBungeeEvent;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.Optional;

import static net.shortninja.staffplus.core.common.Constants.BUNGEE_CORD_CHANNEL;

@IocBukkitMessageListener(
    channel = BUNGEE_CORD_CHANNEL,
    conditionalOnProperty = "isNotEmpty(server-sync-module.report-sync)")
public class ReportReopenBungeeListener implements PluginMessageListener {

    private final BungeeClient bungeeClient;
    private final ServerSyncConfiguration serverSyncConfiguration;

    public ReportReopenBungeeListener(BungeeClient bungeeClient, ServerSyncConfiguration serverSyncConfiguration) {
        this.bungeeClient = bungeeClient;
        this.serverSyncConfiguration = serverSyncConfiguration;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        Optional<ReportReopenedBungeeDto> reopenedBungeeDto = bungeeClient.handleReceived(channel, Constants.BUNGEE_REPORT_REOPEN_CHANNEL, message, ReportReopenedBungeeDto.class);

        if (reopenedBungeeDto.isPresent() && serverSyncConfiguration.reportSyncServers.matchesServer(reopenedBungeeDto.get().getServerName())) {
            Bukkit.getPluginManager().callEvent(new ReportReopenedBungeeEvent(reopenedBungeeDto.get()));
        }
    }
}
