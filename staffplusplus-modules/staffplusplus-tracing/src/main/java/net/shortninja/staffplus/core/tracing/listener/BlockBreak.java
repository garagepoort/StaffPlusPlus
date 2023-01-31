package net.shortninja.staffplus.core.tracing.listener;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.tracing.TraceService;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import static net.shortninja.staffplus.core.tracing.TraceType.BLOCK_BREAK;

@IocBukkitListener
public class BlockBreak implements Listener {
    private final TraceService traceService;

    public BlockBreak(TraceService traceService) {
        this.traceService = traceService;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (event.isCancelled()) {
            Block block = event.getBlock();
            traceService.sendTraceMessage(BLOCK_BREAK, event.getPlayer().getUniqueId(),
                String.format("Block [%s] broken at [%s,%s,%s]", block.getType(), block.getX(), block.getY(), block.getZ()));
        }
    }
}