package net.shortninja.staffplus.player.attribute.gui;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.data.config.Messages;
import net.shortninja.staffplus.data.config.Options;
import net.shortninja.staffplus.player.User;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.player.attribute.infraction.InfractionCoordinator;
import net.shortninja.staffplus.player.attribute.infraction.Warning;
import net.shortninja.staffplus.player.attribute.mode.handler.FreezeHandler;
import net.shortninja.staffplus.player.attribute.mode.handler.GadgetHandler;
import net.shortninja.staffplus.util.Message;
import net.shortninja.staffplus.util.lib.JavaUtils;
import net.shortninja.staffplus.util.lib.hex.Items;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ExamineGui extends AbstractGui
{
	private Message message = StaffPlus.get().message;
	private Options options = StaffPlus.get().options;
	private Messages messages = StaffPlus.get().messages;
	private UserManager userManager = StaffPlus.get().userManager;
	private FreezeHandler freezeHandler = StaffPlus.get().freezeHandler;
	private GadgetHandler gadgetHandler = StaffPlus.get().gadgetHandler;
	private InfractionCoordinator infractionCoordinator = StaffPlus.get().infractionCoordinator;
	private static final int SIZE = 54;
	
	public ExamineGui(Player player, Player targetPlayer, String title)
	{
		super(SIZE, title);
		
		setInventoryContents(targetPlayer);
		
		if(options.modeExamineFood >= 0)
		{
			setItem(options.modeExamineFood, foodItem(targetPlayer), null);
		}
		
		if(options.modeExamineIp >= 0)
		{
			setItem(options.modeExamineIp, ipItem(targetPlayer), null);
		}
		
		if(options.modeExamineGamemode >= 0)
		{
			setItem(options.modeExamineGamemode, gameModeItem(targetPlayer), null);
		}
		
		if(options.modeExamineInfractions >= 0)
		{
			setItem(options.modeExamineInfractions, infractionsItem(userManager.getUser(targetPlayer.getUniqueId())), null);
		}
		
		setInteractiveItems(targetPlayer);
		player.openInventory(getInventory());
		userManager.getUser(player.getUniqueId()).setCurrentGui(this);
	}
	
	private void setInventoryContents(Player targetPlayer)
	{
		ItemStack[] items = targetPlayer.getInventory().getContents();
		ItemStack[] armor = targetPlayer.getInventory().getArmorContents();
		
		JavaUtils.reverse(armor);
		
		for(int i = 0; i < items.length; i++)
		{
			setItem(i, items[i], null);
		}
		
		for(int i = 0; i <= armor.length - 1; i++)
		{
			if(i == 3)
			{
				setItem(39 + i, targetPlayer.getItemInHand(), null);
			}
			
			setItem(38 + i, armor[i], null);
		}
	}
	
	private void setInteractiveItems(final Player targetPlayer)
	{
		if(options.modeExamineLocation >= 0)
		{
			setItem(options.modeExamineLocation, locationItem(targetPlayer), new AbstractAction()
			{
				@Override
				public void click(Player player, ItemStack item, int slot)
				{
					if(targetPlayer != null)
					{
						player.teleport(targetPlayer);
					}else message.send(player, messages.playerOffline, messages.prefixGeneral);
				}
				
				@Override
				public boolean shouldClose()
				{
					return true;
				}
				
				@Override public void execute(Player player, String input) {}
			});
		}
		
		if(options.modeExamineNotes >= 0)
		{
			setItem(options.modeExamineNotes, notesItem(userManager.getUser(targetPlayer.getUniqueId())), new AbstractAction()
			{
				@Override
				public void click(Player player, ItemStack item, int slot)
				{
					User user = userManager.getUser(player.getUniqueId());
					
					message.send(player, messages.typeInput, messages.prefixGeneral);
					
					user.setQueuedAction(new AbstractAction()
					{
						@Override
						public void execute(Player player, String input)
						{
							userManager.getUser(targetPlayer.getUniqueId()).addPlayerNote("&7" + input);
							message.send(player, messages.inputAccepted, messages.prefixGeneral);
						}
						
						@Override
						public boolean shouldClose()
						{
							return true;
						}
						
						@Override public void click(Player player, ItemStack item, int slot) {}
					});
				}
				
				@Override
				public boolean shouldClose()
				{
					return true;
				}
				
				@Override public void execute(Player player, String input) {}
			});
		}
		
		if(options.modeExamineFreeze >= 0)
		{
			setItem(options.modeExamineFreeze, freezeItem(targetPlayer), new AbstractAction()
			{
				@Override
				public void click(Player player, ItemStack item, int slot)
				{
					if(targetPlayer != null)
					{
						gadgetHandler.onFreeze(player, targetPlayer);
					}else message.send(player, messages.playerOffline, messages.prefixGeneral);
				}
				
				@Override
				public boolean shouldClose()
				{
					return true;
				}
				
				@Override public void execute(Player player, String input) {}
			});
		}
		
		if(options.modeExamineWarn >= 0)
		{
			setItem(options.modeExamineWarn, warnItem(), new AbstractAction()
			{
				@Override
				public void click(Player player, ItemStack item, int slot)
				{
					User user = userManager.getUser(player.getUniqueId());
					
					message.send(player, messages.typeInput, messages.prefixGeneral);
					
					user.setQueuedAction(new AbstractAction()
					{
						@Override
						public void execute(Player player, String input)
						{
							UUID uuid = targetPlayer.getUniqueId();
							
							infractionCoordinator.sendWarning(player, new Warning(uuid, targetPlayer.getName(), input, player.getName(), player.getUniqueId(), System.currentTimeMillis()));
							message.send(player, messages.inputAccepted, messages.prefixGeneral);
						}
						
						@Override
						public boolean shouldClose()
						{
							return true;
						}
						
						@Override public void click(Player player, ItemStack item, int slot) {}
					});
				}
				
				@Override
				public boolean shouldClose()
				{
					return true;
				}
				
				@Override public void execute(Player player, String input) {}
			});
		}
	}
	
	private ItemStack foodItem(Player player)
	{
		int healthLevel = (int) player.getHealth();
		int foodLevel = player.getFoodLevel();
		
		ItemStack item = Items.builder()
				.setMaterial(Material.BREAD).setAmount(1)
				.setName("&bFood")
				.addLore("&7Health @ " + healthLevel + "/20", "&7Hunger @" + foodLevel + "/20")
				.build();
		
		return item;
	}
	
	private ItemStack ipItem(Player player)
	{
		String ip = player.getAddress().getAddress().getHostAddress().replace("/", "");
		
		ItemStack item = Items.builder()
				.setMaterial(Material.COMPASS).setAmount(1)
				.setName("&bIP address")
				.addLore("&7" + ip)
				.build();
		
		return item;
	}
	
	private ItemStack gameModeItem(Player player)
	{
		ItemStack item = Items.builder()
				.setMaterial(Material.GRASS).setAmount(1)
				.setName("&bGamemode")
				.addLore("&7" + player.getGameMode().toString())
				.build();
		
		return item;
	}
	
	private ItemStack infractionsItem(User user)
	{
		ItemStack item = Items.builder()
				.setMaterial(Material.BOOK).setAmount(1)
				.setName("&bInfractions")
				.addLore("&7Warnings: " + user.getWarnings().size(), "&7Reports: " + user.getReports().size())
				.build();
		
		return item;
	}
	
	private ItemStack locationItem(Player player)
	{
		Location location = player.getLocation();
		
		ItemStack item = Items.builder()
				.setMaterial(Material.MAP).setAmount(1)
				.setName("&bLocation")
				.addLore("&7" + location.getWorld().getName() + " &8» &7" + JavaUtils.serializeLocation(location))
				.build();
		
		return item;
	}
	
	private ItemStack notesItem(User user)
	{
		List<String> notes = user.getPlayerNotes().isEmpty() ? Arrays.asList("&7No notes found") : user.getPlayerNotes();
		
		ItemStack item = Items.builder()
				.setMaterial(Material.MAP).setAmount(1)
				.setName("&bPlayer notes")
				.setLore(notes)
				.build();
		
		return item;
	}
	
	private ItemStack freezeItem(Player player)
	{
		String frozenStatus = freezeHandler.isFrozen(player.getUniqueId()) ? "" : "not ";
		
		ItemStack item = Items.builder()
				.setMaterial(Material.BLAZE_ROD).setAmount(1)
				.setName("&bFreeze player")
				.setLore(Arrays.asList("&7Click to toggle freeze for this player.", "&7Currently " + frozenStatus + "frozen."))
				.build();
		
		return item;
	}
	
	private ItemStack warnItem()
	{
		ItemStack item = Items.builder()
				.setMaterial(Material.PAPER).setAmount(1)
				.setName("&bWarn player")
				.addLore("&7Click to warn this player.")
				.build();
		
		return item;
	}
}