package net.shortninja.staffplus.command.cmd.mode;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.data.config.Messages;
import net.shortninja.staffplus.data.config.Options;
import net.shortninja.staffplus.util.Message;
import net.shortninja.staffplus.util.Permission;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class ModeCmd extends BukkitCommand
{
	private Permission permission = StaffPlus.get().permission;
	private Message message = StaffPlus.get().message;
	private Options options = StaffPlus.get().options;
	private Messages messages = StaffPlus.get().messages;
	
	public ModeCmd(String name)
	{
		super(name);
	}

	@Override
	public boolean execute(CommandSender sender, String alias, String[] args)
	{
		if(!permission.has(sender, options.permissionMode))
		{
			message.send(sender, messages.noPermission, messages.prefixGeneral);
			return true;
		}
		
		if(args.length >= 2 && permission.isOp(sender))
		{
			Player targetPlayer = Bukkit.getPlayer(args[0]);
			String option = args[1];
			
			if(targetPlayer != null)
			{
				if(option.equalsIgnoreCase("enable"))
				{
					StaffPlus.get().modeCoordinator.addMode(targetPlayer);
				}else if(option.equalsIgnoreCase("disable"))
				{
					StaffPlus.get().modeCoordinator.removeMode(targetPlayer);
				}else message.send(sender, messages.invalidArguments.replace("%usage%", getName() + " &7" + getUsage()), messages.prefixGeneral);
			}else message.send(sender, messages.playerOffline, messages.prefixGeneral);
		}else if(args.length == 1 && permission.isOp(sender))
		{
			Player targetPlayer = Bukkit.getPlayer(args[0]);
			
			if(targetPlayer != null)
			{
				toggleMode(targetPlayer);
			}else message.send(sender, messages.playerOffline, messages.prefixGeneral);
		}else if(sender instanceof Player)
		{
			toggleMode((Player) sender);
		}else message.send(sender, messages.onlyPlayers, messages.prefixGeneral);
		
		return true;
	}
	
	private void toggleMode(Player player)
	{
		if(StaffPlus.get().modeCoordinator.isInMode(player.getUniqueId()))
		{
			StaffPlus.get().modeCoordinator.removeMode(player);
		}else StaffPlus.get().modeCoordinator.addMode(player);
	}
}