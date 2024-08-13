package net.shortninja.staffplus.core.domain.staff.vanish.listeners;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

@IocBukkitListener(conditionalOnProperty = "vanish-module.enabled=true")
public class ExperienceTargetListener implements Listener {
    private final OnlineSessionsManager sessionManager;

    public ExperienceTargetListener(OnlineSessionsManager sessionManager) {
        this.sessionManager = sessionManager;
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onExperienceOrbTarget(EntityTargetLivingEntityEvent event) {
        if (event.getTarget() == null || !(event.getTarget() instanceof Player) || !sessionManager.has(event.getTarget().getUniqueId()) || !(event.getEntity() instanceof ExperienceOrb)) return;
        
        OnlinePlayerSession session = sessionManager.get((Player) event.getTarget());
        if (session.getVanishType() == VanishType.TOTAL || session.getVanishType() == VanishType.PLAYER) {
            event.setCancelled(true);
            event.setTarget(null);
            
            // The player can still pickup the orb, the only way to prevent it is to patch nms
            // As a workaround, we just kill the orb
            event.getEntity().remove();
        }
    }
}