package net.shortninja.staffplus.domain.player.listeners;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.common.config.Options;
import net.shortninja.staffplus.domain.staff.mode.StaffModeService;
import net.shortninja.staffplus.domain.staff.tracing.TraceService;
import net.shortninja.staffplus.session.SessionManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import static net.shortninja.staffplus.domain.staff.tracing.TraceType.WORLD_CHANGE;

public class PlayerWorldChange implements Listener {
    private final Options options = IocContainer.getOptions();
    private final StaffModeService staffModeService = IocContainer.getModeCoordinator();
    private final TraceService traceService = IocContainer.getTraceService();
    private final SessionManagerImpl sessionManager = IocContainer.getSessionManager();

    public PlayerWorldChange() {
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
