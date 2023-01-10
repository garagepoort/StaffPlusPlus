package net.shortninja.staffplus.core.reporting.chatchannels;

import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.configuration.ConfigTransformer;
import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.chatchannels.ChatChannel;
import net.shortninja.staffplus.core.domain.chatchannels.ChatChannelService;
import net.shortninja.staffplus.core.reporting.config.ReportStatusesConfigTransformer;
import net.shortninja.staffplusplus.chatchannels.ChatChannelType;
import net.shortninja.staffplusplus.reports.AcceptReportEvent;
import net.shortninja.staffplusplus.reports.CreateReportEvent;
import net.shortninja.staffplusplus.reports.DeleteReportEvent;
import net.shortninja.staffplusplus.reports.IReport;
import net.shortninja.staffplusplus.reports.RejectReportEvent;
import net.shortninja.staffplusplus.reports.ReopenReportEvent;
import net.shortninja.staffplusplus.reports.ReportStatus;
import net.shortninja.staffplusplus.reports.ResolveReportEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.Optional;

@IocBukkitListener(conditionalOnProperty = "reports-module.chatchannels.enabled=true")
public class ReportChatChannelListener implements Listener {

    @ConfigProperty("reports-module.chatchannels.open-on")
    @ConfigTransformer(ReportStatusesConfigTransformer.class)
    private List<ReportStatus> chatchannelsOpenStatuses;
    @ConfigProperty("reports-module.chatchannels.close-on")
    @ConfigTransformer(ReportStatusesConfigTransformer.class)
    private List<ReportStatus> chatchannelsCloseStatuses;

    private final ChatChannelService chatChannelService;
    private final ReportChatChannelService reportChatChannelService;
    private final BukkitUtils bukkitUtils;

    public ReportChatChannelListener(ChatChannelService chatChannelService,
                                     ReportChatChannelService reportChatChannelService, BukkitUtils bukkitUtils) {
        this.chatChannelService = chatChannelService;
        this.reportChatChannelService = reportChatChannelService;
        this.bukkitUtils = bukkitUtils;
    }

    private void openChannel(IReport report) {
        Optional<ChatChannel> channel = chatChannelService.findChannel(String.valueOf(report.getId()), ChatChannelType.REPORT);
        if (!channel.isPresent()) {
            reportChatChannelService.openChannel(report);
        }
    }

    @EventHandler
    public void onReportAccepted(AcceptReportEvent reportEvent) {
        openOrCloseChannels(ReportStatus.IN_PROGRESS, reportEvent.getReport());
    }

    @EventHandler
    public void onReportResolved(ResolveReportEvent reportEvent) {
        openOrCloseChannels(ReportStatus.RESOLVED, reportEvent.getReport());
    }

    @EventHandler
    public void onReportRejected(RejectReportEvent reportEvent) {
        openOrCloseChannels(ReportStatus.REJECTED, reportEvent.getReport());
    }

    @EventHandler
    public void onReportOpened(CreateReportEvent reportEvent) {
        openOrCloseChannels(ReportStatus.OPEN, reportEvent.getReport());
    }

    @EventHandler
    public void onReportReopened(ReopenReportEvent reportEvent) {
        openOrCloseChannels(ReportStatus.OPEN, reportEvent.getReport());
    }

    @EventHandler
    public void onReportDeleted(DeleteReportEvent reportEvent) {
        openOrCloseChannels(ReportStatus.DELETED, reportEvent.getReport());
    }

    private void openOrCloseChannels(ReportStatus status, IReport report) {
        bukkitUtils.runTaskAsync(() -> {
            if (chatchannelsOpenStatuses.contains(status)) {
                openChannel(report);
            }
            if (chatchannelsCloseStatuses.contains(status)) {
                closeChannel(report);
            }
        });
    }

    private void closeChannel(IReport report) {
        chatChannelService.closeChannel(
            String.valueOf(report.getId()),
            ChatChannelType.REPORT);
    }
}
