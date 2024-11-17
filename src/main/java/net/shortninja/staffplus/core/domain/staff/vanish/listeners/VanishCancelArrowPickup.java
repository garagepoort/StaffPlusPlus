package net.shortninja.staffplus.core.domain.staff.vanish.listeners;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupArrowEvent;

@IocBukkitListener(conditionalOnProperty = "vanish-module.item-pickup=false")
public class VanishCancelArrowPickup implements Listener {
    private final OnlineSessionsManager sessionManager;

    public VanishCancelArrowPickup(OnlineSessionsManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPickup(PlayerPickupArrowEvent event) {
        Player player = event.getPlayer();
        
        if (!sessionManager.has(player.getUniqueId())) return;
        OnlinePlayerSession session = sessionManager.get(player);
        
        if (!session.isVanished()) return;
        event.setCancelled(true);
    }
}