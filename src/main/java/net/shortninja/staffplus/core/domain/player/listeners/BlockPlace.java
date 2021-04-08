package net.shortninja.staffplus.core.domain.player.listeners;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.domain.staff.freeze.FreezeHandler;
import net.shortninja.staffplus.core.domain.staff.tracing.TraceService;
import net.shortninja.staffplus.core.domain.staff.tracing.TraceType;
import net.shortninja.staffplus.core.session.PlayerSession;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.UUID;

@IocBean
public class BlockPlace implements Listener {
    private final FreezeHandler freezeHandler;
    private final TraceService traceService;
    private final SessionManagerImpl sessionManager;

    public BlockPlace(FreezeHandler freezeHandler, TraceService traceService, SessionManagerImpl sessionManager) {
        this.freezeHandler = freezeHandler;
        this.traceService = traceService;
        this.sessionManager = sessionManager;
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        PlayerSession session = sessionManager.get(uuid);

        if ((!session.isInStaffMode() || session.getModeConfiguration().get().isModeBlockManipulation()) && !freezeHandler.isFrozen(uuid)) {
            traceService.sendTraceMessage(TraceType.BLOCK_PLACE, uuid, "Blocked [" + event.getBlock().getType() + "] placed");
            return;
        }

        event.setCancelled(true);
    }
}