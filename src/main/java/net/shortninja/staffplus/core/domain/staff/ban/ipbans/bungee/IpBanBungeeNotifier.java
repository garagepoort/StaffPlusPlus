package net.shortninja.staffplus.core.domain.staff.ban.ipbans.bungee;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.common.bungee.BungeeClient;
import net.shortninja.staffplus.core.domain.staff.ban.ipbans.bungee.dto.IpBanBungeeDto;
import net.shortninja.staffplusplus.ban.IpBanEvent;
import net.shortninja.staffplusplus.ban.IpUnbanEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.shortninja.staffplus.core.common.Constants.BUNGEE_IP_BANNED_CHANNEL;
import static net.shortninja.staffplus.core.common.Constants.BUNGEE_IP_UNBANNED_CHANNEL;

@IocBean
@IocListener
public class IpBanBungeeNotifier implements Listener {

    private final BungeeClient bungeeClient;

    public IpBanBungeeNotifier(BungeeClient bungeeClient) {
        this.bungeeClient = bungeeClient;
    }

    @EventHandler
    public void onBan(IpBanEvent event) {
        Player player = Bukkit.getOnlinePlayers().iterator().next();
        bungeeClient.sendMessage(player, BUNGEE_IP_BANNED_CHANNEL, new IpBanBungeeDto(event.getBan(), event.getKickTemplate().orElse(null)));
    }

    @EventHandler
    public void onUnban(IpUnbanEvent event) {
        Player player = Bukkit.getOnlinePlayers().iterator().next();
        bungeeClient.sendMessage(player, BUNGEE_IP_UNBANNED_CHANNEL, new IpBanBungeeDto(event.getBan()));
    }

}
