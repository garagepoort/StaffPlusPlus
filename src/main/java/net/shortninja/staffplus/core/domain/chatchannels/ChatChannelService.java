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
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@IocBean
public class ChatChannelService {

    @ConfigProperty("%lang%:chatchannels.closed")
    public String chatChannelClosed;
    @ConfigProperty("%lang%:chatchannels.joined")
    public String chatChannelJoined;

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

    public void sendOnChannel(CommandSender sender, String channelId, String message, ChatChannelType type, ServerSyncConfig serverSyncConfig) {
        ChatChannel channel = chatChannelRepository.findChatChannel(channelId, type, serverSyncConfig).orElseThrow(() -> new BusinessException("Chat channel not found"));

        String chatChannelLine = channel.getLine()
            .replace("%message%", message)
            .replace("%sender%", sender.getName())
            .replace("%channelId%", channelId);

        sendToAllMembers(chatChannelLine, channel);
        BukkitUtils.sendEvent(new ChatChannelMessageSendEvent(sender, message, channel));
    }


    public void closeChannel(String channelId, ChatChannelType type, ServerSyncConfig serverSyncConfig) {
        ChatChannel chatChannel = chatChannelRepository.findChatChannel(channelId, type, serverSyncConfig).orElseThrow(() -> new BusinessException("Chat channel not found"));
        chatChannelRepository.delete(chatChannel.getId());

        sendToAllMembers(chatChannelClosed, chatChannel);
    }

    public void create(String channelId, String chatChannelPrefix, String chatChannelLine, String openingMessage, Set<UUID> members, ChatChannelType type) {
        ChatChannel channel = new ChatChannel(chatChannelPrefix, chatChannelLine, channelId, members, type, options.serverName);
        chatChannelRepository.save(channel);


        openingMessage = openingMessage
            .replace("%channelId%", channelId);

        sendToAllMembers(openingMessage, channel);
    }

    private void sendToAllMembers(String message, ChatChannel channel) {
        List<Player> players = channel.getMembers().stream().map(playerManager::getOnlinePlayer)
            .filter(Optional::isPresent)
            .map(p -> p.get().getPlayer())
            .collect(Collectors.toList());
        String chatChannelPrefix = channel.getPrefix()
            .replace("%channelId%", channel.getChannelId());
        messages.send(players, message, chatChannelPrefix);
    }

    public List<String> getAllChannelNames() {
        return chatChannelRepository.getAllChannelNames();
    }

    public void joinChannel(Player player, String channelId, ChatChannelType type, ServerSyncConfig serverSyncConfig) {
        ChatChannel chatChannel = chatChannelRepository.findChatChannel(channelId, type, serverSyncConfig).orElseThrow(() -> new BusinessException("Chat channel not found"));
        chatChannel.addMember(player.getUniqueId());
        chatChannelRepository.addMember(chatChannel, player);
        sendToAllMembers(chatChannelJoined.replace("%player%", player.getName()), chatChannel);
    }
}
