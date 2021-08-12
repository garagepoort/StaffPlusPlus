package net.shortninja.staffplus.core.domain.player.listeners;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.staff.mode.StaffModeService;
import net.shortninja.staffplus.core.domain.staff.tracing.TraceService;
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
    private final OnlineSessionsManager sessionManager;
    private final BukkitUtils bukkitUtils;

    public PlayerWorldChange(StaffModeService staffModeService, TraceService traceService, OnlineSessionsManager sessionManager, BukkitUtils bukkitUtils) {
        this.staffModeService = staffModeService;
        this.traceService = traceService;
        this.sessionManager = sessionManager;
        this.bukkitUtils = bukkitUtils;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onWorldChange(PlayerChangedWorldEvent event) {
        OnlinePlayerSession session = sessionManager.get(event.getPlayer());
        World currentWorld = event.getPlayer().getWorld();

        if (session.isInStaffMode() && (session.getModeConfig().get().isDisableOnWorldChange() || !session.getModeConfig().get().isModeValidInWorld(currentWorld))) {
            bukkitUtils.runTaskAsync(event.getPlayer(), () -> staffModeService.turnStaffModeOff(event.getPlayer()));
        }

        traceService.sendTraceMessage(WORLD_CHANGE, event.getPlayer().getUniqueId(), String.format("World changed from [%s] to [%s]", event.getFrom().getName(), currentWorld.getName()));
    }
}
