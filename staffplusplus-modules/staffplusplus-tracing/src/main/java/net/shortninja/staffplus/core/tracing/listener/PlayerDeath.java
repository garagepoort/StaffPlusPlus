package net.shortninja.staffplus.core.tracing.listener;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.tracing.TraceService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

@IocBukkitListener
public class PlayerDeath implements Listener {
    private final TraceService traceService;

    public PlayerDeath(TraceService traceService) {
        this.traceService = traceService;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDeath(PlayerDeathEvent event) {
        traceService.sendTraceMessage(event.getEntity().getUniqueId(), String.format("Died [%s]", event.getDeathMessage()));
    }
}