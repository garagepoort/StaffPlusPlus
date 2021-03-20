package net.shortninja.staffplus.domain.player.listeners;

import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.session.SessionManagerImpl;
import net.shortninja.staffplus.domain.staff.mode.StaffModeService;
import net.shortninja.staffplus.common.config.Options;
import net.shortninja.staffplus.domain.staff.freeze.FreezeHandler;
import net.shortninja.staffplus.domain.staff.tracing.TraceService;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.util.UUID;

import static net.shortninja.staffplus.domain.staff.tracing.TraceType.DROP_ITEM;

public class PlayerDropItem implements Listener {
    private final Options options = IocContainer.getOptions();
    private final FreezeHandler freezeHandler = IocContainer.getFreezeHandler();
    private final StaffModeService staffModeService = IocContainer.getModeCoordinator();
    private final TraceService traceService = IocContainer.getTraceService();
    private final SessionManagerImpl sessionManager = IocContainer.getSessionManager();

    public PlayerDropItem() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDrop(PlayerDropItemEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        if ((options.modeConfiguration.isModeItemDrop() || !sessionManager.get(uuid).isInStaffMode()) && !freezeHandler.isFrozen(uuid)) {
            traceService.sendTraceMessage(DROP_ITEM, event.getPlayer().getUniqueId(), String.format("Dropped item [%s]", event.getItemDrop().getType()));
            return;
        }

        event.setCancelled(true);
    }
}