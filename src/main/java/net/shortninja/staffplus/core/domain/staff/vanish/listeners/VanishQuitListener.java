package net.shortninja.staffplus.core.domain.staff.vanish.listeners;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.domain.staff.vanish.VanishService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@IocBukkitListener(conditionalOnProperty = "vanish-module.enabled=true")
public class VanishQuitListener implements Listener {

    private final VanishService vanishService;

    public VanishQuitListener(VanishService vanishService) {
        this.vanishService = vanishService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void removeFromCache(PlayerQuitEvent event) {
        vanishService.clearCache(event.getPlayer());
    }
}
