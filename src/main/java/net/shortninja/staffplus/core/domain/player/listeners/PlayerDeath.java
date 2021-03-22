package net.shortninja.staffplus.core.domain.player.listeners;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.IocContainer;
import net.shortninja.staffplus.core.domain.staff.revive.ReviveHandler;
import net.shortninja.staffplus.core.domain.staff.tracing.TraceService;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeath implements Listener {
    private final ReviveHandler reviveHandler = StaffPlus.get().reviveHandler;
    private final TraceService traceService = IocContainer.get(TraceService.class);

    public PlayerDeath() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDeath(PlayerDeathEvent event) {
        reviveHandler.cacheInventory(event.getEntity());
        traceService.sendTraceMessage(event.getEntity().getUniqueId(), String.format("Died [%s]", event.getDeathMessage()));
    }
}