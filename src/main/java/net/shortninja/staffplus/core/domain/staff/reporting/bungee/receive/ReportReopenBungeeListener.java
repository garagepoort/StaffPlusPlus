package net.shortninja.staffplus.core.domain.staff.reporting.bungee.receive;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMessageListener;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.common.bungee.BungeeClient;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.domain.staff.reporting.bungee.dto.ReportReopenedBungeeDto;
import net.shortninja.staffplus.core.domain.staff.reporting.bungee.events.ReportReopenedBungeeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.Optional;

import static net.shortninja.staffplus.core.common.Constants.BUNGEE_CORD_CHANNEL;

@IocBean(conditionalOnProperty = "server-sync-module.report-sync=true")
@IocMessageListener(channel = BUNGEE_CORD_CHANNEL)
public class ReportReopenBungeeListener implements PluginMessageListener {

    private final BungeeClient bungeeClient;
    private final Options options;

    public ReportReopenBungeeListener(BungeeClient bungeeClient, Options options) {
        this.bungeeClient = bungeeClient;
        this.options = options;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (options.serverSyncConfiguration.isReportSyncEnabled()) {
            Optional<ReportReopenedBungeeDto> reopenedBungeeDto = bungeeClient.handleReceived(channel, Constants.BUNGEE_REPORT_REOPEN_CHANNEL, message, ReportReopenedBungeeDto.class);
            reopenedBungeeDto.ifPresent(b -> Bukkit.getPluginManager().callEvent(new ReportReopenedBungeeEvent(b)));
        }
    }
}
