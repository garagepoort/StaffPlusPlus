package net.shortninja.staffplus.core.domain.player.listeners;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.session.PlayerSession;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.UUID;

@IocBean
public class EntityDamage implements Listener {
    private final Options options;
    private final SessionManagerImpl sessionManager;

    public EntityDamage(Options options, SessionManagerImpl sessionManager) {
        this.options = options;
        this.sessionManager = sessionManager;
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
        if ((options.modeConfiguration.isModeInvincible() && session.isInStaffMode() || (!options.staffItemsConfiguration.getFreezeModeConfiguration().isModeFreezeDamage() && session.isFrozen()) || session.isProtected())) {
            event.setCancelled(true);
        }
    }
}