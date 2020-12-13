package net.shortninja.staffplus.server.listener;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.alerts.xray.XrayService;
import net.shortninja.staffplus.staff.freeze.FreezeHandler;
import net.shortninja.staffplus.staff.mode.ModeCoordinator;
import net.shortninja.staffplus.staff.tracing.TraceService;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.UUID;

import static net.shortninja.staffplus.staff.tracing.TraceType.BLOCK_BREAK;

public class BlockBreak implements Listener {
    private final Options options = IocContainer.getOptions();
    private final FreezeHandler freezeHandler = IocContainer.getFreezeHandler();
    private final ModeCoordinator modeCoordinator = IocContainer.getModeCoordinator();
    private final XrayService xrayService = IocContainer.getXrayService();
    private final TraceService traceService = IocContainer.getTraceService();

    public BlockBreak() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (freezeHandler.isFrozen(uuid)) {
            event.setCancelled(true);
            return;
        }

        if (options.modeBlockManipulation || !modeCoordinator.isInMode(player.getUniqueId())) {
            Block block = event.getBlock();
            xrayService.handleBlockBreak(block.getType(), player);
            traceService.sendTraceMessage(BLOCK_BREAK, event.getPlayer().getUniqueId(),
                String.format("Block [%s] broken at [%s,%s,%s]", block.getType(), block.getX(), block.getY(), block.getZ()));
            return;
        }

        event.setCancelled(true);
    }
}