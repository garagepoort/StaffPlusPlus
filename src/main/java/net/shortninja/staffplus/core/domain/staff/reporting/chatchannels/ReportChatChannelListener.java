package net.shortninja.staffplus.core.domain.staff.reporting.chatchannels;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.chatchannels.ChatChannelService;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfiguration;
import net.shortninja.staffplusplus.chatchannels.ChatChannelType;
import net.shortninja.staffplusplus.reports.AcceptReportEvent;
import net.shortninja.staffplusplus.reports.IReport;
import net.shortninja.staffplusplus.reports.ResolveReportEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@IocBean
@IocListener
public class ReportChatChannelListener implements Listener {

    private final ChatChannelService chatChannelService;
    private final ServerSyncConfiguration serverSyncConfiguration;
    private final BukkitUtils bukkitUtils;

    public ReportChatChannelListener(ChatChannelService chatChannelService,
                                     ServerSyncConfiguration serverSyncConfiguration,
                                     BukkitUtils bukkitUtils) {
        this.chatChannelService = chatChannelService;
        this.serverSyncConfiguration = serverSyncConfiguration;
        this.bukkitUtils = bukkitUtils;
    }

    @EventHandler
    public void onReportAccepted(AcceptReportEvent reportEvent) {
        IReport report = reportEvent.getReport();
        List<UUID> members = Arrays.asList(report.getReporterUuid(), report.getStaffUuid());

        chatChannelService.create(String.valueOf(report.getId()), members, ChatChannelType.REPORT);
    }

    @EventHandler
    public void onReportClosed(ResolveReportEvent reportEvent) {
        bukkitUtils.runTaskAsync(() -> chatChannelService.delete(String.valueOf(reportEvent.getReport().getId()), ChatChannelType.REPORT, serverSyncConfiguration.reportSyncServers));
    }
}
