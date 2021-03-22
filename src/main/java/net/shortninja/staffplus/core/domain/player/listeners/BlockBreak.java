package net.shortninja.staffplus.core.domain.player.listeners;

import net.shortninja.staffplus.core.StaffPlus;
import be.garagepoort.mcioc.IocContainer;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.domain.staff.alerts.xray.XrayService;
import net.shortninja.staffplus.core.domain.staff.freeze.FreezeHandler;
import net.shortninja.staffplus.core.domain.staff.tracing.TraceService;
import net.shortninja.staffplus.core.session.PlayerSession;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.UUID;

import static net.shortninja.staffplus.core.domain.staff.tracing.TraceType.BLOCK_BREAK;

public class BlockBreak implements Listener {
    private final Options options = IocContainer.get(Options.class);
    private final FreezeHandler freezeHandler = IocContainer.get(FreezeHandler.class);
    private final XrayService xrayService = IocContainer.get(XrayService.class);
    private final TraceService traceService = IocContainer.get(TraceService.class);
    private final SessionManagerImpl sessionManager = IocContainer.get(SessionManagerImpl.class);

    public BlockBreak() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        PlayerSession session = sessionManager.get(player.getUniqueId());
        UUID uuid = player.getUniqueId();

        if (freezeHandler.isFrozen(uuid)) {
            event.setCancelled(true);
            return;
        }

        if (options.modeConfiguration.isModeBlockManipulation() || !session.isInStaffMode()) {
            Block block = event.getBlock();
            xrayService.handleBlockBreak(block.getType(), player);
            traceService.sendTraceMessage(BLOCK_BREAK, event.getPlayer().getUniqueId(),
                String.format("Block [%s] broken at [%s,%s,%s]", block.getType(), block.getX(), block.getY(), block.getZ()));
            return;
        }

        event.setCancelled(true);
    }
}