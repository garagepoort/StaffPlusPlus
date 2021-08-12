package net.shortninja.staffplus.core.domain.player.listeners;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.domain.staff.tracing.TraceService;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import static net.shortninja.staffplus.core.domain.staff.tracing.TraceType.DROP_ITEM;

@IocBean
public class PlayerDropItem implements Listener {
    private final TraceService traceService;
    private final OnlineSessionsManager sessionManager;

    public PlayerDropItem(TraceService traceService, OnlineSessionsManager sessionManager) {
        this.traceService = traceService;
        this.sessionManager = sessionManager;
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onDrop(PlayerDropItemEvent event) {

        OnlinePlayerSession session = sessionManager.get(event.getPlayer());
        if ((!session.isInStaffMode() || session.getModeConfig().get().isModeItemDrop()) && !session.isFrozen()) {
            traceService.sendTraceMessage(DROP_ITEM, event.getPlayer().getUniqueId(), String.format("Dropped item [%s]", event.getItemDrop().getType()));
            return;
        }

        event.setCancelled(true);
    }
}