package net.shortninja.staffplus.core.domain.staff.reporting.chatchannels;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.domain.chatchannels.ChatChannel;
import net.shortninja.staffplus.core.domain.chatchannels.ChatChannelService;
import net.shortninja.staffplusplus.chatchannels.ChatChannelType;
import net.shortninja.staffplusplus.reports.IReport;
import net.shortninja.staffplusplus.session.SppInteractor;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static net.shortninja.staffplus.core.common.Constants.CONSOLE_UUID;

@IocBean
public class ReportChatChannelService {
    @ConfigProperty("%lang%:reports.chatchannel.prefix")
    public String chatChannelPrefix;
    @ConfigProperty("%lang%:reports.chatchannel.chatline")
    public String chatChannelLine;
    @ConfigProperty("%lang%:reports.chatchannel.openingmessage")
    public String chatChannelOpeningMessage;

    private final ChatChannelService chatChannelService;

    public ReportChatChannelService(ChatChannelService chatChannelService) {
        this.chatChannelService = chatChannelService;
    }

    public void openChannel(IReport report) {
        openChannel(null, report);
    }

    public void openChannel(SppInteractor creator, IReport report) {
        Set<UUID> members = new HashSet<>();
        members.add(report.getReporterUuid());
        if (creator != null && !creator.getId().equals(CONSOLE_UUID)) {
            members.add(creator.getId());
        }
        if (report.getStaffUuid() != null) {
            members.add(report.getStaffUuid());
        }

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
    }
}
