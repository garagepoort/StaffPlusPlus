package net.shortninja.staffplus.listener.entity;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.UserManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
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
		Entity entity = event.getDamager();
		
		if(!(entity instanceof Player))
		{
			return;
		}
		
		Player player = (Player) entity;
		
		if(userManager.getUser(player.getUniqueId()).isFrozen())
		{
			event.setCancelled(true);
		}
	}
}