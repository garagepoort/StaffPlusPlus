package net.shortninja.staffplus.core.tracing.listener;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.tracing.TraceService;
import net.shortninja.staffplus.core.tracing.TraceType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@IocBukkitListener
public class AsyncPlayerChat implements Listener {
    private final TraceService traceService;

    public AsyncPlayerChat(TraceService traceService) {
        this.traceService = traceService;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        traceService.sendTraceMessage(TraceType.CHAT, player.getUniqueId(), event.getMessage());
    }
}