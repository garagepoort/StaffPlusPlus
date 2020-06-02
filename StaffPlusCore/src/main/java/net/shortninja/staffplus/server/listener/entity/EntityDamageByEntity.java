package net.shortninja.staffplus.server.listener.entity;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.player.attribute.mode.ModeCoordinator;
import net.shortninja.staffplus.server.data.config.Options;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntity implements Listener {
    private Options options = StaffPlus.get().options;
    private UserManager userManager = StaffPlus.get().userManager;
    private ModeCoordinator modeCoordinator = StaffPlus.get().modeCoordinator;

    public EntityDamageByEntity() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent event) {
        Player player = null;

        if (!(event.getDamager() instanceof Player)) {
            if (event.getDamager() instanceof Arrow) {
                Arrow arrow = (Arrow) event.getDamager();

                if (arrow.getShooter() instanceof Player) {
                    player = (Player) arrow.getShooter();
                }
            } else return;
        } else player = (Player) event.getDamager();
<<<<<<< HEAD
        if(userManager==null)
            return;

        if (player != null && (userManager.get(player.getUniqueId()).isFrozen() || (!options.modeDamage && modeCoordinator.isInMode(player.getUniqueId())))) {
=======
        if (userManager == null)
            userManager = StaffPlus.get().userManager;
        if (options == null)
            options = StaffPlus.get().options;
        if (modeCoordinator == null)
            modeCoordinator = StaffPlus.get().modeCoordinator;
        if (userManager == null)
            return;
        /*NPE*/if (userManager.get(player.getUniqueId()).isFrozen() || (!options.modeDamage && modeCoordinator.isInMode(player.getUniqueId()))) {
>>>>>>> b2eb803718fc6d2d09f3ef627210b17920278857
            event.setCancelled(true);
        }
    }
}