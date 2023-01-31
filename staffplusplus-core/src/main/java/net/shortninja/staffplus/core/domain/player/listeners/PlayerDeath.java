package net.shortninja.staffplus.core.domain.player.listeners;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.domain.staff.revive.ReviveHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

@IocBukkitListener
public class PlayerDeath implements Listener {
    private final ReviveHandler reviveHandler;

    public PlayerDeath(ReviveHandler reviveHandler) {
        this.reviveHandler = reviveHandler;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDeath(PlayerDeathEvent event) {
        reviveHandler.cacheInventory(event.getEntity());
    }
}