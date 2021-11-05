package net.shortninja.staffplus.core.domain.staff.mode;

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
import org.bukkit.event.player.PlayerTeleportEvent;

import static net.shortninja.staffplus.core.domain.staff.tracing.TraceType.WORLD_CHANGE;

@IocBean
@IocListener
public class StaffModeWorldChangeListener implements Listener {
    private final StaffModeService staffModeService;
    private final OnlineSessionsManager sessionManager;
    private final BukkitUtils bukkitUtils;

    public StaffModeWorldChangeListener(StaffModeService staffModeService, OnlineSessionsManager sessionManager, BukkitUtils bukkitUtils) {
        this.staffModeService = staffModeService;
        this.sessionManager = sessionManager;
        this.bukkitUtils = bukkitUtils;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onWorldChange(PlayerTeleportEvent event) {
        OnlinePlayerSession session = sessionManager.get(event.getPlayer());
        World currentWorld = event.getPlayer().getWorld();
        World toWorld = event.getTo().getWorld();
        boolean worldChanged = !currentWorld.equals(toWorld);

        if(!worldChanged || !session.isInStaffMode()) {
            return;
        }

        if (session.getModeConfig().get().isDisableOnWorldChange() || !session.getModeConfig().get().isModeValidInWorld(toWorld)) {
            bukkitUtils.runTaskAsync(event.getPlayer(), () -> staffModeService.turnStaffModeOffAndTeleport(event.getPlayer(), event.getTo()));
            event.setCancelled(true);
        }
    }
}
