package net.shortninja.staffplus.player.attribute.gui.hub;

import java.util.ArrayList;
import java.util.List;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.data.config.Messages;
import net.shortninja.staffplus.data.config.Options;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.player.attribute.gui.AbstractGui;
import net.shortninja.staffplus.player.attribute.infraction.InfractionCoordinator;
import net.shortninja.staffplus.player.attribute.infraction.Report;
import net.shortninja.staffplus.util.Message;
import net.shortninja.staffplus.util.lib.hex.Items;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ReportsGui extends AbstractGui
{
	private Message message = StaffPlus.get().message;
	private Options options = StaffPlus.get().options;
	private Messages messages = StaffPlus.get().messages;
	private UserManager userManager = StaffPlus.get().userManager;
	private InfractionCoordinator infractionCoordinator = StaffPlus.get().infractionCoordinator;
	private static final int SIZE = 54;
	
	public ReportsGui(Player player, String title)
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
					infractionCoordinator.removeUnresolvedReport(p.getUniqueId());
				}else message.send(player, messages.playerOffline, messages.prefixGeneral);
			}
			
			@Override
			public boolean shouldClose()
			{
				return true;
			}
			
			@Override public void execute(Player player, String input) {}
		};
		
		int count = 0; // Using this with an enhanced for loop because it is much faster than converting to an array.
		
		for(Report report : infractionCoordinator.getUnresolvedReports())
		{
			if((count + 1) >= SIZE)
			{
				break;
			}
			
			setItem(count, reportItem(report), action);
			count++;
		}
		
		player.openInventory(getInventory());
		userManager.getUser(player.getUniqueId()).setCurrentGui(this);
	}
	
	private ItemStack reportItem(Report report)
	{
		List<String> lore = new ArrayList<String>();
		
		lore.add("&bReason: " + report.getReason());
		
		if(options.reportsShowReporter)
		{
			lore.add("&bReporter: " + report.getReporterName());
		}
		
		ItemStack item = Items.editor(Items.createSkull(report.getName())).setAmount(1)
				.setName("&b" + report.getName())
				.setLore(lore)
				.build();
		
		return item;
	}
}