package net.shortninja.staffplus.listener;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.User;
import net.shortninja.staffplus.player.UserManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryClose implements Listener
{
	private UserManager userManager = StaffPlus.get().userManager;
	
	public InventoryClose()
	{
		Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onClose(InventoryCloseEvent event)
	{
		Player player = (Player) event.getPlayer();
		User user = userManager.getUser(player.getUniqueId());
		
		if(user.getCurrentGui() != null)
		{
			user.setCurrentGui(null);
		}
	}
}