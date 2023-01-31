package net.shortninja.staffplus.core.tracing;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import static net.shortninja.staffplus.core.tracing.TraceType.WORLD_CHANGE;

@IocBukkitListener
public class TraceWorldChangeListener implements Listener {
    private final TraceService traceService;

    public TraceWorldChangeListener(TraceService traceService) {
        this.traceService = traceService;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onWorldChange(PlayerChangedWorldEvent event) {
        World currentWorld = event.getPlayer().getWorld();
        traceService.sendTraceMessage(WORLD_CHANGE, event.getPlayer().getUniqueId(), String.format("World changed from [%s] to [%s]", event.getFrom().getName(), currentWorld.getName()));
    }
}
