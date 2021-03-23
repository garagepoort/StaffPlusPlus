package net.shortninja.staffplus.core.domain.staff.reporting.bungee;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.common.bungee.BungeeClient;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.domain.staff.reporting.ReportNotifier;
import net.shortninja.staffplus.core.domain.staff.reporting.bungee.dto.ReportDeletedBungee;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.Optional;


public class ReportDeletedBungeeListener implements PluginMessageListener {

    private BungeeClient bungeeClient = StaffPlus.get().iocContainer.get(BungeeClient.class);
    private ReportNotifier reportNotifier = StaffPlus.get().iocContainer.get(ReportNotifier.class);
    private Options options = StaffPlus.get().iocContainer.get(Options.class);

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if(options.serverSyncConfiguration.isReportSyncEnabled()) {
            Optional<ReportDeletedBungee> reportCreatedBungee = bungeeClient.handleReceived(channel, Constants.BUNGEE_REPORT_DELETED_CHANNEL, message, ReportDeletedBungee.class);
            reportCreatedBungee.ifPresent(createdBungee -> reportNotifier.notifyStaffReportDeleted(createdBungee.getDeletedByName(), createdBungee.getReport()));
        }
    }
}
