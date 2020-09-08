package net.shortninja.staffplus.server.listener;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.attribute.mode.ModeCoordinator;
import net.shortninja.staffplus.player.attribute.mode.handler.freeze.FreezeHandler;
import net.shortninja.staffplus.server.data.config.Options;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.UUID;

public class BlockPlace implements Listener {
    private Options options = StaffPlus.get().options;
    private FreezeHandler freezeHandler = StaffPlus.get().freezeHandler;
    private ModeCoordinator modeCoordinator = StaffPlus.get().modeCoordinator;

    public BlockPlace() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        if ((options.modeBlockManipulation || !modeCoordinator.isInMode(uuid)) && !freezeHandler.isFrozen(uuid)) {
            return;
        }

        event.setCancelled(true);
    }
}