package net.shortninja.staffplus.core.domain.staff.ban.playerbans.bungee.receive;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMessageListener;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.common.bungee.BungeeClient;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.bungee.dto.BanBungeeDto;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.bungee.events.BanBungeeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.Optional;

import static net.shortninja.staffplus.core.common.Constants.BUNGEE_CORD_CHANNEL;

@IocBean(conditionalOnProperty = "server-sync-module.ban-sync=true")
@IocMessageListener(channel = BUNGEE_CORD_CHANNEL)
public class BanBungeeListener implements PluginMessageListener {

    private final BungeeClient bungeeClient;
    private final Options options;

    public BanBungeeListener(BungeeClient bungeeClient, Options options) {
        this.bungeeClient = bungeeClient;
        this.options = options;
    }


    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if(options.serverSyncConfiguration.banSyncEnabled) {
            Optional<BanBungeeDto> banBungeeDto = bungeeClient.handleReceived(channel, Constants.BUNGEE_PLAYER_BANNED_CHANNEL, message, BanBungeeDto.class);
            banBungeeDto.ifPresent(b -> Bukkit.getPluginManager().callEvent(new BanBungeeEvent(b)));
        }
    }
}
