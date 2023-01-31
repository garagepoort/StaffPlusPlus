package net.shortninja.staffplus.core.protection.listeners;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

@IocBukkitListener
public class EntityDamage implements Listener {
    private final OnlineSessionsManager sessionManager;

    public EntityDamage(OnlineSessionsManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (!(entity instanceof Player)) {
            return;
        }

        if(!sessionManager.has(entity.getUniqueId())) {
            event.setCancelled(true);
        }

        OnlinePlayerSession session = sessionManager.get((Player) entity);
        if (session.isProtected()) {
            event.setCancelled(true);
        }
    }
}