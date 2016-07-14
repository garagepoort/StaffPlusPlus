package net.shortninja.staffplus.listener;

import java.util.UUID;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.data.config.Options;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.player.attribute.mode.ModeCoordinator;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamage implements Listener
{
	private Options options = StaffPlus.get().options;
	private UserManager userManager = StaffPlus.get().userManager;
	private ModeCoordinator modeCoordinator = StaffPlus.get().modeCoordinator;
	
	public EntityDamage()
	{
		Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onDamage(EntityDamageEvent event)
	{
		Entity entity = event.getEntity();
		
		if(!(entity instanceof Player))
		{
			return;
		}
		
		UUID uuid = ((Player) entity).getUniqueId();
		
		if((options.modeInvincible && modeCoordinator.isInMode(uuid) || (!options.modeFreezeDamage && userManager.getUser(uuid).isFrozen())))
		{
			event.setCancelled(true);
		}
	}
}