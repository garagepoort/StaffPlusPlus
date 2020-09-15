package net.shortninja.staffplus.server.listener;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.attribute.mode.ModeCoordinator;
import net.shortninja.staffplus.player.attribute.mode.handler.freeze.FreezeHandler;
import net.shortninja.staffplus.server.AlertCoordinator;
import net.shortninja.staffplus.server.data.config.Options;
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

public class BlockBreak implements Listener {
    private final BlockFace[] FACES =
            {
                    BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST,
                    BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST,
                    BlockFace.UP, BlockFace.DOWN
            };
    private Options options = IocContainer.getOptions();
    private FreezeHandler freezeHandler = StaffPlus.get().freezeHandler;
    private ModeCoordinator modeCoordinator = StaffPlus.get().modeCoordinator;
    private AlertCoordinator alertCoordinator = StaffPlus.get().alertCoordinator;
    private Set<Location> locations = new HashSet<>();

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