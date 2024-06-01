package net.shortninja.staffplus.core.domain.staff.vanish.listeners;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.GenericGameEvent;

@IocBukkitListener(conditionalOnProperty = "vanish-module.enabled=true")
public class VanishSculkSensorListener implements Listener {
    private final OnlineSessionsManager sessionManager;

    public VanishSculkSensorListener(OnlineSessionsManager sessionManager) {
        this.sessionManager = sessionManager;
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onSoundWaveCreation(GenericGameEvent event) {
        if (event.getEntity() == null || !(event.getEntity() instanceof Player) || !sessionManager.has(event.getEntity().getUniqueId())) return;
        
        OnlinePlayerSession session = sessionManager.get((Player) event.getEntity());
        if (session.getVanishType() == VanishType.TOTAL || session.getVanishType() == VanishType.PLAYER) {
            event.setCancelled(true);
        }
    }
}