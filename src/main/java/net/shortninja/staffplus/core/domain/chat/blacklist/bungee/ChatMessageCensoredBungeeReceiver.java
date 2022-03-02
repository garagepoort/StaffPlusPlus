package net.shortninja.staffplus.core.domain.chat.blacklist.bungee;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMessageListener;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.configuration.ConfigTransformer;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.common.bungee.BungeeClient;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfig;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfigTransformer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.Optional;

import static net.shortninja.staffplus.core.common.Constants.BUNGEE_CORD_CHANNEL;

@IocBean(conditionalOnProperty = "isNotEmpty(alerts-module.blacklist-notify-bungee)")
@IocMessageListener(channel = BUNGEE_CORD_CHANNEL)
public class ChatMessageCensoredBungeeReceiver implements PluginMessageListener {

    @ConfigProperty("alerts-module.blacklist-notify-bungee")
    @ConfigTransformer(ServerSyncConfigTransformer.class)
    private ServerSyncConfig syncServers;

    private final BungeeClient bungeeClient;

    public ChatMessageCensoredBungeeReceiver(BungeeClient bungeeClient) {
        this.bungeeClient = bungeeClient;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        Optional<ChatMessageCensoredBungeeDto> chatMessageCensoredBungeeDto = bungeeClient.handleReceived(channel, Constants.BUNGEE_BLACKLIST_ALERT_CHANNEL, message, ChatMessageCensoredBungeeDto.class);

        if (chatMessageCensoredBungeeDto.isPresent() && syncServers.matchesServer(chatMessageCensoredBungeeDto.get().getServerName())) {
            Bukkit.getPluginManager().callEvent(new ChatMessageCensoredBungeeEvent(chatMessageCensoredBungeeDto.get()));
        }
    }
}