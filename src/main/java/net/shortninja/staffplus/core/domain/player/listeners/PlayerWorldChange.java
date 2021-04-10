package net.shortninja.staffplus.core.domain.player.listeners;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.domain.staff.mode.StaffModeService;
import net.shortninja.staffplus.core.domain.staff.tracing.TraceService;
import net.shortninja.staffplus.core.session.PlayerSession;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import static net.shortninja.staffplus.core.domain.staff.tracing.TraceType.WORLD_CHANGE;

@IocBean
@IocListener
public class PlayerWorldChange implements Listener {
    private final StaffModeService staffModeService;
    private final TraceService traceService;
    private final SessionManagerImpl sessionManager;

    public PlayerWorldChange(StaffModeService staffModeService, TraceService traceService, SessionManagerImpl sessionManager) {
        this.staffModeService = staffModeService;
        this.traceService = traceService;
        this.sessionManager = sessionManager;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onWorldChange(PlayerChangedWorldEvent event) {
        PlayerSession session = sessionManager.get(event.getPlayer().getUniqueId());
        World currentWorld = event.getPlayer().getWorld();

        if (session.isInStaffMode() && (session.getModeConfiguration().get().isDisableOnWorldChange() || !session.getModeConfiguration().get().isModeValidInWorld(currentWorld))) {
            staffModeService.turnStaffModeOff(event.getPlayer());
        }
        traceService.sendTraceMessage(WORLD_CHANGE, event.getPlayer().getUniqueId(), String.format("World changed from [%s] to [%s]", event.getFrom().getName(), currentWorld.getName()));
    }
}
