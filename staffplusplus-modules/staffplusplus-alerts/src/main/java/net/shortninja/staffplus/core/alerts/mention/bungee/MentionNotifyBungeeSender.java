package net.shortninja.staffplus.core.alerts.mention.bungee;

import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.configuration.ConfigTransformer;
import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.common.bungee.BungeeClient;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfig;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfigTransformer;
import net.shortninja.staffplusplus.chat.PlayerMentionedEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocBukkitListener(conditionalOnProperty = "isNotEmpty(alerts-module.mention-notify-bungee)")
public class MentionNotifyBungeeSender implements Listener {

    @ConfigProperty("alerts-module.mention-notify-bungee")
    @ConfigTransformer(ServerSyncConfigTransformer.class)
    private ServerSyncConfig syncServers;

    private final BungeeClient bungeeClient;

    public MentionNotifyBungeeSender(BungeeClient bungeeClient) {
        this.bungeeClient = bungeeClient;
    }

    @EventHandler
    public void onPlayerMentioned(PlayerMentionedEvent playerMentionedEvent) {
        if (Bukkit.getOnlinePlayers().isEmpty()) {
            return;
        }
        Player player = Bukkit.getOnlinePlayers().iterator().next();
        bungeeClient.sendMessage(player, Constants.BUNGEE_MENTION_ALERT_CHANNEL, new MentionBungeeDto(playerMentionedEvent), syncServers);
    }
}