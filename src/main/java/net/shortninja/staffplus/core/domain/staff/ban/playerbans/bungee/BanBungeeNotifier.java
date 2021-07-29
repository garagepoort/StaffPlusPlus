package net.shortninja.staffplus.core.domain.staff.ban.playerbans.bungee;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.common.bungee.BungeeClient;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.bungee.dto.BanBungeeDto;
import net.shortninja.staffplusplus.ban.BanEvent;
import net.shortninja.staffplusplus.ban.UnbanEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.shortninja.staffplus.core.common.Constants.BUNGEE_PLAYER_BANNED_CHANNEL;
import static net.shortninja.staffplus.core.common.Constants.BUNGEE_PLAYER_UNBANNED_CHANNEL;

@IocBean(conditionalOnProperty = "server-sync-module.ban-sync=true")
@IocListener
public class BanBungeeNotifier implements Listener {

    private final BungeeClient bungeeClient;

    public BanBungeeNotifier(BungeeClient bungeeClient) {
        this.bungeeClient = bungeeClient;
    }

    @EventHandler
    public void onBan(BanEvent event) {
        Player player = Bukkit.getOnlinePlayers().iterator().next();
        bungeeClient.sendMessage(player, BUNGEE_PLAYER_BANNED_CHANNEL, new BanBungeeDto(event.getBan(), event.getBanMessage()));
    }

    @EventHandler
    public void onUnban(UnbanEvent event) {
        Player player = Bukkit.getOnlinePlayers().iterator().next();
        bungeeClient.sendMessage(player, BUNGEE_PLAYER_UNBANNED_CHANNEL, new BanBungeeDto(event.getBan()));
    }

}
