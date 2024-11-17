package net.shortninja.staffplus.core.domain.staff.mode;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

@IocBukkitListener
public class ModeKeepItemsOnDeath implements Listener {
    private final OnlineSessionsManager sessionManager;

    public ModeKeepItemsOnDeath(OnlineSessionsManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeath(PlayerDeathEvent event)  {
        Player player = event.getEntity();
        OnlinePlayerSession session = sessionManager.get(player);
        
        if (!session.isInStaffMode()) return;
        
        event.setKeepInventory(true);
        event.setKeepLevel(true);
        
        event.getDrops().clear();
        event.setDroppedExp(0);
    }
}