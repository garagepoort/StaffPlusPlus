package net.shortninja.staffplus.core.domain.staff.reporting.chatchannels;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.chatchannels.ChatChannel;
import net.shortninja.staffplus.core.domain.chatchannels.ChatChannelService;
import net.shortninja.staffplusplus.chatchannels.ChatChannelType;
import net.shortninja.staffplusplus.reports.AcceptReportEvent;
import net.shortninja.staffplusplus.reports.IReport;
import net.shortninja.staffplusplus.reports.RejectReportEvent;
import net.shortninja.staffplusplus.reports.ResolveReportEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@IocBean(conditionalOnProperty = "reports-module.chatchannels.enabled=true")
@IocListener
public class ReportChatChannelListener implements Listener {

    @ConfigProperty("%lang%:reports.chatchannel.prefix")
    public String chatChannelPrefix;
    @ConfigProperty("%lang%:reports.chatchannel.chatline")
    public String chatChannelLine;
    @ConfigProperty("%lang%:reports.chatchannel.openingmessage")
    public String chatChannelOpeningMessage;

    private final ChatChannelService chatChannelService;
    private final BukkitUtils bukkitUtils;

    public ReportChatChannelListener(ChatChannelService chatChannelService,
                                     BukkitUtils bukkitUtils) {
        this.chatChannelService = chatChannelService;
        this.bukkitUtils = bukkitUtils;
    }

    @EventHandler
    public void onReportAccepted(AcceptReportEvent reportEvent) {
        bukkitUtils.runTaskAsync(() -> {
            IReport report = reportEvent.getReport();
            Set<UUID> members = new HashSet<>();
            members.add(report.getReporterUuid());
            members.add(report.getStaffUuid());

            Optional<ChatChannel> channel = chatChannelService.findChannel(String.valueOf(report.getId()), ChatChannelType.REPORT);
            if (!channel.isPresent()) {
                chatChannelService.create(
                    String.valueOf(report.getId()),
                    chatChannelPrefix,
                    chatChannelLine,
                    chatChannelOpeningMessage,
                    members,
                    ChatChannelType.REPORT);
            }
        });
    }

    @EventHandler
    public void onReportClosed(ResolveReportEvent reportEvent) {
        bukkitUtils.runTaskAsync(() -> chatChannelService.closeChannel(
            String.valueOf(reportEvent.getReport().getId()),
            ChatChannelType.REPORT));
    }

    @EventHandler
    public void onReportClosed(RejectReportEvent reportEvent) {
        bukkitUtils.runTaskAsync(() -> chatChannelService.closeChannel(
            String.valueOf(reportEvent.getReport().getId()),
            ChatChannelType.REPORT));
    }
}
