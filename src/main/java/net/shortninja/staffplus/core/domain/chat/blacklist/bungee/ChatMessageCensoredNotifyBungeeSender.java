package net.shortninja.staffplus.core.domain.chat.blacklist.bungee;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.configuration.ConfigTransformer;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.common.bungee.BungeeClient;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfig;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfigTransformer;
import net.shortninja.staffplusplus.chat.ChatMessageCensoredEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocBean(conditionalOnProperty = "isNotEmpty(alerts-module.blacklist-notify-bungee)")
@IocListener
public class ChatMessageCensoredNotifyBungeeSender implements Listener {

    @ConfigProperty("alerts-module.blacklist-notify-bungee")
    @ConfigTransformer(ServerSyncConfigTransformer.class)
    private ServerSyncConfig syncServers;

    private final BungeeClient bungeeClient;

    public ChatMessageCensoredNotifyBungeeSender(BungeeClient bungeeClient) {
        this.bungeeClient = bungeeClient;
    }

    @EventHandler
    public void onChatMessageCensored(ChatMessageCensoredEvent chatMessageCensoredEvent) {
        if (Bukkit.getOnlinePlayers().isEmpty()) {
            return;
        }
        Player player = Bukkit.getOnlinePlayers().iterator().next();
        bungeeClient.sendMessage(player, Constants.BUNGEE_BLACKLIST_ALERT_CHANNEL, new ChatMessageCensoredBungeeDto(chatMessageCensoredEvent), syncServers);
    }
}