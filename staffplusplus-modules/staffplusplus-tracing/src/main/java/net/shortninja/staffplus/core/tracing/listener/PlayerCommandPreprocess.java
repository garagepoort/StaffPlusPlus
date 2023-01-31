package net.shortninja.staffplus.core.tracing.listener;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.tracing.TraceService;
import net.shortninja.staffplus.core.tracing.TraceType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.UUID;

@IocBukkitListener
public class PlayerCommandPreprocess implements Listener {

    private final TraceService traceService;

    public PlayerCommandPreprocess(TraceService traceService) {
        this.traceService = traceService;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        String command = event.getMessage().toLowerCase();
        traceService.sendTraceMessage(TraceType.COMMANDS, uuid, "Player invoked command: [" + command + "]");
    }

}