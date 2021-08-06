package net.shortninja.staffplus.core.domain.player.ip;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.StaffPlus;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static org.bukkit.Bukkit.getScheduler;

@IocBean
@IocListener
public class UpdateIpListener implements Listener {

    private final PlayerIpService playerIpService;

    public UpdateIpListener(PlayerIpService playerIpService) {
        this.playerIpService = playerIpService;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void updateIp(PlayerJoinEvent playerJoinEvent) {
        getScheduler().runTaskAsynchronously(StaffPlus.get(), () -> playerIpService.savePlayerIp(playerJoinEvent.getPlayer()));
    }
}
