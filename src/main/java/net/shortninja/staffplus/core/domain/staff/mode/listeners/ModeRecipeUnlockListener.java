package net.shortninja.staffplus.core.domain.staff.mode.listeners;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRecipeDiscoverEvent;

@IocBukkitListener
public class ModeRecipeUnlockListener implements Listener {
    private final OnlineSessionsManager sessionManager;

    public ModeRecipeUnlockListener(OnlineSessionsManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onRecipeUnlock(PlayerRecipeDiscoverEvent event)  {
        Player player = event.getPlayer();
        OnlinePlayerSession session = sessionManager.get(player);
        
        if (!session.isInStaffMode()) return;
        
        event.setCancelled(true);
    }
}