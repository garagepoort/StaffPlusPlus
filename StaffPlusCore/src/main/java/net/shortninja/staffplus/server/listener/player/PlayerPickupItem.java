package net.shortninja.staffplus.server.listener.player;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.attribute.mode.ModeCoordinator;
import net.shortninja.staffplus.server.data.config.Options;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class PlayerPickupItem implements Listener {
    private Options options = IocContainer.getOptions();
    private ModeCoordinator modeCoordinator = StaffPlus.get().modeCoordinator;

    public PlayerPickupItem() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPickup(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (options.modeItemChange || !modeCoordinator.isInMode(player.getUniqueId())) {
                IocContainer.getTraceService().sendTraceMessage(player.getUniqueId(), String.format("Picked up item [%s]", event.getItem().getType()));
                return;
            }
            event.setCancelled(true);
        }

    }
}