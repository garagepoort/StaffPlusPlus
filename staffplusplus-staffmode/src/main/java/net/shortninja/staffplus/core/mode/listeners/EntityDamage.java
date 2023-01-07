package net.shortninja.staffplus.core.mode.listeners;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.mode.config.GeneralModeConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Optional;

@IocBukkitListener
public class EntityDamage implements Listener {
    private final OnlineSessionsManager sessionManager;

    public EntityDamage(OnlineSessionsManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player)) {
            return;
        }

        OnlinePlayerSession session = sessionManager.get((Player) entity);
        if ((inStaffMode(session))) {
            event.setCancelled(true);
        }
    }

    private boolean inStaffMode(OnlinePlayerSession session) {
        Optional<GeneralModeConfiguration> modeConfig = session.get("modeConfig");
        return session.isInStaffMode() && modeConfig.get().isModeInvincible();
    }
}