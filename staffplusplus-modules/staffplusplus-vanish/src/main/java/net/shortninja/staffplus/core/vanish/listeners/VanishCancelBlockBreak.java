package net.shortninja.staffplus.core.vanish.listeners;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

@IocBukkitListener(conditionalOnProperty = "vanish-module.block-break=false")
public class VanishCancelBlockBreak implements Listener {
    private final OnlineSessionsManager sessionManager;

    public VanishCancelBlockBreak(OnlineSessionsManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        OnlinePlayerSession session = sessionManager.get(player);

        if (session.isVanished()) {
            event.setCancelled(true);
        }
    }
}