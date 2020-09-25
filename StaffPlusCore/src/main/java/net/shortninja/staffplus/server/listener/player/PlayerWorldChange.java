package net.shortninja.staffplus.server.listener.player;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.staff.mode.ModeCoordinator;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.tracing.TraceService;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import static net.shortninja.staffplus.staff.tracing.TraceType.WORLD_CHANGE;

public class PlayerWorldChange implements Listener {
    private final Options options = IocContainer.getOptions();
    private final ModeCoordinator modeCoordinator = StaffPlus.get().modeCoordinator;
    private final TraceService traceService = IocContainer.getTraceService();

    public PlayerWorldChange() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }


    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onWorldChange(PlayerChangedWorldEvent event) {
        if (modeCoordinator.isInMode(event.getPlayer().getUniqueId()) && options.worldChange) {
            modeCoordinator.removeMode(event.getPlayer());
        }
        traceService.sendTraceMessage(WORLD_CHANGE, event.getPlayer().getUniqueId(), String.format("World changed from [%s] to [%s]", event.getFrom().getName(), event.getPlayer().getWorld().getName()));
    }
}
