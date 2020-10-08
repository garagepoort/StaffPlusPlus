package net.shortninja.staffplus.staff.protect;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import org.bukkit.Bukkit;
import org.bukkit.block.Container;
import org.bukkit.block.data.Powerable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ProtectListener implements Listener {
    private final ProtectService protectService = IocContainer.getProtectService();


    public ProtectListener() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        if (protectService.isLocationProtect(event.getPlayer(), event.getBlockClicked().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getClickedBlock() != null) {
            if ((event.getClickedBlock().getState() instanceof Container || event.getClickedBlock().getBlockData() instanceof Powerable) && protectService.isLocationProtect(player, event.getClickedBlock().getLocation())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (protectService.isLocationProtect(player, event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlace(BlockPlaceEvent event) {
        if (protectService.isLocationProtect(event.getPlayer(), event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }
}
