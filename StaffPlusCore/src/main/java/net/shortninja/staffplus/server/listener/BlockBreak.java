package net.shortninja.staffplus.server.listener;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.server.AlertCoordinator;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.freeze.FreezeHandler;
import net.shortninja.staffplus.staff.mode.ModeCoordinator;
import net.shortninja.staffplus.staff.tracing.TraceService;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static net.shortninja.staffplus.staff.tracing.TraceType.BLOCK_BREAK;

public class BlockBreak implements Listener {
    private final BlockFace[] FACES =
        {
            BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST,
            BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST,
            BlockFace.UP, BlockFace.DOWN
        };
    private final Options options = IocContainer.getOptions();
    private final FreezeHandler freezeHandler = IocContainer.getFreezeHandler();
    private final ModeCoordinator modeCoordinator = IocContainer.getModeCoordinator();
    private final AlertCoordinator alertCoordinator = IocContainer.getAlertCoordinator();
    private final Set<Location> locations = new HashSet<>();
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

            if (options.alertsXrayBlocks.contains(block.getType())) {
                int start = alertCoordinator.getNotifiedAmount();
                int amount = 0;

                alertCoordinator.addNotified(block.getLocation());
                //TODO FIX THIS calculateVein(block.getType(), block, false);
                amount = alertCoordinator.getNotifiedAmount() - start;
                locations.clear();
                if (amount > 0) {
                    int lightLevel = player.getLocation().getBlock().getLightLevel();

                    alertCoordinator.onXray(player.getName(), amount, block.getType(), lightLevel);
                }
            }

            traceService.sendTraceMessage(BLOCK_BREAK, event.getPlayer().getUniqueId(),
                String.format("Block [%s] broken at [%s,%s,%s]", block.getType(), block.getX(), block.getY(), block.getZ()));
            return;
        }

        event.setCancelled(true);
    }

    private void calculateVein(Material referenceType, Block block, boolean shouldCheck) {
        locations.add(block.getLocation());
        if (shouldCheck && (block.getType() != referenceType || alertCoordinator.hasNotified(block.getLocation()))) {
            return;
        } else {
            alertCoordinator.addNotified(block.getLocation());
        }
        for (BlockFace face : FACES) {
            if (locations.contains(block.getRelative(face).getLocation()))
                continue;
            calculateVein(referenceType, block.getRelative(face), true);

        }
    }
}