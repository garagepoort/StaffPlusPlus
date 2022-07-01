package net.shortninja.staffplus.core.domain.player.listeners;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.domain.staff.revive.ReviveHandler;
import net.shortninja.staffplus.core.domain.staff.tracing.TraceService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

@IocBukkitListener
public class PlayerDeath implements Listener {
    private final ReviveHandler reviveHandler;
    private final TraceService traceService;

    public PlayerDeath(ReviveHandler reviveHandler, TraceService traceService) {
        this.reviveHandler = reviveHandler;
        this.traceService = traceService;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDeath(PlayerDeathEvent event) {
        reviveHandler.cacheInventory(event.getEntity());
        traceService.sendTraceMessage(event.getEntity().getUniqueId(), String.format("Died [%s]", event.getDeathMessage()));
    }
}