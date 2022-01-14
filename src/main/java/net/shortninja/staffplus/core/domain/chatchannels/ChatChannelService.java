package net.shortninja.staffplus.core.domain.chatchannels;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfig;
import net.shortninja.staffplusplus.chatchannels.ChatChannelMessageSendEvent;
import net.shortninja.staffplusplus.chatchannels.ChatChannelType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@IocBean
public class ChatChannelService {

    @ConfigProperty("%lang%:chatchannels.closed")
    public String chatChannelClosed;

    private final ChatChannelRepository chatChannelRepository;
    private final Options options;
    private final PlayerManager playerManager;
    private final Messages messages;

    public ChatChannelService(ChatChannelRepository chatChannelRepository, Options options, PlayerManager playerManager, Messages messages) {
        this.chatChannelRepository = chatChannelRepository;
        this.options = options;
        this.playerManager = playerManager;
        this.messages = messages;
    }

    public void sendOnChannel(CommandSender sender, String channelId, String message, String chatChannelPrefix, String chatChannelLine, ChatChannelType type, ServerSyncConfig serverSyncConfig) {
        ChatChannel channel = chatChannelRepository.findChatChannel(channelId, type, serverSyncConfig).orElseThrow(() -> new BusinessException("Chat channel not found"));

        chatChannelLine = chatChannelLine
            .replace("%message%", message)
            .replace("%sender%", sender.getName())
            .replace("%channelId%", channelId);
        chatChannelPrefix = chatChannelPrefix
            .replace("%channelId%", channelId);

        sendToAllMembers(chatChannelPrefix, chatChannelLine, channel);
        BukkitUtils.sendEvent(new ChatChannelMessageSendEvent(sender, message, channel));
    }


    public void closeChannel(String channelId, String chatChannelPrefix, ChatChannelType type, ServerSyncConfig serverSyncConfig) {
        ChatChannel chatChannel = chatChannelRepository.findChatChannel(channelId, type, serverSyncConfig).orElseThrow(() -> new BusinessException("Chat channel not found"));
        chatChannelRepository.delete(chatChannel.getId());

        chatChannelPrefix = chatChannelPrefix
            .replace("%channelId%", channelId);

        sendToAllMembers(chatChannelPrefix, chatChannelClosed, chatChannel);
    }

    public void create(String channelId, String chatChannelPrefix, String openingMessage, List<UUID> members, ChatChannelType type) {
        ChatChannel channel = new ChatChannel(channelId, members, type, options.serverName);
        chatChannelRepository.save(channel);


        openingMessage = openingMessage
            .replace("%channelId%", channelId);
        chatChannelPrefix = chatChannelPrefix
            .replace("%channelId%", channelId);

        sendToAllMembers(chatChannelPrefix, openingMessage, channel);
    }

    private void sendToAllMembers(String chatChannelPrefix, String chatChannelLine, ChatChannel channel) {
        List<Player> players = channel.getMembers().stream().map(playerManager::getOnlinePlayer)
            .filter(Optional::isPresent)
            .map(p -> p.get().getPlayer())
            .collect(Collectors.toList());
        messages.send(players, chatChannelLine, chatChannelPrefix);
    }
}
