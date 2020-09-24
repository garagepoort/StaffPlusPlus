package net.shortninja.staffplus.server.listener.entity;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.session.SessionManager;
import net.shortninja.staffplus.player.attribute.mode.ModeCoordinator;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.unordered.IPlayerSession;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.UUID;

public class EntityDamage implements Listener {
    private Options options = IocContainer.getOptions();
    private SessionManager sessionManager = StaffPlus.get().getUserManager();
    private ModeCoordinator modeCoordinator = StaffPlus.get().modeCoordinator;

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
        IPlayerSession session = sessionManager.get(uuid);
        if ((options.modeInvincible && modeCoordinator.isInMode(uuid) || (!options.modeFreezeDamage && session.isFrozen()))) {
            event.setCancelled(true);
        }
    }
}