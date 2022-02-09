package net.shortninja.staffplus.core.domain.chatchannels.bungee.receive;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMessageListener;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.common.bungee.BungeeClient;
import net.shortninja.staffplus.core.domain.chatchannels.bungee.dto.ChatChannelPlayerJoinedBungeeDto;
import net.shortninja.staffplus.core.domain.chatchannels.bungee.events.ChatChannelPlayerJoinedBungeeEvent;
import net.shortninja.staffplus.core.domain.chatchannels.config.ChatChannelConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.Optional;

@IocBean
@IocMessageListener(channel = Constants.BUNGEE_CORD_CHANNEL)
public class ChatChannelPlayerJoinedBungeeListener implements PluginMessageListener {

    private final BungeeClient bungeeClient;
    private final ChatChannelConfiguration chatChannelConfiguration;

    public ChatChannelPlayerJoinedBungeeListener(BungeeClient bungeeClient, ChatChannelConfiguration chatChannelConfiguration) {
        this.bungeeClient = bungeeClient;
        this.chatChannelConfiguration = chatChannelConfiguration;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        Optional<ChatChannelPlayerJoinedBungeeDto> bungeeMessage = bungeeClient.handleReceived(channel, Constants.BUNGEE_CHATCHANNELS_PLAYER_JOINED_CHANNEL, message, ChatChannelPlayerJoinedBungeeDto.class);
        bungeeMessage
            .filter(b -> chatChannelConfiguration.enabledChannels.contains(bungeeMessage.get().getType()))
            .ifPresent(b -> Bukkit.getPluginManager().callEvent(new ChatChannelPlayerJoinedBungeeEvent(b)));
    }
}
