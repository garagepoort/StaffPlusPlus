package net.shortninja.staffplus.core.domain.player.listeners;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.IocContainer;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.domain.staff.freeze.FreezeHandler;
import net.shortninja.staffplus.core.domain.staff.mode.StaffModeService;
import net.shortninja.staffplus.core.domain.staff.tracing.TraceService;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.util.UUID;

import static net.shortninja.staffplus.core.domain.staff.tracing.TraceType.DROP_ITEM;

public class PlayerDropItem implements Listener {
    private final Options options = IocContainer.get(Options.class);
    private final FreezeHandler freezeHandler = IocContainer.get(FreezeHandler.class);
    private final StaffModeService staffModeService = IocContainer.get(StaffModeService.class);
    private final TraceService traceService = IocContainer.get(TraceService.class);
    private final SessionManagerImpl sessionManager = IocContainer.get(SessionManagerImpl.class);

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