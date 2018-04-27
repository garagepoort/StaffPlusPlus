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

    public PlayerWorldChange()
    {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onWorldChange(PlayerChangedWorldEvent event){
        if(modeCoordinator.isInMode(event.getPlayer().getUniqueId()))
            modeCoordinator.removeMode(event.getPlayer());
    }
}
