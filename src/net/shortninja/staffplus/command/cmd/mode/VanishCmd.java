package net.shortninja.staffplus.command.cmd.mode;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.data.config.Messages;
import net.shortninja.staffplus.data.config.Options;
import net.shortninja.staffplus.player.User;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.player.attribute.mode.handler.VanishHandler;
import net.shortninja.staffplus.player.attribute.mode.handler.VanishHandler.VanishType;
import net.shortninja.staffplus.util.Message;
import net.shortninja.staffplus.util.Permission;
import net.shortninja.staffplus.util.lib.JavaUtils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class VanishCmd extends BukkitCommand
{
	private Permission permission = StaffPlus.get().permission;
	private Message message = StaffPlus.get().message;
	private Options options = StaffPlus.get().options;
	private Messages messages = StaffPlus.get().messages;
	private UserManager userManager = StaffPlus.get().userManager;
	private VanishHandler vanishHandler = StaffPlus.get().vanishHandler;
	
	public VanishCmd(String name)
	{
		super(name);
	}

	@Override
	public boolean execute(CommandSender sender, String alias, String[] args)
	{
		if(args.length >= 3 && permission.isOp(sender))
		{
			Player targetPlayer = Bukkit.getPlayer(args[1]);
			String option = args[2];

			if(targetPlayer != null)
			{
				if(option.equalsIgnoreCase("enable"))
				{
					handleVanishArgument(sender, args[0], targetPlayer, false);
				}else vanishHandler.removeVanish(targetPlayer);
			}else message.send(sender, messages.playerOffline, messages.prefixGeneral);
		}else if(args.length == 2 && permission.isOp(sender))
		{
			Player targetPlayer = Bukkit.getPlayer(args[1]);
			
			if(targetPlayer != null)
			{
				handleVanishArgument(sender, args[0], targetPlayer, false);
			}else message.send(sender, messages.playerOffline, messages.prefixGeneral);
		}else if(args.length == 1 && permission.isOp(sender))
		{
			if((sender instanceof Player))
			{
				handleVanishArgument(sender, args[0], (Player) sender, true);
			}else message.send(sender, messages.onlyPlayers, messages.prefixGeneral);
		}else sendHelp(sender);
		
		return true;
	}
	
	private void handleVanishArgument(CommandSender sender, String argument, Player player, boolean shouldCheckPermission)
	{
		boolean isValid = JavaUtils.isValidEnum(VanishType.class, argument.toUpperCase());
		VanishType vanishType = VanishType.NONE;
		User user = userManager.getUser(player.getUniqueId());
		
		if(!isValid)
		{
			sendHelp(sender);
			return;
		}else vanishType = VanishType.valueOf(argument.toUpperCase());
		
		switch(vanishType)
		{
			case TOTAL:
				if(permission.has(player, options.permissionVanishTotal) || !shouldCheckPermission)
				{
					if(user.getVanishType() != VanishType.TOTAL)
					{
						vanishHandler.addVanish(player, vanishType);
					}else vanishHandler.removeVanish(player);
				}else message.send(player, messages.noPermission, messages.prefixGeneral);
				break;
			case LIST:
				if(permission.has(player, options.permissionVanishList) || !shouldCheckPermission)
				{
					if(user.getVanishType() != VanishType.LIST)
					{
						vanishHandler.addVanish(player, vanishType);
					}else vanishHandler.removeVanish(player);
				}else message.send(player, messages.noPermission, messages.prefixGeneral);
				break;
			case NONE:
				if(permission.has(player, options.permissionVanishList) || permission.has(player, options.permissionVanishTotal) || !shouldCheckPermission)
				{
					vanishHandler.removeVanish(player);
				}else message.send(player, messages.noPermission, messages.prefixGeneral);
				break;
		}
	}
	
	private void sendHelp(CommandSender sender)
	{
		message.send(sender, message.LONG_LINE, "");
		message.send(sender, "&b/" + getName() + " &7" + getUsage(), messages.prefixGeneral);
		message.send(sender, message.LONG_LINE, "");
	}
}