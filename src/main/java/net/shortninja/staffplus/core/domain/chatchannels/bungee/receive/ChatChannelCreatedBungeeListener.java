package net.shortninja.staffplus.core.domain.chatchannels.bungee.receive;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitMessageListener;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.common.bungee.BungeeClient;
import net.shortninja.staffplus.core.domain.chatchannels.bungee.dto.ChatChannelCreatedBungeeDto;
import net.shortninja.staffplus.core.domain.chatchannels.bungee.events.ChatChannelCreatedBungeeEvent;
import net.shortninja.staffplus.core.domain.chatchannels.config.ChatChannelConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.Optional;

@IocBukkitMessageListener(channel = Constants.BUNGEE_CORD_CHANNEL)
public class ChatChannelCreatedBungeeListener implements PluginMessageListener {

    private final BungeeClient bungeeClient;
    private final ChatChannelConfiguration chatChannelConfiguration;

    public ChatChannelCreatedBungeeListener(BungeeClient bungeeClient, ChatChannelConfiguration chatChannelConfiguration) {
        this.bungeeClient = bungeeClient;
        this.chatChannelConfiguration = chatChannelConfiguration;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        Optional<ChatChannelCreatedBungeeDto> bungeeMessage = bungeeClient.handleReceived(channel, Constants.BUNGEE_CHATCHANNELS_CREATED_CHANNEL, message, ChatChannelCreatedBungeeDto.class);
        bungeeMessage
            .filter(b -> chatChannelConfiguration.enabledChannels.contains(bungeeMessage.get().getType()))
            .ifPresent(b -> Bukkit.getPluginManager().callEvent(new ChatChannelCreatedBungeeEvent(b)));
    }
}
