package net.shortninja.staffplus.server.listener.entity;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.session.SessionManager;
import net.shortninja.staffplus.staff.mode.ModeCoordinator;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.tracing.TraceService;
import net.shortninja.staffplus.staff.tracing.TraceType;
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

public class EntityDamageByEntity implements Listener {
    private final Options options = IocContainer.getOptions();
    private final SessionManager sessionManager = IocContainer.getSessionManager();
    private final ModeCoordinator modeCoordinator = StaffPlus.get().modeCoordinator;
    private final TraceService traceService = IocContainer.getTraceService();

    public EntityDamageByEntity() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent event) {
        Entity damaged = event.getEntity();

        Optional<Player> damager = getDamager(event.getEntity());

        if (damager.isPresent()) {
            UUID playerUuid = damager.get().getUniqueId();
            if (sessionManager.get(playerUuid).isFrozen() || (!options.modeDamage && modeCoordinator.isInMode(playerUuid))) {
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