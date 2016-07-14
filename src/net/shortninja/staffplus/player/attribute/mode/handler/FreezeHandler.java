package net.shortninja.staffplus.player.attribute.mode.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.data.config.Messages;
import net.shortninja.staffplus.data.config.Options;
import net.shortninja.staffplus.player.User;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.player.attribute.gui.FreezeGui;
import net.shortninja.staffplus.util.Message;
import net.shortninja.staffplus.util.Permission;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FreezeHandler
{
	private Permission permission = StaffPlus.get().permission;
	private Message message = StaffPlus.get().message;
	private Options options = StaffPlus.get().options;
	private Messages messages = StaffPlus.get().messages;
	private UserManager userManager = StaffPlus.get().userManager;
	private static Map<UUID, Location> lastFrozenLocations = new HashMap<UUID, Location>();	
	
	public boolean isFrozen(UUID uuid)
	{
		User user = userManager.getUser(uuid);
		
		return lastFrozenLocations.containsKey(uuid) || user.isFrozen();
	}
	
	public void addFreeze(Player player)
	{
		UUID uuid = player.getUniqueId();
		
		if(permission.has(player, options.permissionFreezeBypass))
		{
			return;
		}
		
		userManager.getUser(player.getUniqueId()).setFrozen(true);
		lastFrozenLocations.put(uuid, player.getLocation());
		player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 128));
		player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 128));
		
		if(options.modeFreezePrompt)
		{
			new FreezeGui(player, options.modeFreezePromptTitle);
			player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 128));
		}else message.sendCollectedMessage(player, messages.freeze, messages.prefixGeneral);
	}
	
	public void removeFreeze(Player player)
	{
		UUID uuid = player.getUniqueId();
		User user = userManager.getUser(uuid);
		
		user.setFrozen(false);
		lastFrozenLocations.remove(uuid);
		message.send(player, messages.unfrozen, messages.prefixGeneral);
		player.removePotionEffect(PotionEffectType.JUMP);
		player.removePotionEffect(PotionEffectType.SLOW);
		
		if(options.modeFreezePrompt && user.getCurrentGui() != null)
		{
			if(user.getCurrentGui() instanceof FreezeGui)
			{
				player.closeInventory();
			}
			
			player.removePotionEffect(PotionEffectType.BLINDNESS);
		}
	}
	
	public void checkLocations()
	{
		for(UUID uuid : lastFrozenLocations.keySet())
		{
			Player player = Bukkit.getPlayer(uuid);
			
			if(player != null)
			{
				Location playerLocation = player.getLocation();
				Location lastLocation = lastFrozenLocations.get(uuid).setDirection(playerLocation.getDirection());
				
				if(compareLocations(playerLocation, lastLocation))
				{
					continue;
				}
				
				player.teleport(lastLocation);
			}
		}
	}
	
	/*
	 * Only making this method because Location#equals checks if direction is the
	 * same, which I really don't care for.
	 */
	private boolean compareLocations(Location previous, Location current)
	{
		return previous.getBlockX() == current.getBlockX() && previous.getBlockY() == current.getBlockY() && previous.getBlockZ() == current.getBlockZ();
	}
}