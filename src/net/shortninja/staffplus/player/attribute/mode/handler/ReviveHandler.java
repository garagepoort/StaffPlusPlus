package net.shortninja.staffplus.player.attribute.mode.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.data.config.Messages;
import net.shortninja.staffplus.player.attribute.mode.ModeDataVault;
import net.shortninja.staffplus.util.Message;
import net.shortninja.staffplus.util.lib.JavaUtils;

import org.bukkit.entity.Player;

public class ReviveHandler
{
	private Message message = StaffPlus.get().message;
	private Messages messages = StaffPlus.get().messages;
	private static Map<UUID, ModeDataVault> savedInventories = new HashMap<UUID, ModeDataVault>();
	
	public boolean hasSavedInventory(UUID uuid)
	{
		return savedInventories.containsKey(uuid);
	}
	
	public void cacheInventory(Player player)
	{
		UUID uuid = player.getUniqueId();
		ModeDataVault modeDataVault = new ModeDataVault(uuid, player.getInventory().getContents(), player.getInventory().getArmorContents());
	
		savedInventories.put(uuid, modeDataVault);
	}
	
	public void restoreInventory(Player player)
	{
		UUID uuid = player.getUniqueId();
		ModeDataVault modeDataVault = savedInventories.get(uuid);
		
		JavaUtils.clearInventory(player);
		player.getInventory().setContents(modeDataVault.getItems());
		player.getInventory().setArmorContents(modeDataVault.getArmor());
		message.send(player, messages.revivedUser, messages.prefixGeneral);
	}
}