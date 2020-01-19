package net.shortninja.staffplus.server.listener.player;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.attribute.mode.ModeCoordinator;
import net.shortninja.staffplus.server.data.config.Options;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PlayerPickupItem implements Listener {
    private Options options = StaffPlus.get().options;
    private ModeCoordinator modeCoordinator = StaffPlus.get().modeCoordinator;

    public PlayerPickupItem() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();

        if (options.modeItemChange || !modeCoordinator.isInMode(player.getUniqueId())) {
            return;
        }

        event.setCancelled(true);
    }
}