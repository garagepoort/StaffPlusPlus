package net.shortninja.staffplus.player.attribute.gui.hub;

import java.util.List;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.data.config.Messages;
import net.shortninja.staffplus.data.config.Options;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.player.attribute.gui.AbstractGui;
import net.shortninja.staffplus.util.Message;
import net.shortninja.staffplus.util.lib.JavaUtils;
import net.shortninja.staffplus.util.lib.hex.Items;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MinerGui extends AbstractGui
{
	private Message message = StaffPlus.get().message;
	private Options options = StaffPlus.get().options;
	private Messages messages = StaffPlus.get().messages;
	private UserManager userManager = StaffPlus.get().userManager;
	private static final int SIZE = 54;
	
	public MinerGui(Player player, String title)
	{
		super(SIZE, title);
		
		AbstractAction action = new AbstractAction()
		{
			@Override
			public void click(Player player, ItemStack item, int slot)
			{
				Player p = Bukkit.getPlayer(item.getItemMeta().getDisplayName().substring(2));
				
				if(p != null)
				{
					player.teleport(p);
				}else message.send(player, messages.playerOffline, messages.prefixGeneral);
			}
			
			@Override
			public boolean shouldClose()
			{
				return true;
			}

			@Override public void execute(Player player, String input) {}
		};
		
		List<Player> onlinePlayers = JavaUtils.getOnlinePlayers();
		
		for(int i = 0; i < onlinePlayers.size(); i++)
		{
			Player p = onlinePlayers.get(i);
			
			if(p.getLocation().getBlockY() > options.modeGuiMinerLevel || (i + 1) >= SIZE)
			{
				continue;
			}
			
			setItem(i, minerItem(p), action);
		}
		
		player.openInventory(getInventory());
		userManager.getUser(player.getUniqueId()).setCurrentGui(this);
	}
	
	private ItemStack minerItem(Player player)
	{
		Location location = player.getLocation();
		
		ItemStack item = Items.editor(Items.createSkull(player.getName())).setAmount(1)
				.setName("&b" + player.getName())
				.addLore("&7" + location.getWorld().getName() + " &8» &7" + JavaUtils.serializeLocation(location))
				.build();
		
		return item;
	}
}