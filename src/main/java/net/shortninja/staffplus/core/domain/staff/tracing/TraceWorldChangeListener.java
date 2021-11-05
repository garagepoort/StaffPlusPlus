package net.shortninja.staffplus.core.domain.staff.tracing;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.staff.mode.StaffModeService;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import static net.shortninja.staffplus.core.domain.staff.tracing.TraceType.WORLD_CHANGE;

@IocBean
@IocListener
public class TraceWorldChangeListener implements Listener {
    private final TraceService traceService;

    public TraceWorldChangeListener(TraceService traceService) {
        this.traceService = traceService;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onWorldChange(PlayerChangedWorldEvent event) {
        World currentWorld = event.getPlayer().getWorld();
        traceService.sendTraceMessage(WORLD_CHANGE, event.getPlayer().getUniqueId(), String.format("World changed from [%s] to [%s]", event.getFrom().getName(), currentWorld.getName()));
    }
}
