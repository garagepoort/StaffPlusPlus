package net.shortninja.staffplus.core.domain.staff.reporting.bungee;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.common.bungee.BungeeClient;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.domain.staff.reporting.ReportNotifier;
import net.shortninja.staffplus.core.domain.staff.reporting.bungee.dto.ReportDeletedBungee;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.Optional;

import static net.shortninja.staffplus.core.common.Constants.BUNGEE_CORD_CHANNEL;

@IocBean
public class ReportDeletedBungeeListener implements PluginMessageListener {

    private final BungeeClient bungeeClient;
    private final ReportNotifier reportNotifier;
    private final Options options;

    public ReportDeletedBungeeListener(BungeeClient bungeeClient, ReportNotifier reportNotifier, Options options) {
        this.bungeeClient = bungeeClient;
        this.reportNotifier = reportNotifier;
        this.options = options;
        StaffPlus.get().getServer().getMessenger().registerIncomingPluginChannel(StaffPlus.get(), BUNGEE_CORD_CHANNEL, this);
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if(options.serverSyncConfiguration.isReportSyncEnabled()) {
            Optional<ReportDeletedBungee> reportCreatedBungee = bungeeClient.handleReceived(channel, Constants.BUNGEE_REPORT_DELETED_CHANNEL, message, ReportDeletedBungee.class);
            reportCreatedBungee.ifPresent(report -> reportNotifier.notifyStaffReportDeleted(report.getDeletedByName(), report.getReporterName()));
        }
    }
}
