package net.shortninja.staffplus.server.listener.player;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.attribute.InventorySerializer;
import net.shortninja.staffplus.player.attribute.mode.handler.ReviveHandler;
import net.shortninja.staffplus.staff.tracing.TraceService;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeath implements Listener {
    private final ReviveHandler reviveHandler = StaffPlus.get().reviveHandler;
    private final TraceService traceService = IocContainer.getTraceService();

    public PlayerDeath() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDeath(PlayerDeathEvent event) {
        reviveHandler.cacheInventory(event.getEntity());
        InventorySerializer serializer = new InventorySerializer(event.getEntity().getUniqueId());
        serializer.deleteFile();
        traceService.sendTraceMessage(event.getEntity().getUniqueId(), String.format("Died [%s]", event.getDeathMessage()));
    }
}