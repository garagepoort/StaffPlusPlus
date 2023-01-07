package net.shortninja.staffplus.core.domain.staff.tracing.listener;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.domain.staff.tracing.TraceService;
import net.shortninja.staffplus.core.domain.staff.tracing.TraceType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.UUID;

@IocBukkitListener
public class BlockPlace implements Listener {
    private final TraceService traceService;

    public BlockPlace(TraceService traceService) {
        this.traceService = traceService;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        if (!event.isCancelled()) {
            traceService.sendTraceMessage(TraceType.BLOCK_PLACE, uuid, "Block [" + event.getBlock().getType() + "] placed");
        }
    }
}