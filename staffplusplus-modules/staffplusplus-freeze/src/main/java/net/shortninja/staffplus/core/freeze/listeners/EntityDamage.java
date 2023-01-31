package net.shortninja.staffplus.core.freeze.listeners;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.freeze.config.FreezeConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

@IocBukkitListener
public class EntityDamage implements Listener {
    private final FreezeConfiguration freezeConfiguration;
    private final OnlineSessionsManager sessionManager;

    public EntityDamage(FreezeConfiguration freezeConfiguration, OnlineSessionsManager sessionManager) {
        this.freezeConfiguration = freezeConfiguration;
        this.sessionManager = sessionManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (!(entity instanceof Player)) {
            return;
        }

        OnlinePlayerSession session = sessionManager.get((Player) entity);
        if (isFrozen(session)) {
            event.setCancelled(true);
        }
    }

    private boolean isFrozen(OnlinePlayerSession session) {
        return !freezeConfiguration.damage && session.isFrozen();
    }
}