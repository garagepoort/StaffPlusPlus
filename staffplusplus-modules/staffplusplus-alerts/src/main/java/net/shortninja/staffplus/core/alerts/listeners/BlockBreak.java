package net.shortninja.staffplus.core.alerts.listeners;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.alerts.xray.XrayService;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

@IocBukkitListener
public class BlockBreak implements Listener {
    private final XrayService xrayService;

    public BlockBreak(XrayService xrayService) {
        this.xrayService = xrayService;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (event.isCancelled()) {
            Block block = event.getBlock();
            xrayService.handleBlockBreak(block, player);
        }
    }
}