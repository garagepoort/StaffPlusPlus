package net.shortninja.staffplus.server.listener.entity;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.session.SessionManager;
import net.shortninja.staffplus.staff.mode.ModeCoordinator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class EntityChangeBlock implements Listener {

    private final ModeCoordinator modeCoordinator = StaffPlus.get().modeCoordinator;
    private final SessionManager sessionManager = IocContainer.getSessionManager();
    public EntityChangeBlock() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockChange(EntityChangeBlockEvent event){
        String material = "FARMLAND";
        if(event.getEntityType().equals(EntityType.PLAYER)){
            if(event.getBlock().getType().equals(Material.valueOf(material))){
                if(modeCoordinator.isInMode(event.getEntity().getUniqueId()) ||
                        sessionManager.get(event.getEntity().getUniqueId()).isVanished())
                    event.setCancelled(true);
            }
        }
    }
}
