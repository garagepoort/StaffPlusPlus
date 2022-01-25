package net.shortninja.staffplus.core.domain.chatchannels.bungee;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.common.bungee.BungeeClient;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfig;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfiguration;
import net.shortninja.staffplusplus.chatchannels.ChatChannelMessageSendEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.shortninja.staffplus.core.common.Constants.BUNGEE_CHATCHANNELS_MESSAGE_SEND_CHANNEL;

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
        bungeeClient.sendMessage(player, BUNGEE_CHATCHANNELS_MESSAGE_SEND_CHANNEL, new ChatChannelMessageBungee(chatChannelMessageSendEvent.getMessage(), chatChannelMessageSendEvent), serverSyncConfig);
    }
}
