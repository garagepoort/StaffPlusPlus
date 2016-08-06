package net.shortninja.staffplus.player.attribute.gui.hub;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.data.config.Options;
import net.shortninja.staffplus.player.User;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.player.attribute.gui.AbstractGui;
import net.shortninja.staffplus.util.lib.hex.Items;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HubGui extends AbstractGui
{
	private Options options = StaffPlus.get().options;
	private UserManager userManager = StaffPlus.get().userManager;
	private static final int SIZE = 27;
	
	public HubGui(Player player, String title)
	{
		super(SIZE, title);
		
		User user = userManager.get(player.getUniqueId());
		
		if(options.modeGuiReports)
		{
			setItem(options.modeGuiMiner ? 12 : 13, reportsItem(), new AbstractAction()
			{
				@Override
				public void click(Player player, ItemStack item, int slot)
				{
					new ReportsGui(player, options.modeGuiReportsTitle);
				}
				
				@Override
				public boolean shouldClose()
				{
					return false;
				}

				@Override public void execute(Player player, String input) {}
			});
		}
		
		if(options.modeGuiMiner)
		{
			setItem(options.modeGuiReports ? 14 : 13, minerItem(), new AbstractAction()
			{
				@Override
				public void click(Player player, ItemStack item, int slot)
				{
					new MinerGui(player, options.modeGuiMinerTitle);
				}
				
				@Override
				public boolean shouldClose()
				{
					return false;
				}

				@Override public void execute(Player player, String input) {}
			});
		}
		
		setGlass(user);
		player.openInventory(getInventory());
		user.setCurrentGui(this);
	}
	
	private ItemStack reportsItem()
	{
		ItemStack item = Items.builder()
				.setMaterial(Material.PAPER).setAmount(1)
				.setName("&bUnresolved reports GUI")
				.addLore("&7Shows all reports that haven't been resolved.")
				.build();
		
		return item;
	}
	
	private ItemStack minerItem()
	{
		ItemStack item = Items.builder()
				.setMaterial(Material.STONE_PICKAXE).setAmount(1)
				.setName("&bMiner GUI")
				.addLore("&7Shows all players under the set Y value.")
				.build();
		
		return item;
	}
}