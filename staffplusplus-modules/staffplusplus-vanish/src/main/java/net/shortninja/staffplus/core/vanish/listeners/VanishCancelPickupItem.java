package net.shortninja.staffplus.core.vanish.listeners;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

@IocBukkitListener(conditionalOnProperty = "vanish-module.item-pickup=false")
public class VanishCancelPickupItem implements Listener {
    private final OnlineSessionsManager sessionManager;

    public VanishCancelPickupItem(OnlineSessionsManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPickup(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            OnlinePlayerSession session = sessionManager.get(player);
            if (session.isVanished()) {
                event.setCancelled(true);
            }
        }
    }
}