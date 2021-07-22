package net.shortninja.staffplus.core.domain.staff.vanish.listeners;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.application.session.SessionManagerImpl;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

@IocBean
@IocListener
public class MobsAttackingVanishedListener implements Listener {
    private final SessionManagerImpl sessionManager;

    public MobsAttackingVanishedListener(SessionManagerImpl sessionManager) {
        this.sessionManager = sessionManager;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onTarget(EntityTargetLivingEntityEvent event) {
        if (event.getTarget() instanceof Player && sessionManager.has(event.getTarget().getUniqueId())) {
            PlayerSession playerSession = sessionManager.get(event.getTarget().getUniqueId());
            if (playerSession.getVanishType() == VanishType.TOTAL || playerSession.getVanishType() == VanishType.PLAYER) {
                event.setCancelled(true);
            }
        }
    }
}