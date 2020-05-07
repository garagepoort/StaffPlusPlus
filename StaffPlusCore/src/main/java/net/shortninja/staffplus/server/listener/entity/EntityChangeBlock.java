package net.shortninja.staffplus.server.listener.entity;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.player.attribute.mode.ModeCoordinator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class EntityChangeBlock implements Listener {

    private ModeCoordinator modeCoordinator = StaffPlus.get().modeCoordinator;
    private UserManager userManager = StaffPlus.get().userManager;
    public EntityChangeBlock() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockChange(EntityChangeBlockEvent event){
        if(event.getEntityType().equals(EntityType.PLAYER)){
            if(event.getBlock().getType().equals(Material.FARMLAND)){
                if(modeCoordinator.isInMode(event.getEntity().getUniqueId()) ||
                        userManager.get(event.getEntity().getUniqueId()).isVanished())
                    event.setCancelled(true);
            }
        }
    }
}
