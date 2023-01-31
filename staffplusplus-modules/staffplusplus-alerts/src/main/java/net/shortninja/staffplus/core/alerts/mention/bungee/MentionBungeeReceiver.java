package net.shortninja.staffplus.core.alerts.mention.bungee;

import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.configuration.ConfigTransformer;
import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitMessageListener;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.common.bungee.BungeeClient;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfig;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfigTransformer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.Optional;

import static net.shortninja.staffplus.core.common.Constants.BUNGEE_CORD_CHANNEL;

@IocBukkitMessageListener(
    channel = BUNGEE_CORD_CHANNEL,
    conditionalOnProperty = "isNotEmpty(alerts-module.mention-notify-bungee)")
public class MentionBungeeReceiver implements PluginMessageListener {

    @ConfigProperty("alerts-module.mention-notify-bungee")
    @ConfigTransformer(ServerSyncConfigTransformer.class)
    private ServerSyncConfig syncServers;

    private final BungeeClient bungeeClient;

    public MentionBungeeReceiver(BungeeClient bungeeClient) {
        this.bungeeClient = bungeeClient;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        Optional<MentionBungeeDto> mentionBungeeDto = bungeeClient.handleReceived(channel, Constants.BUNGEE_MENTION_ALERT_CHANNEL, message, MentionBungeeDto.class);

        if (mentionBungeeDto.isPresent() && syncServers.matchesServer(mentionBungeeDto.get().getServerName())) {
            Bukkit.getPluginManager().callEvent(new PlayerMentionedBungeeEvent(mentionBungeeDto.get()));
        }
    }
}