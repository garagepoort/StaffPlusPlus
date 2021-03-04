package net.shortninja.staffplus.server.listener.entity;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.session.SessionManager;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

public class EntityTarget implements Listener {
    private SessionManager sessionManager = IocContainer.getSessionManager();

    public EntityTarget() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onTarget(EntityTargetEvent event) {
        if (event.getTarget() instanceof Player) {
            Player player = (Player) event.getTarget();
            if (sessionManager.get(player.getUniqueId()) == null)
                return;
            if (sessionManager.get(player.getUniqueId()).getVanishType() != VanishType.TOTAL) {
                return;
            }

            event.setCancelled(true);
        }
    }
}