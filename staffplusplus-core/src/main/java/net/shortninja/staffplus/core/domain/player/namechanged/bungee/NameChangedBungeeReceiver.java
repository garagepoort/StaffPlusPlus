package net.shortninja.staffplus.core.domain.player.namechanged.bungee;

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
    conditionalOnProperty = "isNotEmpty(alerts-module.name-notify-bungee)")
public class NameChangedBungeeReceiver implements PluginMessageListener {

    @ConfigProperty("alerts-module.name-notify-bungee")
    @ConfigTransformer(ServerSyncConfigTransformer.class)
    private ServerSyncConfig syncServers;

    private final BungeeClient bungeeClient;

    public NameChangedBungeeReceiver(BungeeClient bungeeClient) {
        this.bungeeClient = bungeeClient;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        Optional<NameChangeBungeeDto> nameChangeBungeeDto = bungeeClient.handleReceived(channel, Constants.BUNGEE_NAME_CHANGED_ALERT_CHANNEL, message, NameChangeBungeeDto.class);

        if (nameChangeBungeeDto.isPresent() && syncServers.matchesServer(nameChangeBungeeDto.get().getServerName())) {
            Bukkit.getPluginManager().callEvent(new NameChangedBungeeEvent(nameChangeBungeeDto.get()));
        }
    }
}