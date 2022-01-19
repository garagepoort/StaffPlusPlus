package net.shortninja.staffplus.core.domain.chatchannels;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfig;
import net.shortninja.staffplusplus.chatchannels.ChatChannelClosedEvent;
import net.shortninja.staffplusplus.chatchannels.ChatChannelCreatedEvent;
import net.shortninja.staffplusplus.chatchannels.ChatChannelMessageSendEvent;
import net.shortninja.staffplusplus.chatchannels.ChatChannelPlayerJoinedEvent;
import net.shortninja.staffplusplus.chatchannels.ChatChannelPlayerLeftEvent;
import net.shortninja.staffplusplus.chatchannels.ChatChannelType;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;

@IocBean
public class ChatChannelService {

    private final ChatChannelRepository chatChannelRepository;
    private final Options options;
    private List<ChatChannel> CHAT_CHANNELS_CACHE;

    public ChatChannelService(ChatChannelRepository chatChannelRepository, Options options, BukkitUtils bukkitUtils) {
        this.chatChannelRepository = chatChannelRepository;
        this.options = options;
        bukkitUtils.runTaskAsync(() -> CHAT_CHANNELS_CACHE = chatChannelRepository.findAll());
    }

    public void sendOnChannel(CommandSender sender, String channelId, String message, ChatChannelType type, ServerSyncConfig serverSyncConfig) {
        ChatChannel channel = chatChannelRepository.findChatChannel(channelId, type, serverSyncConfig).orElseThrow(() -> new BusinessException("Chat channel not found"));
        sendEvent(new ChatChannelMessageSendEvent(sender, message, channel));
    }

    public void closeChannel(String channelId, ChatChannelType type, ServerSyncConfig serverSyncConfig) {
        ChatChannel chatChannel = chatChannelRepository.findChatChannel(channelId, type, serverSyncConfig).orElseThrow(() -> new BusinessException("Chat channel not found"));
        chatChannelRepository.delete(chatChannel.getId());
        CHAT_CHANNELS_CACHE.removeIf(c -> c.getId() == chatChannel.getId());
        sendEvent(new ChatChannelClosedEvent(chatChannel));
    }

    public void create(String channelId, String chatChannelPrefix, String chatChannelLine, String openingMessage, Set<UUID> members, ChatChannelType type) {
        Optional<ChatChannel> existingChannel = chatChannelRepository.findChatChannel(channelId, type);
        if(existingChannel.isPresent()) {
            throw new BusinessException("Cannot open chat channel. This channel has already been opened");
        }

        ChatChannel channel = new ChatChannel(chatChannelPrefix, chatChannelLine, channelId, members, type, options.serverName);
        chatChannelRepository.save(channel);
        updateCache(channel);
        sendEvent(new ChatChannelCreatedEvent(channel, openingMessage));
    }

    public List<String> getAllChannelNames() {
        return CHAT_CHANNELS_CACHE.stream()
            .map(ChatChannel::getName)
            .collect(Collectors.toList());
    }

    public void joinChannel(SppPlayer player, String channelId, ChatChannelType type, ServerSyncConfig serverSyncConfig) {
        ChatChannel chatChannel = chatChannelRepository.findChatChannel(channelId, type, serverSyncConfig).orElseThrow(() -> new BusinessException("&CChat channel not found"));
        if (chatChannel.hasMember(player)) {
            throw new BusinessException("&CYou are already a member of this channel");
        }
        chatChannel.addMember(player.getId());
        chatChannelRepository.addMember(chatChannel, player);
        updateCache(chatChannel);
        sendEvent(new ChatChannelPlayerJoinedEvent(player, chatChannel));
    }

    public void leaveChannel(SppPlayer player, String channelId, ChatChannelType type, ServerSyncConfig serverSyncConfig) {
        ChatChannel chatChannel = chatChannelRepository.findChatChannel(channelId, type, serverSyncConfig).orElseThrow(() -> new BusinessException("&CChat channel not found"));
        if (!chatChannel.hasMember(player)) {
            throw new BusinessException("&CYou are not a member of this channel");
        }
        chatChannel.removeMember(player.getId());
        chatChannelRepository.removeMember(chatChannel, player);
        updateCache(chatChannel);
        sendEvent(new ChatChannelPlayerLeftEvent(player, chatChannel));
    }

    public List<String> getMyChannelIds(Player player, ChatChannelType type) {
        return CHAT_CHANNELS_CACHE.stream()
            .filter(c -> c.getType() == type)
            .filter(c -> c.getMembers().contains(player.getUniqueId()))
            .map(ChatChannel::getChannelId)
            .collect(Collectors.toList());
    }

    public List<String> getAllChannelIds(ChatChannelType type) {
        return CHAT_CHANNELS_CACHE.stream()
            .filter(c -> c.getType() == type)
            .map(ChatChannel::getChannelId)
            .collect(Collectors.toList());
    }

    private void updateCache(ChatChannel chatChannel) {
        CHAT_CHANNELS_CACHE.removeIf(c -> c.getId() == chatChannel.getId());
        CHAT_CHANNELS_CACHE.add(chatChannel);
    }

    public Optional<ChatChannel> findChannel(String channelId, ChatChannelType type) {
        return chatChannelRepository.findChatChannel(channelId, type);
    }
}
