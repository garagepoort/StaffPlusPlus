package net.shortninja.staffplus.core.domain.chatchannels.bungee;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMessageListener;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.common.bungee.BungeeClient;
import net.shortninja.staffplus.core.domain.chatchannels.config.ChatChannelConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.Optional;

@IocBean
@IocMessageListener(channel = Constants.BUNGEE_CORD_CHANNEL)
public class ChatChannelBungeeReceiver implements PluginMessageListener {

    private final BungeeClient bungeeClient;
    private final ChatChannelConfiguration chatChannelConfiguration;

    public ChatChannelBungeeReceiver(BungeeClient bungeeClient, ChatChannelConfiguration chatChannelConfiguration) {
        this.bungeeClient = bungeeClient;
        this.chatChannelConfiguration = chatChannelConfiguration;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        Optional<ChatChannelMessageBungee> bungeeMessage = bungeeClient.handleReceived(channel, Constants.BUNGEE_CHATCHANNELS_MESSAGE_SEND_CHANNEL, message, ChatChannelMessageBungee.class);
        bungeeMessage
            .filter(b -> chatChannelConfiguration.enabledChannels.contains(bungeeMessage.get().getType()))
            .ifPresent(b -> Bukkit.getPluginManager().callEvent(new ChatChannelMessageReceivedBungeeEvent(b)));
    }
}
