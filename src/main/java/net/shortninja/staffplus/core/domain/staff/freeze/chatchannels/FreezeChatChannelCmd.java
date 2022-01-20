package net.shortninja.staffplus.core.domain.staff.freeze.chatchannels;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.Command;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.chatchannels.ChatChannelService;
import net.shortninja.staffplusplus.chatchannels.ChatChannelType;
import net.shortninja.staffplusplus.session.IPlayerSession;
import net.shortninja.staffplusplus.session.SppInteractor;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy.NONE;

@Command(
    command = "commands:freezechannel.chat",
    permissions = "permissions:freeze-chat",
    description = "Send a message on your freeze chat channel",
    usage = "[channelId] [message]",
    playerRetrievalStrategy = NONE
)
@IocBean(conditionalOnProperty = "freeze-module.chatchannels.enabled=true")
@IocMultiProvider(SppCommand.class)
public class FreezeChatChannelCmd extends AbstractCmd {

    private final ChatChannelService chatChannelService;
    private final BukkitUtils bukkitUtils;
    private final OnlineSessionsManager onlineSessionsManager;

    public FreezeChatChannelCmd(PermissionHandler permissionHandler,
                                Messages messages,
                                CommandService commandService,
                                ChatChannelService chatChannelService, BukkitUtils bukkitUtils, OnlineSessionsManager onlineSessionsManager) {
        super(messages, permissionHandler, commandService);
        this.chatChannelService = chatChannelService;
        this.bukkitUtils = bukkitUtils;
        this.onlineSessionsManager = onlineSessionsManager;
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer targetPlayer, Map<String, String> optionalParameters) {
        UUID senderUuid = sender instanceof Player ? ((Player) sender).getUniqueId() : Constants.CONSOLE_UUID;
        String senderName = sender instanceof Player ? sender.getName() : "Console";
        SppInteractor sppInteractor = new SppInteractor(senderUuid, senderName, sender);

        IPlayerSession session = onlineSessionsManager.get(senderUuid);
        String channelId;
        String message;

        if(session.isFrozen()) {
            channelId = senderName;
            message = JavaUtils.compileWords(args, 0);
        } else {
            channelId = args[0];
            message = JavaUtils.compileWords(args, 1);
        }

        bukkitUtils.runTaskAsync(sender, () -> {
            chatChannelService.sendOnChannel(sppInteractor,
                channelId,
                message,
                ChatChannelType.FREEZE);
        });
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
        UUID senderUuid = sender instanceof Player ? ((Player) sender).getUniqueId() : Constants.CONSOLE_UUID;
        IPlayerSession session = onlineSessionsManager.get(senderUuid);

        String currentArg = args.length > 0 ? args[args.length - 1] : "";
        if (!session.isFrozen() && args.length == 1) {
            if (sender instanceof Player) {
                return chatChannelService.getMyChannelIds((Player) sender, ChatChannelType.FREEZE).stream()
                    .filter(s -> currentArg.isEmpty() || s.contains(currentArg))
                    .collect(Collectors.toList());
            } else {
                return chatChannelService.getAllChannelIds(ChatChannelType.FREEZE).stream()
                    .filter(s -> currentArg.isEmpty() || s.contains(currentArg))
                    .collect(Collectors.toList());
            }
        }

        return Collections.emptyList();
    }
}