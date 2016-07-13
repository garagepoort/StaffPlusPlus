package net.shortninja.staffplus.listener;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.User;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.player.attribute.gui.AbstractGui.AbstractAction;
import net.shortninja.staffplus.server.compatibility.IProtocol;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryClick implements Listener
{
	private IProtocol versionProtocol = StaffPlus.get().versionProtocol;
	private UserManager userManager = StaffPlus.get().userManager;
	
	public InventoryClick()
	{
		Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onClick(InventoryClickEvent event)
	{
		Player player = (Player) event.getWhoClicked();
		User user = userManager.getUser(player.getUniqueId());
		ItemStack item = event.getCurrentItem();
		int slot = event.getSlot();
		
		System.out.println(versionProtocol.getNbtString(item));
		
		if(user.getCurrentGui() == null || item == null)
		{
			return;
		}
		
		AbstractAction action = user.getCurrentGui().getAction(slot);
		
		if(action != null)
		{
			action.click(player, item, slot);
			
			if(action.shouldClose())
			{
				player.closeInventory();
			}
		} 
		
		event.setCancelled(true);
	}
}