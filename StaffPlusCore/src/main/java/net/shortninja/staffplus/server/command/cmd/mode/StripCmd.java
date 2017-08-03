package net.shortninja.staffplus.server.command.cmd.mode;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import net.shortninja.staffplus.util.lib.JavaUtils;
import net.shortninja.staffplus.server.data.config.Messages;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class StripCmd extends BukkitCommand
{
	private PermissionHandler permission = StaffPlus.get().permission;
	private MessageCoordinator message = StaffPlus.get().message;
	private Options options = StaffPlus.get().options;
	private Messages messages = StaffPlus.get().messages;
	
	public StripCmd(String name)
	{
		super(name);
	}

	@Override
	public boolean execute(CommandSender sender, String alias, String[] args)
	{
		Player targetPlayer = null;
		
		if(!permission.has(sender, options.permissionStrip))
		{
			message.send(sender, messages.noPermission, messages.prefixGeneral);
			return true;
		}
		
		if(args.length == 1)
		{
			targetPlayer = Bukkit.getPlayer(args[0]);
		}else if(!(sender instanceof Player))
		{
			message.send(sender, messages.onlyPlayers, messages.prefixGeneral);
			return true;
		}else targetPlayer = JavaUtils.getTargetPlayer((Player) sender);
		
		if(targetPlayer != null)
		{
			if(JavaUtils.hasInventorySpace(targetPlayer))
			{
				strip(targetPlayer, 0);
				message.send(sender, messages.strip.replace("%player%", targetPlayer.getName()), messages.prefixGeneral);
			}else message.send(sender, messages.invalidArguments.replace("%usage%", usageMessage), messages.prefixGeneral);
		}else message.send(sender, messages.playerOffline, messages.prefixGeneral);
		
		return true;
	}
	
	private void strip(Player player, int index)
	{
		ItemStack[] armor = player.getInventory().getArmorContents();
		
		switch(index)
		{
			case 0:
				if(armor[0] != null)
				{
					player.getInventory().addItem(armor[0].clone());
					player.getInventory().setBoots(null);
				}
				break;
			case 1:
				if(armor[1] != null)
				{
					player.getInventory().addItem(armor[1].clone());
					player.getInventory().setLeggings(null);
				}
				break;
			case 2:
				if(armor[2] != null)
				{
					player.getInventory().addItem(armor[2].clone());
					player.getInventory().setChestplate(null);
				}
				break;
			case 3:
				if(armor[3] != null)
				{
					player.getInventory().addItem(armor[3].clone());
					player.getInventory().setHelmet(null);
				}
				break;
			default:
				return;
		}
		
		if(JavaUtils.hasInventorySpace(player) && index < 3)
		{
			strip(player, index + 1);
		}
	}
}