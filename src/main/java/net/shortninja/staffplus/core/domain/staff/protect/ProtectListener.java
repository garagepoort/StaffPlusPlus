package net.shortninja.staffplus.core.domain.staff.protect;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import org.bukkit.Bukkit;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;

@IocBean
public class ProtectListener implements Listener {
    private final ProtectService protectService;

    public ProtectListener(ProtectService protectService) {
        this.protectService = protectService;
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        if (protectService.isLocationProtected(event.getPlayer(), event.getBlockClicked().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getClickedBlock() != null) {
            if ((event.getClickedBlock().getState() instanceof Container
                || event.getClickedBlock().getType().name().contains("BUTTON")
                || event.getClickedBlock().getType().name().contains("LEVER")) && protectService.isLocationProtected(player, event.getClickedBlock().getLocation())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (protectService.isLocationProtected(player, event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlace(BlockPlaceEvent event) {
        if (protectService.isLocationProtected(event.getPlayer(), event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }
}
