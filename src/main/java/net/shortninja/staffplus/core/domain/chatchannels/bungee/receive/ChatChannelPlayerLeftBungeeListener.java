package net.shortninja.staffplus.core.domain.chatchannels.bungee.receive;

import be.garagepoort.mcioc.IocMessageListener;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.common.bungee.BungeeClient;
import net.shortninja.staffplus.core.domain.chatchannels.bungee.dto.ChatChannelPlayerLeftBungeeDto;
import net.shortninja.staffplus.core.domain.chatchannels.bungee.events.ChatChannelPlayerLeftBungeeEvent;
import net.shortninja.staffplus.core.domain.chatchannels.config.ChatChannelConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.Optional;

@IocMessageListener(channel = Constants.BUNGEE_CORD_CHANNEL)
public class ChatChannelPlayerLeftBungeeListener implements PluginMessageListener {

    private final BungeeClient bungeeClient;
    private final ChatChannelConfiguration chatChannelConfiguration;

    public ChatChannelPlayerLeftBungeeListener(BungeeClient bungeeClient, ChatChannelConfiguration chatChannelConfiguration) {
        this.bungeeClient = bungeeClient;
        this.chatChannelConfiguration = chatChannelConfiguration;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        Optional<ChatChannelPlayerLeftBungeeDto> bungeeMessage = bungeeClient.handleReceived(channel, Constants.BUNGEE_CHATCHANNELS_PLAYER_LEFT_CHANNEL, message, ChatChannelPlayerLeftBungeeDto.class);
        bungeeMessage
            .filter(b -> chatChannelConfiguration.enabledChannels.contains(bungeeMessage.get().getType()))
            .ifPresent(b -> Bukkit.getPluginManager().callEvent(new ChatChannelPlayerLeftBungeeEvent(b)));
    }
}
