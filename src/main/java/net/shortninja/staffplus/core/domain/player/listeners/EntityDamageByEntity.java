package net.shortninja.staffplus.core.domain.player.listeners;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.application.session.SessionManagerImpl;
import net.shortninja.staffplus.core.domain.staff.tracing.TraceService;
import net.shortninja.staffplus.core.domain.staff.tracing.TraceType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Optional;
import java.util.UUID;

@IocBean
public class EntityDamageByEntity implements Listener {
    private final SessionManagerImpl sessionManager;
    private final TraceService traceService;

    public EntityDamageByEntity(SessionManagerImpl sessionManager, TraceService traceService) {
        this.sessionManager = sessionManager;
        this.traceService = traceService;
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent event) {
        Entity damaged = event.getEntity();

        Optional<Player> damager = getDamager(event.getDamager());
        if (damager.isPresent() && damager.get().isOnline()) {
            UUID playerUuid = damager.get().getUniqueId();
            PlayerSession session = sessionManager.get(playerUuid);
            if (session.isFrozen() || (session.isInStaffMode() && !session.getModeConfiguration().get().isModeDamage())) {
                event.setCancelled(true);
                return;
            }
        }

        if (damager.isPresent() || damaged instanceof Player) {
            logTrace(event.getDamager(), damaged);
        }
    }

    private void logTrace(Entity damager, Entity damaged) {
        String damagerName = damager instanceof Player ? damager.getName() : damager.getType().toString();
        String damagedName = damaged instanceof Player ? damaged.getName() : damaged.getType().toString();

        traceService.sendTraceMessage(TraceType.DAMAGE, damaged.getUniqueId(), String.format("Player received damage from [%s]", damagerName));
        traceService.sendTraceMessage(TraceType.DAMAGE, damager.getUniqueId(), String.format("Player dealt damage to [%s]", damagedName));
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