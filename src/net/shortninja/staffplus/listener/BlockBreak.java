package net.shortninja.staffplus.listener;

import java.util.HashSet;
import java.util.Set;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.data.config.Options;
import net.shortninja.staffplus.player.attribute.mode.ModeCoordinator;
import net.shortninja.staffplus.server.AlertCoordinator;
import net.shortninja.staffplus.util.Permission;

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
	private Permission permission = StaffPlus.get().permission;
	private Options options = StaffPlus.get().options;
	private ModeCoordinator modeCoordinator = StaffPlus.get().modeCoordinator;
	private AlertCoordinator alertCoordinator = StaffPlus.get().alertCoordinator;
	
	public BlockBreak()
	{
		Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlace(BlockBreakEvent event)
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
				getVein(block.getType(), block, new HashSet<Block>(), false);
				amount = alertCoordinator.getNotifiedAmount() - start;
				
				if(amount > 0)
				{
					alertCoordinator.onXray(player.getName(), amount, block.getType(), block.getLightLevel());
				}
			}
			
			return;
		}
		
		event.setCancelled(true);
	}
	
	private void getVein(Material referenceType, Block block, Set<Block> collected, boolean shouldCheck)
	{
		if(shouldCheck && (block.getType() != referenceType || collected.contains(block) || alertCoordinator.hasNotified(block.getLocation())))
		{
			return;
		}else alertCoordinator.addNotified(block.getLocation());
		
		for(BlockFace face : FACES)
		{
			getVein(referenceType, block.getRelative(face), collected, true);
		}
	}
	
	private final BlockFace[] FACES =
	{
		BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST,
		BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST,
		BlockFace.UP, BlockFace.DOWN
	};
}