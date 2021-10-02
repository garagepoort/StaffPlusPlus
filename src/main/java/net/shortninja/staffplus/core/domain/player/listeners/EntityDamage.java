package net.shortninja.staffplus.core.domain.player.listeners;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.domain.staff.freeze.config.FreezeConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

@IocBean
public class EntityDamage implements Listener {
    private final FreezeConfiguration freezeConfiguration;
    private final OnlineSessionsManager sessionManager;

    public EntityDamage(FreezeConfiguration freezeConfiguration, OnlineSessionsManager sessionManager) {
        this.freezeConfiguration = freezeConfiguration;
        this.sessionManager = sessionManager;
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (!(entity instanceof Player)) {
            return;
        }

        if(!sessionManager.has(entity.getUniqueId())) {
            event.setCancelled(true);
        }

        OnlinePlayerSession session = sessionManager.get((Player) entity);
        if ((inStaffMode(session) || isFrozen(session) || session.isProtected())) {
            event.setCancelled(true);
        }
    }

    private boolean inStaffMode(OnlinePlayerSession session) {
        return session.isInStaffMode() && session.getModeConfig().get().isModeInvincible();
    }

    private boolean isFrozen(OnlinePlayerSession session) {
        return !freezeConfiguration.damage && session.isFrozen();
    }
}