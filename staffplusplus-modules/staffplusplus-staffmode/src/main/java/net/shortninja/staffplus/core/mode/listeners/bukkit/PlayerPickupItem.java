package net.shortninja.staffplus.core.mode.listeners.bukkit;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.mode.config.GeneralModeConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

import java.util.Optional;

@IocBukkitListener
public class PlayerPickupItem implements Listener {
    private final OnlineSessionsManager sessionManager;

    public PlayerPickupItem(OnlineSessionsManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPickup(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            OnlinePlayerSession session = sessionManager.get(player);
            Optional<GeneralModeConfiguration> modeConfig = session.get("modeConfig");
            if (session.isInStaffMode() && !modeConfig.get().isModeItemPickup()) {
                event.setCancelled(true);
            }
        }
    }
}