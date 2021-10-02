package net.shortninja.staffplus.core.domain.staff.freeze.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

@IocBean(conditionalOnProperty = "freeze-module.enabled=true")
@IocListener
public class FreezePlayerMovementListener implements Listener {

    private final OnlineSessionsManager onlineSessionsManager;

    public FreezePlayerMovementListener(OnlineSessionsManager onlineSessionsManager) {
        this.onlineSessionsManager = onlineSessionsManager;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void cancelPlayerMoveIfFrozen(PlayerMoveEvent playerMoveEvent) {
        Player player = playerMoveEvent.getPlayer();
        PlayerSession session = onlineSessionsManager.get(player);
        if(session.isFrozen()) {
            playerMoveEvent.setCancelled(true);
        }
    }
}
