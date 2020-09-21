package net.shortninja.staffplus.server.listener.player;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.attribute.mode.ModeCoordinator;
import net.shortninja.staffplus.staff.freeze.FreezeHandler;
import net.shortninja.staffplus.server.data.config.Options;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.util.UUID;

public class PlayerDropItem implements Listener {
    private Options options = IocContainer.getOptions();
    private FreezeHandler freezeHandler = IocContainer.getFreezeHandler();
    private ModeCoordinator modeCoordinator = StaffPlus.get().modeCoordinator;

    public PlayerDropItem() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDrop(PlayerDropItemEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        if ((options.modeItemChange || !modeCoordinator.isInMode(uuid)) && !freezeHandler.isFrozen(uuid)) {
            return;
        }

        event.setCancelled(true);
    }
}