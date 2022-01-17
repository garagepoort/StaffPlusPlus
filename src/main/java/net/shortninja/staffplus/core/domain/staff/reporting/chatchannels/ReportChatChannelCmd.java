package net.shortninja.staffplus.core.domain.staff.reporting.chatchannels;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.Command;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.chatchannels.ChatChannelService;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfiguration;
import net.shortninja.staffplusplus.chatchannels.ChatChannelType;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;

import java.util.Map;
import java.util.Optional;

import static net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy.NONE;

@Command(
    command = "commands:reports.chat",
    permissions = "permissions:reports.chat",
    description = "Send a message on your chat channel",
    usage = "[channelId] [message]",
    playerRetrievalStrategy = NONE
)
@IocBean
@IocMultiProvider(SppCommand.class)
public class ReportChatChannelCmd extends AbstractCmd {

    private final ChatChannelService chatChannelService;
    private final BukkitUtils bukkitUtils;
    private final ServerSyncConfiguration serverSyncConfiguration;

    public ReportChatChannelCmd(PermissionHandler permissionHandler,
                                Messages messages,
                                CommandService commandService,
                                ChatChannelService chatChannelService, BukkitUtils bukkitUtils,
                                ServerSyncConfiguration serverSyncConfiguration) {
        super(messages, permissionHandler, commandService);
        this.chatChannelService = chatChannelService;
        this.bukkitUtils = bukkitUtils;
        this.serverSyncConfiguration = serverSyncConfiguration;
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer targetPlayer, Map<String, String> optionalParameters) {
        String channelId = args[0];
        String message = args[1];
        bukkitUtils.runTaskAsync(sender, () -> {
            chatChannelService.sendOnChannel(sender,
                channelId,
                message,
                ChatChannelType.REPORT,
                serverSyncConfiguration.reportSyncServers);
        });
        return true;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 2;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.empty();
    }
}