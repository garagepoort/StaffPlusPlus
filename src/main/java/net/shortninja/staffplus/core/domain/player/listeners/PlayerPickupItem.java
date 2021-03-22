package net.shortninja.staffplus.core.domain.player.listeners;

import net.shortninja.staffplus.core.StaffPlus;
import be.garagepoort.mcioc.IocContainer;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.domain.staff.mode.StaffModeService;
import net.shortninja.staffplus.core.domain.staff.tracing.TraceService;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

import static net.shortninja.staffplus.core.domain.staff.tracing.TraceType.PICKUP_ITEM;

public class PlayerPickupItem implements Listener {
    private final Options options = IocContainer.get(Options.class);
    private final StaffModeService staffModeService = IocContainer.get(StaffModeService.class);
    private final TraceService traceService = IocContainer.get(TraceService.class);
    private final SessionManagerImpl sessionManager = IocContainer.get(SessionManagerImpl.class);

    public PlayerPickupItem() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPickup(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (options.modeConfiguration.isModeItemPickup() || !sessionManager.get(player.getUniqueId()).isInStaffMode()) {
                traceService.sendTraceMessage(PICKUP_ITEM, player.getUniqueId(), String.format("Picked up item [%s]", event.getItem().getItemStack().getType()));
                return;
            }
            event.setCancelled(true);
        }

    }
}