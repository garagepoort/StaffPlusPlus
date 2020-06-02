package net.shortninja.staffplus.server.listener.player;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.attribute.mode.ModeCoordinator;
import net.shortninja.staffplus.server.data.config.Options;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class PlayerWorldChange implements Listener {
    private Options options = StaffPlus.get().options;
    private ModeCoordinator modeCoordinator = StaffPlus.get().modeCoordinator;

    public PlayerWorldChange() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

<<<<<<< HEAD
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
=======
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
>>>>>>> b2eb803718fc6d2d09f3ef627210b17920278857
    public void onWorldChange(PlayerChangedWorldEvent event) {
        if (modeCoordinator.isInMode(event.getPlayer().getUniqueId()) && options.worldChange) {
            modeCoordinator.removeMode(event.getPlayer());
        }
    }
}
