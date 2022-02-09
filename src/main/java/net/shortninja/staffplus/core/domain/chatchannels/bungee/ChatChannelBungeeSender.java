package net.shortninja.staffplus.core.domain.chatchannels.bungee;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.common.bungee.BungeeClient;
import net.shortninja.staffplus.core.domain.chatchannels.bungee.dto.ChatChannelBungeeDto;
import net.shortninja.staffplus.core.domain.chatchannels.bungee.dto.ChatChannelCreatedBungeeDto;
import net.shortninja.staffplus.core.domain.chatchannels.bungee.dto.ChatChannelMessageBungeeDto;
import net.shortninja.staffplus.core.domain.chatchannels.bungee.dto.ChatChannelPlayerJoinedBungeeDto;
import net.shortninja.staffplus.core.domain.chatchannels.bungee.dto.ChatChannelPlayerLeftBungeeDto;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfig;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfiguration;
import net.shortninja.staffplusplus.chatchannels.ChatChannelClosedEvent;
import net.shortninja.staffplusplus.chatchannels.ChatChannelCreatedEvent;
import net.shortninja.staffplusplus.chatchannels.ChatChannelMessageSendEvent;
import net.shortninja.staffplusplus.chatchannels.ChatChannelPlayerJoinedEvent;
import net.shortninja.staffplusplus.chatchannels.ChatChannelPlayerLeftEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.shortninja.staffplus.core.common.Constants.BUNGEE_CHATCHANNELS_CLOSED_CHANNEL;
import static net.shortninja.staffplus.core.common.Constants.BUNGEE_CHATCHANNELS_CREATED_CHANNEL;
import static net.shortninja.staffplus.core.common.Constants.BUNGEE_CHATCHANNELS_MESSAGE_SEND_CHANNEL;
import static net.shortninja.staffplus.core.common.Constants.BUNGEE_CHATCHANNELS_PLAYER_JOINED_CHANNEL;
import static net.shortninja.staffplus.core.common.Constants.BUNGEE_CHATCHANNELS_PLAYER_LEFT_CHANNEL;

@IocBean
@IocListener
public class ChatChannelBungeeSender implements Listener {

    private final BungeeClient bungeeClient;
    private final ServerSyncConfiguration serverSyncConfiguration;

    public ChatChannelBungeeSender(BungeeClient bungeeClient, ServerSyncConfiguration serverSyncConfiguration) {
        this.bungeeClient = bungeeClient;
        this.serverSyncConfiguration = serverSyncConfiguration;
    }

    @EventHandler
    public void onChat(ChatChannelMessageSendEvent chatChannelMessageSendEvent) {
        if (Bukkit.getOnlinePlayers().isEmpty()) {
            return;
        }
        Player player = Bukkit.getOnlinePlayers().iterator().next();
        ServerSyncConfig serverSyncConfig = serverSyncConfiguration.getForChatChannelType(chatChannelMessageSendEvent.getChannel().getType());
        bungeeClient.sendMessage(player, BUNGEE_CHATCHANNELS_MESSAGE_SEND_CHANNEL, new ChatChannelMessageBungeeDto(chatChannelMessageSendEvent.getMessage(), chatChannelMessageSendEvent), serverSyncConfig);
    }

    @EventHandler
    public void onChannelCreated(ChatChannelCreatedEvent chatChannelCreatedEvent) {
        if (Bukkit.getOnlinePlayers().isEmpty()) {
            return;
        }
        Player player = Bukkit.getOnlinePlayers().iterator().next();
        ServerSyncConfig serverSyncConfig = serverSyncConfiguration.getForChatChannelType(chatChannelCreatedEvent.getChannel().getType());
        bungeeClient.sendMessage(player, BUNGEE_CHATCHANNELS_CREATED_CHANNEL, new ChatChannelCreatedBungeeDto(chatChannelCreatedEvent.getOpeningMessage(), chatChannelCreatedEvent), serverSyncConfig);
    }

    @EventHandler
    public void onChannelClosed(ChatChannelClosedEvent chatChannelClosedEvent) {
        if (Bukkit.getOnlinePlayers().isEmpty()) {
            return;
        }
        Player player = Bukkit.getOnlinePlayers().iterator().next();
        ServerSyncConfig serverSyncConfig = serverSyncConfiguration.getForChatChannelType(chatChannelClosedEvent.getChannel().getType());
        bungeeClient.sendMessage(player, BUNGEE_CHATCHANNELS_CLOSED_CHANNEL, new ChatChannelBungeeDto(chatChannelClosedEvent.getChannel()), serverSyncConfig);
    }

    @EventHandler
    public void onPlayerJoined(ChatChannelPlayerJoinedEvent chatChannelPlayerJoinedEvent) {
        if (Bukkit.getOnlinePlayers().isEmpty()) {
            return;
        }
        Player player = Bukkit.getOnlinePlayers().iterator().next();
        ServerSyncConfig serverSyncConfig = serverSyncConfiguration.getForChatChannelType(chatChannelPlayerJoinedEvent.getChannel().getType());
        bungeeClient.sendMessage(player, BUNGEE_CHATCHANNELS_PLAYER_JOINED_CHANNEL, new ChatChannelPlayerJoinedBungeeDto(chatChannelPlayerJoinedEvent), serverSyncConfig);
    }

    @EventHandler
    public void onPlayerLeftChannel(ChatChannelPlayerLeftEvent chatChannelPlayerLeftEvent) {
        if (Bukkit.getOnlinePlayers().isEmpty()) {
            return;
        }
        Player player = Bukkit.getOnlinePlayers().iterator().next();
        ServerSyncConfig serverSyncConfig = serverSyncConfiguration.getForChatChannelType(chatChannelPlayerLeftEvent.getChannel().getType());
        bungeeClient.sendMessage(player, BUNGEE_CHATCHANNELS_PLAYER_LEFT_CHANNEL, new ChatChannelPlayerLeftBungeeDto(chatChannelPlayerLeftEvent), serverSyncConfig);
    }
}
