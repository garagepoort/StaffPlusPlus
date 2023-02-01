package net.shortninja.staffplus.core.vanish.listeners;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Optional;

@IocBukkitListener(conditionalOnProperty = "vanish-module.damage=false")
public class VanishCancelGiveDamage implements Listener {
    private final OnlineSessionsManager sessionManager;

    public VanishCancelGiveDamage(OnlineSessionsManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent event) {
        Optional<Player> damager = getDamager(event.getDamager());
        if (damager.isPresent() && damager.get().isOnline()) {
            OnlinePlayerSession session = sessionManager.get(damager.get());
            if (session.isVanished()) {
                event.setCancelled(true);
            }
        }
    }

    public Optional<Player> getDamager(Entity damager) {
        if (damager instanceof Player) {
            return Optional.of((Player) damager);
        }
        if (damager instanceof Arrow) {
            ProjectileSource shooter = ((Arrow) damager).getShooter();
            if (shooter instanceof Player) {
                return Optional.of((Player) shooter);
            }
        }
        return Optional.empty();
    }
}