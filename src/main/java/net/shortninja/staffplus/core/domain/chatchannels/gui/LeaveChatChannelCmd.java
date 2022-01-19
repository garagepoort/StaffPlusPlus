package net.shortninja.staffplus.core.domain.chatchannels.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.Command;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.chatchannels.ChatChannelService;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfiguration;
import net.shortninja.staffplusplus.chatchannels.ChatChannelType;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy.NONE;

@Command(
    command = "commands:chatchannels.leave",
    description = "Leave an existing chat channel",
    usage = "[channelName]",
    playerRetrievalStrategy = NONE
)
@IocBean
@IocMultiProvider(SppCommand.class)
public class LeaveChatChannelCmd extends AbstractCmd {

    @ConfigProperty("permissions:chatchannels.leave")
    private String leavePermission;

    private final ChatChannelService chatChannelService;
    private final BukkitUtils bukkitUtils;
    private final ServerSyncConfiguration serverSyncConfiguration;
    private final PlayerManager playerManager;

    public LeaveChatChannelCmd(PermissionHandler permissionHandler,
                               Messages messages,
                               CommandService commandService,
                               ChatChannelService chatChannelService,
                               BukkitUtils bukkitUtils,
                               ServerSyncConfiguration serverSyncConfiguration, PlayerManager playerManager) {
        super(messages, permissionHandler, commandService);
        this.chatChannelService = chatChannelService;
        this.bukkitUtils = bukkitUtils;
        this.serverSyncConfiguration = serverSyncConfiguration;
        this.playerManager = playerManager;
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player, Map<String, String> optionalParameters) {
        String[] split = args[0].split("_");
        ChatChannelType chatChannelType = ChatChannelType.valueOf(split[0]);
        String chatChannelId = split[1];

        permissionHandler.validate(sender, leavePermission + "." + chatChannelType.name().toLowerCase());
        validateIsPlayer(sender);

        Optional<SppPlayer> onlinePlayer = playerManager.getOnlinePlayer(sender.getName());
        bukkitUtils.runTaskAsync(sender, () -> chatChannelService.leaveChannel(onlinePlayer.get(), chatChannelId, chatChannelType, serverSyncConfiguration.getForChatChannelType(chatChannelType)));
        return true;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 1;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.empty();
    }

    @Override
    public List<String> autoComplete(CommandSender sender, String[] args, String[] sppArgs) throws IllegalArgumentException {
        String currentArg = args.length > 0 ? args[args.length - 1] : "";

        if (args.length == 1) {
            return chatChannelService.getAllChannelNames().stream()
                .filter(s -> currentArg.isEmpty() || s.contains(currentArg))
                .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}
