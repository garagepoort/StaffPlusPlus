package net.shortninja.staffplus.server.listener;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.attribute.mode.ModeCoordinator;
import net.shortninja.staffplus.server.AlertCoordinator;
import net.shortninja.staffplus.server.data.config.Options;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreak implements Listener
{
	private Options options = StaffPlus.get().options;
	private ModeCoordinator modeCoordinator = StaffPlus.get().modeCoordinator;
	private AlertCoordinator alertCoordinator = StaffPlus.get().alertCoordinator;
	
	public BlockBreak()
	{
		Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBreak(BlockBreakEvent event)
	{
		Player player = event.getPlayer();
		
		if(options.modeBlockManipulation || !modeCoordinator.isInMode(player.getUniqueId()))
		{
			Block block = event.getBlock();
			
			if(options.alertsXrayBlocks.contains(block.getType()))
			{
				int start = alertCoordinator.getNotifiedAmount();
				int amount = 0;
				
				alertCoordinator.addNotified(block.getLocation());
				calculateVein(block.getType(), block, false);
				amount = alertCoordinator.getNotifiedAmount() - start;
				
				if(amount > 0)
				{
					int lightLevel = block.getLightFromBlocks() + block.getLightFromSky();
					
					alertCoordinator.onXray(player.getName(), amount, block.getType(), lightLevel);
				}
			}
			
			return;
		}
		
		event.setCancelled(true);
	}
	
	private void calculateVein(Material referenceType, Block block, boolean shouldCheck)
	{
		if(shouldCheck && (block.getType() != referenceType || alertCoordinator.hasNotified(block.getLocation())))
		{
			return;
		}else alertCoordinator.addNotified(block.getLocation());
		
		for(BlockFace face : FACES)
		{
			calculateVein(referenceType, block.getRelative(face), true);
		}
	}
	
	private final BlockFace[] FACES =
	{
		BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST,
		BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST,
		BlockFace.UP, BlockFace.DOWN
	};
}