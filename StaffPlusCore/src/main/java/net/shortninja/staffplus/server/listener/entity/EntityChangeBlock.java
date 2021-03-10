package net.shortninja.staffplus.server.listener.entity;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.session.SessionManagerImpl;
import net.shortninja.staffplus.staff.mode.StaffModeService;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class EntityChangeBlock implements Listener {

    private final StaffModeService staffModeService = IocContainer.getModeCoordinator();
    private final SessionManagerImpl sessionManager = IocContainer.getSessionManager();
    public EntityChangeBlock() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockChange(EntityChangeBlockEvent event){
        if(event.getEntityType().equals(EntityType.PLAYER)){
            if(event.getBlock().getType().equals(Material.CROPS)){
                PlayerSession playerSession = sessionManager.get(event.getEntity().getUniqueId());
                if(playerSession.isInStaffMode() || playerSession.isVanished())
                    event.setCancelled(true);
            }
        }
    }
}
