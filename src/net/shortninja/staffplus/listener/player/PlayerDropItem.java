package net.shortninja.staffplus.listener.player;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.data.config.Options;
import net.shortninja.staffplus.player.attribute.mode.ModeCoordinator;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItem implements Listener
{
	private Options options = StaffPlus.get().options;
	private ModeCoordinator modeCoordinator = StaffPlus.get().modeCoordinator;
	
	public PlayerDropItem()
	{
		Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onDrop(PlayerDropItemEvent event)
	{
		Player player = event.getPlayer();
		
		if(options.modeBlockManipulation || !modeCoordinator.isInMode(player.getUniqueId()))
		{
			return;
		}
		
		event.setCancelled(true);
	}
}