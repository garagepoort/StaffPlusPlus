package net.shortninja.staffplus.core.mode.listeners.bukkit;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.mode.config.GeneralModeConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Optional;

@IocBukkitListener
public class BlockBreak implements Listener {
    private final OnlineSessionsManager sessionManager;

    public BlockBreak(OnlineSessionsManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        OnlinePlayerSession session = sessionManager.get(player);

        Optional<GeneralModeConfiguration> mode = session.get("modeConfig");
        if (session.isInStaffMode() && !mode.get().isModeBlockManipulation()) {
            event.setCancelled(true);
        }
    }
}