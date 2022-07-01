package net.shortninja.staffplus.core.domain.staff.vanish.listeners;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

@IocBukkitListener(conditionalOnProperty = "vanish-module.item-drop=false")
public class VanishCancelDropItem implements Listener {
    private final OnlineSessionsManager sessionManager;

    public VanishCancelDropItem(OnlineSessionsManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onDrop(PlayerDropItemEvent event) {
        OnlinePlayerSession session = sessionManager.get(event.getPlayer());
        if (session.isVanished()) {
            event.setCancelled(true);
        }
    }
}