package net.shortninja.staffplus.core.domain.staff.reporting.bungee;

import be.garagepoort.mcioc.IocContainer;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.common.bungee.BungeeClient;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.domain.staff.reporting.ReportNotifier;
import net.shortninja.staffplus.core.domain.staff.reporting.bungee.dto.ReportReopenedBungee;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.Optional;


public class ReportReopenBungeeListener implements PluginMessageListener {

    private BungeeClient bungeeClient = IocContainer.get(BungeeClient.class);
    private ReportNotifier reportNotifier = IocContainer.get(ReportNotifier.class);
    private Options options = IocContainer.get(Options.class);

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if(options.serverSyncConfiguration.isReportSyncEnabled()) {
            Optional<ReportReopenedBungee> reportCreatedBungee = bungeeClient.handleReceived(channel, Constants.BUNGEE_REPORT_REOPEN_CHANNEL, message, ReportReopenedBungee.class);
            reportCreatedBungee.ifPresent(createdBungee -> reportNotifier.notifyReportReopen(createdBungee.getReopenByName(), createdBungee.getReport()));
        }
    }
}
