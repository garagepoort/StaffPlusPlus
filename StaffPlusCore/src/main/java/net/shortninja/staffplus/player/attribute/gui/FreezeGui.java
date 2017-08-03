package net.shortninja.staffplus.player.attribute.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.util.lib.hex.Items;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.server.data.config.Messages;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FreezeGui extends AbstractGui
{
	private Messages messages = StaffPlus.get().messages;
	private UserManager userManager = StaffPlus.get().userManager;
	private static final int SIZE = 9;
	
	public FreezeGui(Player player, String title)
	{
		super(SIZE, title);
		
		setItem(4, freezeItem(), null);
		player.openInventory(getInventory());
		userManager.get(player.getUniqueId()).setCurrentGui(this);
	}
	
	private ItemStack freezeItem()
	{
		List<String> freezeMessage = new ArrayList<String>(messages.freeze);
		String name = getTitle();
		List<String> lore = Arrays.asList("&7You are currently frozen!");
		
		if(freezeMessage.size() >= 1)
		{
			name = freezeMessage.get(0);
			freezeMessage.remove(0);
			lore = freezeMessage;
		}
		
		ItemStack item = Items.builder()
				.setMaterial(Material.PAPER).setAmount(1)
				.setName(name)
				.setLore(lore)
				.build();
		
		return item;
	}
}