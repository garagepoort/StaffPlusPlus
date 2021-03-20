package net.shortninja.staffplus.domain.staff.reporting.bungee;

import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.common.Constants;
import net.shortninja.staffplus.common.bungee.BungeeClient;
import net.shortninja.staffplus.common.config.Options;
import net.shortninja.staffplus.domain.staff.reporting.ReportNotifier;
import net.shortninja.staffplus.domain.staff.reporting.bungee.dto.ReportBungee;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.Optional;


public class ReportCreatedBungeeListener implements PluginMessageListener {

    private BungeeClient bungeeClient = IocContainer.getBungeeClient();
    private ReportNotifier reportNotifier = IocContainer.getReportNotifier();
    private Options options = IocContainer.getOptions();

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if(options.serverSyncConfiguration.isReportSyncEnabled()) {
            Optional<ReportBungee> reportCreatedBungee = bungeeClient.handleReceived(channel, Constants.BUNGEE_REPORT_CREATED_CHANNEL, message, ReportBungee.class);
            reportCreatedBungee.ifPresent(createdBungee -> reportNotifier.notifyStaffReportCreated(createdBungee));
        }
    }
}
