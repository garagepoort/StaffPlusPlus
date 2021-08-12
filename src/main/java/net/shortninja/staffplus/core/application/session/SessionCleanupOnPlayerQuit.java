package net.shortninja.staffplus.core.application.session;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@IocBean
@IocListener
public class SessionCleanupOnPlayerQuit implements Listener {

    private final OnlineSessionsManager sessionManager;

    public SessionCleanupOnPlayerQuit(OnlineSessionsManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerQuit(PlayerQuitEvent playerQuitEvent) {
        sessionManager.remove(playerQuitEvent.getPlayer());
    }
}