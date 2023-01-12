package net.shortninja.staffplus.core.alerts.listeners;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.alerts.xray.XrayService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@IocBukkitListener
public class PlayerQuit implements Listener {

    private final XrayService xrayService;

    public PlayerQuit(XrayService xrayService) {
        this.xrayService = xrayService;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        xrayService.clearTrace(player);
    }
}