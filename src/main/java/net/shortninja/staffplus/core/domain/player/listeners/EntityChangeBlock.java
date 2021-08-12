package net.shortninja.staffplus.core.domain.player.listeners;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

@IocBean
public class EntityChangeBlock implements Listener {

    private final OnlineSessionsManager sessionManager;
    public EntityChangeBlock(OnlineSessionsManager sessionManager) {
        this.sessionManager = sessionManager;
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockChange(EntityChangeBlockEvent event){
        if(event.getEntityType().equals(EntityType.PLAYER)){
            if(event.getBlock().getType().equals(Material.CROPS)){
                OnlinePlayerSession playerSession = sessionManager.get((Player) event.getEntity());
                if(playerSession.isInStaffMode() || playerSession.isVanished())
                    event.setCancelled(true);
            }
        }
    }
}
