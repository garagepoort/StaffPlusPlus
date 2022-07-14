package net.shortninja.staffplus.core.domain.chatchannels.gui;

import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.domain.chatchannels.bungee.dto.ChatChannelCreatedBungeeDto;
import net.shortninja.staffplus.core.domain.chatchannels.bungee.dto.ChatChannelMessageBungeeDto;
import net.shortninja.staffplus.core.domain.chatchannels.bungee.dto.ChatChannelPlayerJoinedBungeeDto;
import net.shortninja.staffplus.core.domain.chatchannels.bungee.dto.ChatChannelPlayerLeftBungeeDto;
import net.shortninja.staffplus.core.domain.chatchannels.bungee.events.ChatChannelClosedBungeeEvent;
import net.shortninja.staffplus.core.domain.chatchannels.bungee.events.ChatChannelCreatedBungeeEvent;
import net.shortninja.staffplus.core.domain.chatchannels.bungee.events.ChatChannelMessageReceivedBungeeEvent;
import net.shortninja.staffplus.core.domain.chatchannels.bungee.events.ChatChannelPlayerJoinedBungeeEvent;
import net.shortninja.staffplus.core.domain.chatchannels.bungee.events.ChatChannelPlayerLeftBungeeEvent;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.chatchannels.ChatChannelClosedEvent;
import net.shortninja.staffplusplus.chatchannels.ChatChannelCreatedEvent;
import net.shortninja.staffplusplus.chatchannels.ChatChannelMessageSendEvent;
import net.shortninja.staffplusplus.chatchannels.ChatChannelPlayerJoinedEvent;
import net.shortninja.staffplusplus.chatchannels.ChatChannelPlayerLeftEvent;
import net.shortninja.staffplusplus.chatchannels.IChatChannel;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@IocBukkitListener
public class ChatChannelChatNotifier implements Listener {

    @ConfigProperty("%lang%:chatchannels.closed")
    public String chatChannelClosed;
    @ConfigProperty("%lang%:chatchannels.joined")
    public String chatChannelJoined;
    @ConfigProperty("%lang%:chatchannels.left")
    public String chatChannelLeft;

    private final Messages messages;
    private final PlayerManager playerManager;

    public ChatChannelChatNotifier(Messages messages, PlayerManager playerManager) {
        this.messages = messages;
        this.playerManager = playerManager;
    }

    @EventHandler
    public void notifyMessageSendOnChannel(ChatChannelMessageSendEvent event) {
        IChatChannel channel = event.getChannel();

        String chatChannelLine = channel.getLine()
            .replace("%message%", event.getMessage())
            .replace("%sender%", event.getSender().getUsername())
            .replace("%channelId%", channel.getChannelId());

        sendToAllMembers(chatChannelLine, channel.getMembers(), channel.getPrefix(), channel.getChannelId());
    }

    @EventHandler
    public void notifyMessageSendOnChannelBungee(ChatChannelMessageReceivedBungeeEvent event) {
        ChatChannelMessageBungeeDto channel = event.getChatChannelMessageBungee();

        String chatChannelLine = channel.getLine()
            .replace("%message%", channel.getMessage())
            .replace("%sender%", channel.getSenderName())
            .replace("%channelId%", channel.getChannelId());

        sendToAllMembers(chatChannelLine, channel.getMembers(), channel.getPrefix(), channel.getChannelId());
    }

    @EventHandler
    public void notifyPlayerCreatedChannel(ChatChannelCreatedEvent event) {
        String openingMessage = event.getOpeningMessage()
            .replace("%channelId%", event.getChannel().getChannelId());

        sendToAllMembers(openingMessage, event.getChannel().getMembers(), event.getChannel().getPrefix(), event.getChannel().getChannelId());
    }

    @EventHandler
    public void notifyPlayerCreatedChannelBungee(ChatChannelCreatedBungeeEvent event) {
        ChatChannelCreatedBungeeDto channelDto = event.getChatChannelCreatedBungeeDto();

        String openingMessage = channelDto.getOpeningMessage()
            .replace("%channelId%", channelDto.getChannelId());

        sendToAllMembers(openingMessage, channelDto.getMembers(), channelDto.getPrefix(), channelDto.getChannelId());
    }

    @EventHandler
    public void notifyPlayerClosedChannel(ChatChannelClosedEvent event) {
        sendToAllMembers(chatChannelClosed, event.getChannel().getMembers(), event.getChannel().getPrefix(), event.getChannel().getChannelId());
    }

    @EventHandler
    public void notifyPlayerClosedChannelBungee(ChatChannelClosedBungeeEvent event) {
        sendToAllMembers(chatChannelClosed, event.getChannel().getMembers(), event.getChannel().getPrefix(), event.getChannel().getChannelId());
    }

    @EventHandler
    public void notifyPlayerJoinedChannel(ChatChannelPlayerJoinedEvent event) {
        sendToAllMembers(chatChannelJoined.replace("%player%", event.getPlayer().getUsername()),
            event.getChannel().getMembers(),
            event.getChannel().getPrefix(),
            event.getChannel().getChannelId());
    }

    @EventHandler
    public void notifyPlayerJoinedChannelBungee(ChatChannelPlayerJoinedBungeeEvent event) {
        ChatChannelPlayerJoinedBungeeDto chatChannelPlayerJoinedBungeeDto = event.getChatChannelPlayerJoinedBungeeDto();

        sendToAllMembers(chatChannelJoined.replace("%player%", chatChannelPlayerJoinedBungeeDto.getPlayerName()),
            chatChannelPlayerJoinedBungeeDto.getMembers(),
            chatChannelPlayerJoinedBungeeDto.getPrefix(),
            chatChannelPlayerJoinedBungeeDto.getChannelId());
    }

    @EventHandler
    public void notifyPlayerLeftChannel(ChatChannelPlayerLeftEvent event) {
        IChatChannel channel = event.getChannel();

        if (event.getPlayer().isOnline()) {
            String chatChannelPrefix = channel.getPrefix()
                .replace("%channelId%", channel.getChannelId());
            messages.send(event.getPlayer().getPlayer(), chatChannelLeft.replace("%player%", event.getPlayer().getUsername()), chatChannelPrefix);
        }
        sendToAllMembers(chatChannelLeft.replace("%player%", event.getPlayer().getUsername()), channel.getMembers(), channel.getPrefix(), channel.getChannelId());
    }

    @EventHandler
    public void notifyPlayerLeftChannelBungee(ChatChannelPlayerLeftBungeeEvent event) {
        ChatChannelPlayerLeftBungeeDto channel = event.getChatChannelPlayerLeftBungeeDto();
        sendToAllMembers(chatChannelLeft.replace("%player%", channel.getPlayerName()), channel.getMembers(), channel.getPrefix(), channel.getChannelId());
    }

    private void sendToAllMembers(String message, Set<UUID> members, String prefix, String channelId) {
        List<Player> players = members.stream().map(playerManager::getOnlinePlayer)
            .filter(Optional::isPresent)
            .map(p -> p.get().getPlayer())
            .collect(Collectors.toList());
        String chatChannelPrefix = prefix
            .replace("%channelId%", channelId);
        messages.send(players, message, chatChannelPrefix);
    }
}
