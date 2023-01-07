package net.shortninja.staffplus.core.mode.listeners;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.mode.config.GeneralModeConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Optional;

@IocBukkitListener
public class BlockPlacement implements Listener {
    private final OnlineSessionsManager sessionManager;

    public BlockPlacement(OnlineSessionsManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        OnlinePlayerSession session = sessionManager.get(event.getPlayer());
        Optional<GeneralModeConfiguration> modeConfig = session.get("modeConfig");
        if (session.isInStaffMode() && !modeConfig.get().isModeBlockManipulation()) {
            event.setCancelled(true);
        }
    }
}