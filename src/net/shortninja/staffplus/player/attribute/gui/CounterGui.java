package net.shortninja.staffplus.player.attribute.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.data.config.Messages;
import net.shortninja.staffplus.data.config.Options;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.player.attribute.mode.ModeCoordinator;
import net.shortninja.staffplus.util.Message;
import net.shortninja.staffplus.util.Permission;
import net.shortninja.staffplus.util.lib.JavaUtils;
import net.shortninja.staffplus.util.lib.hex.Items;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CounterGui extends AbstractGui
{
	private Permission permission = StaffPlus.get().permission;
	private Message message = StaffPlus.get().message;
	private Options options = StaffPlus.get().options;
	private Messages messages = StaffPlus.get().messages;
	private UserManager userManager = StaffPlus.get().userManager;
	private ModeCoordinator modeCoordinator = StaffPlus.get().modeCoordinator;
	private static final int SIZE = 54;
	
	public CounterGui(Player player, String title)
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
		
		List<Player> players = options.modeCounterShowStaffMode ? getModePlayers() : JavaUtils.getOnlinePlayers();
		int slot = 0; // Better to use this because not every iteration is going to have a result.
		
		for(Player p : players)
		{
			if(!permission.has(p, options.permissionMember))
			{
				continue;
			}else if((slot + 1) >= SIZE)
			{
				break;
			}
			
			setItem(slot, modePlayerItem(p), action);
			slot++;
		}
		
		player.openInventory(getInventory());
		userManager.getUser(player.getUniqueId()).setCurrentGui(this);
	}
	
	private List<Player> getModePlayers()
	{
		List<Player> modePlayers = new ArrayList<Player>();
		
		for(UUID uuid : modeCoordinator.getModeUsers())
		{
			Player player = Bukkit.getPlayer(uuid);
			
			if(player != null)
			{
				modePlayers.add(player);
			}
		}
		
		return modePlayers;
	}
	
	private ItemStack modePlayerItem(Player player)
	{
		Location location = player.getLocation();
		
		ItemStack item = Items.editor(Items.createSkull(player.getName()))
				.setName("&b" + player.getName())
				.addLore("&7" + location.getWorld().getName() + " &8» &7" + JavaUtils.serializeLocation(location))
				.build();
		
		return item;
	}
}