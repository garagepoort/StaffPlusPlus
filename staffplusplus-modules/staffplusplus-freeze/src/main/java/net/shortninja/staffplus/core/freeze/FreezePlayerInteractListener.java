package net.shortninja.staffplus.core.freeze;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

@IocBukkitListener
public class FreezePlayerInteractListener implements Listener {

    private final OnlineSessionsManager sessionManager;

    public FreezePlayerInteractListener(OnlineSessionsManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        OnlinePlayerSession playerSession = sessionManager.get(player);
        if (playerSession.isFrozen() &&
            (event.getAction() == Action.LEFT_CLICK_AIR ||
                event.getAction() == Action.LEFT_CLICK_BLOCK ||
                event.getAction() == Action.RIGHT_CLICK_AIR ||
                event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            event.setCancelled(true);
        }
    }
}
