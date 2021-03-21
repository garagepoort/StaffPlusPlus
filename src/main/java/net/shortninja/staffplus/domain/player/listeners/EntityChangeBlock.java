package net.shortninja.staffplus.domain.player.listeners;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.domain.staff.mode.StaffModeService;
import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.session.SessionManagerImpl;
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
        String material = "FARMLAND";
        if(event.getEntityType().equals(EntityType.PLAYER)){
            if(event.getBlock().getType().equals(Material.valueOf(material))){
                PlayerSession playerSession = sessionManager.get(event.getEntity().getUniqueId());
                if(playerSession.isInStaffMode() || playerSession.isVanished())
                    event.setCancelled(true);
            }
        }
    }
}
