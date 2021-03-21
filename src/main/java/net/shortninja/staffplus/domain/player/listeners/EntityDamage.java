package net.shortninja.staffplus.domain.player.listeners;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.common.config.Options;
import net.shortninja.staffplus.domain.staff.mode.StaffModeService;
import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.session.SessionManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.UUID;

public class EntityDamage implements Listener {
    private final Options options = IocContainer.getOptions();
    private final SessionManagerImpl sessionManager = IocContainer.getSessionManager();
    private final StaffModeService staffModeService = IocContainer.getModeCoordinator();

    public EntityDamage() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (!(entity instanceof Player)) {
            return;
        }

        UUID uuid = entity.getUniqueId();
        PlayerSession session = sessionManager.get(uuid);
        if ((options.modeConfiguration.isModeInvincible() && session.isInStaffMode() || (!options.modeConfiguration.getFreezeModeConfiguration().isModeFreezeDamage() && session.isFrozen()) || session.isProtected())) {
            event.setCancelled(true);
        }
    }
}