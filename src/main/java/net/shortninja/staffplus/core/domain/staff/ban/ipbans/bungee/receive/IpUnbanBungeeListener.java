package net.shortninja.staffplus.core.domain.staff.ban.ipbans.bungee.receive;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMessageListener;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.common.bungee.BungeeClient;
import net.shortninja.staffplus.core.domain.staff.ban.ipbans.bungee.dto.IpBanBungeeDto;
import net.shortninja.staffplus.core.domain.staff.ban.ipbans.bungee.events.IpUnbanBungeeEvent;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.Optional;

import static net.shortninja.staffplus.core.common.Constants.BUNGEE_CORD_CHANNEL;

@IocBean(conditionalOnProperty = "isNotEmpty(server-sync-module.ban-sync)")
@IocMessageListener(channel = BUNGEE_CORD_CHANNEL)
public class IpUnbanBungeeListener implements PluginMessageListener {

    private final BungeeClient bungeeClient;
    private final ServerSyncConfiguration serverSyncConfiguration;

    public IpUnbanBungeeListener(BungeeClient bungeeClient, ServerSyncConfiguration serverSyncConfiguration) {
        this.bungeeClient = bungeeClient;
        this.serverSyncConfiguration = serverSyncConfiguration;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        Optional<IpBanBungeeDto> banBungeeDto = bungeeClient.handleReceived(channel, Constants.BUNGEE_PLAYER_UNBANNED_CHANNEL, message, IpBanBungeeDto.class);

        if (banBungeeDto.isPresent() && serverSyncConfiguration.banSyncServers.matchesServer(banBungeeDto.get().getServerName())) {
            Bukkit.getPluginManager().callEvent(new IpUnbanBungeeEvent(banBungeeDto.get()));
        }
    }
}
