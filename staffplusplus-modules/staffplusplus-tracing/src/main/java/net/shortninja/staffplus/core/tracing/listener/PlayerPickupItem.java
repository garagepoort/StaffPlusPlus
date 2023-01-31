package net.shortninja.staffplus.core.tracing.listener;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.tracing.TraceService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

import static net.shortninja.staffplus.core.tracing.TraceType.PICKUP_ITEM;

@IocBukkitListener
public class PlayerPickupItem implements Listener {
    private final TraceService traceService;

    public PlayerPickupItem(TraceService traceService) {
        this.traceService = traceService;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPickup(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (!event.isCancelled()) {
                traceService.sendTraceMessage(PICKUP_ITEM, player.getUniqueId(), String.format("Picked up item [%s]", event.getItem().getItemStack().getType()));
            }
        }

    }
}