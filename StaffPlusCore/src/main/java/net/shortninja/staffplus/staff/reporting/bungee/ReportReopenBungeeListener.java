package net.shortninja.staffplus.staff.reporting.bungee;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.common.Constants;
import net.shortninja.staffplus.common.bungee.BungeeClient;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.reporting.ReportNotifier;
import net.shortninja.staffplus.staff.reporting.bungee.dto.ReportReopenedBungee;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.Optional;


public class ReportReopenBungeeListener implements PluginMessageListener {

    private BungeeClient bungeeClient = IocContainer.getBungeeClient();
    private ReportNotifier reportNotifier = IocContainer.getReportNotifier();
    private Options options = IocContainer.getOptions();

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if(options.serverSyncConfiguration.isReportSyncEnabled()) {
            Optional<ReportReopenedBungee> reportCreatedBungee = bungeeClient.handleReceived(channel, Constants.BUNGEE_REPORT_REOPEN_CHANNEL, message, ReportReopenedBungee.class);
            reportCreatedBungee.ifPresent(createdBungee -> reportNotifier.notifyReportReopen(createdBungee.getReopenByName(), createdBungee.getReport()));
        }
    }
}
