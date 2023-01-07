package net.shortninja.staffplus.core.domain.player.listeners;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

@IocBean(conditionalOnProperty = "freeze-module.enabled=true")
public class FreezeBlockPlace implements Listener {
    private final OnlineSessionsManager sessionManager;

    public FreezeBlockPlace(OnlineSessionsManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        OnlinePlayerSession session = sessionManager.get(event.getPlayer());

        if (session.isFrozen()) {
            event.setCancelled(true);
        }
    }
}