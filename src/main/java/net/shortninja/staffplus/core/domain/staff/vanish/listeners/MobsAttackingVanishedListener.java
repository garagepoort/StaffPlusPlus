package net.shortninja.staffplus.core.domain.staff.vanish.listeners;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

@IocBean
@IocListener
public class MobsAttackingVanishedListener implements Listener {
    private final OnlineSessionsManager sessionManager;

    public MobsAttackingVanishedListener(OnlineSessionsManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onTarget(EntityTargetLivingEntityEvent event) {
        if (event.getTarget() instanceof Player && sessionManager.has(event.getTarget().getUniqueId())) {
            OnlinePlayerSession session = sessionManager.get((Player) event.getTarget());
            if (session.getVanishType() == VanishType.TOTAL || session.getVanishType() == VanishType.PLAYER) {
                event.setCancelled(true);
            }
        }
    }
}