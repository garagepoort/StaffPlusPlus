package net.shortninja.staffplus.core.mode.listeners;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.mode.config.GeneralModeConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.util.Optional;

@IocBukkitListener
public class PlayerDropItem implements Listener {
    private final OnlineSessionsManager sessionManager;

    public PlayerDropItem(OnlineSessionsManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onDrop(PlayerDropItemEvent event) {
        OnlinePlayerSession session = sessionManager.get(event.getPlayer());
        Optional<GeneralModeConfiguration> mode = session.get("modeConfig");
        if (session.isInStaffMode() && !mode.get().isModeItemDrop()) {
            event.setCancelled(true);
        }
    }
}