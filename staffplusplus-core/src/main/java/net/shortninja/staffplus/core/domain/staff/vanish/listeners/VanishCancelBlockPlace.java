package net.shortninja.staffplus.core.domain.staff.vanish.listeners;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

@IocBukkitListener(conditionalOnProperty = "vanish-module.block-place=false")
public class VanishCancelBlockPlace implements Listener {
    private final OnlineSessionsManager sessionManager;

    public VanishCancelBlockPlace(OnlineSessionsManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        OnlinePlayerSession session = sessionManager.get(event.getPlayer());

        if (session.isVanished()) {
            event.setCancelled(true);
        }
    }
}