package net.shortninja.staffplus.core.domain.staff.vanish.listeners;

import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.domain.staff.vanish.VanishServiceImpl;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@IocListener(conditionalOnProperty = "vanish-module.enabled=true")
public class VanishQuitListener implements Listener {

    private final VanishServiceImpl vanishService;

    public VanishQuitListener(VanishServiceImpl vanishService) {
        this.vanishService = vanishService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void removeFromCache(PlayerQuitEvent event) {
        vanishService.clearCache(event.getPlayer());
    }
}
