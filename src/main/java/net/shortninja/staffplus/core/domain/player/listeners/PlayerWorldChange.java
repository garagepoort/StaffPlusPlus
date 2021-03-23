package net.shortninja.staffplus.core.domain.player.listeners;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.domain.staff.mode.StaffModeService;
import net.shortninja.staffplus.core.domain.staff.tracing.TraceService;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import static net.shortninja.staffplus.core.domain.staff.tracing.TraceType.WORLD_CHANGE;

@IocBean
public class PlayerWorldChange implements Listener {
    private final Options options;
    private final StaffModeService staffModeService;
    private final TraceService traceService;
    private final SessionManagerImpl sessionManager;

    public PlayerWorldChange(Options options, StaffModeService staffModeService, TraceService traceService, SessionManagerImpl sessionManager) {
        this.options = options;
        this.staffModeService = staffModeService;
        this.traceService = traceService;
        this.sessionManager = sessionManager;
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }


    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onWorldChange(PlayerChangedWorldEvent event) {
        if (sessionManager.get(event.getPlayer().getUniqueId()).isInStaffMode() && options.modeConfiguration.isWorldChange()) {
            staffModeService.removeMode(event.getPlayer());
        }
        traceService.sendTraceMessage(WORLD_CHANGE, event.getPlayer().getUniqueId(), String.format("World changed from [%s] to [%s]", event.getFrom().getName(), event.getPlayer().getWorld().getName()));
    }
}
