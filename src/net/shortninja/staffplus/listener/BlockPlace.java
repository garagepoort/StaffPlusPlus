package net.shortninja.staffplus.listener;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.data.config.Options;
import net.shortninja.staffplus.player.attribute.mode.ModeCoordinator;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlace implements Listener
{
	private Options options = StaffPlus.get().options;
	private ModeCoordinator modeCoordinator = StaffPlus.get().modeCoordinator;
	
	public BlockPlace()
	{
		Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlace(BlockPlaceEvent event)
	{
		Player player = event.getPlayer();
		
		if(options.modeBlockManipulation || !modeCoordinator.isInMode(player.getUniqueId()))
		{
			return;
		}
		
		event.setCancelled(true);
	}
}