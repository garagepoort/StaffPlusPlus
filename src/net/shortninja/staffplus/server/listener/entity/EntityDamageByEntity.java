package net.shortninja.staffplus.server.listener.entity;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.UserManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntity implements Listener
{
	private UserManager userManager = StaffPlus.get().userManager;
	
	public EntityDamageByEntity()
	{
		Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onDamage(EntityDamageByEntityEvent event)
	{
		Player player = null;
		
		if(!(event.getDamager() instanceof Player))
		{
			if(event.getDamager() instanceof Arrow)
			{
				Arrow arrow = (Arrow) event.getDamager();
				
				if(arrow.getShooter() instanceof Player)
				{
					player = (Player) arrow.getShooter();
				}
			}else return;
		}else player = (Player) event.getDamager();
		
		if(player != null && userManager.get(player.getUniqueId()).isFrozen())
		{
			event.setCancelled(true);
		}
	}
}