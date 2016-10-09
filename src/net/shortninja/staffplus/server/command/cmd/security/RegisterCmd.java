package net.shortninja.staffplus.server.command.cmd.security;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.attribute.SecurityHandler;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class RegisterCmd extends BukkitCommand
{
	private PermissionHandler permission = StaffPlus.get().permission;
	private MessageCoordinator message = StaffPlus.get().message;
	private Options options = StaffPlus.get().options;
	private Messages messages = StaffPlus.get().messages;
	private SecurityHandler securityHandler = StaffPlus.get().securityHandler;
	
	public RegisterCmd(String name)
	{
		super(name);
	}

	@Override
	public boolean execute(CommandSender sender, String alias, String[] args)
	{
		if(!permission.has(sender, options.permissionMember))
		{
			message.send(sender, messages.noPermission, messages.prefixGeneral);
			return true;
		}else if(!(sender instanceof Player))
		{
			message.send(sender, messages.onlyPlayers, messages.prefixGeneral);
			return true;
		}
		
		if(args.length >= 2)
		{
			String password = args[0];
			
			if(password.equals(args[1]))
			{
				securityHandler.setPassword(((Player) sender).getUniqueId(), password, true);
				message.send(sender, messages.loginRegistered, messages.prefixGeneral);
			}else message.send(sender, messages.invalidArguments.replace("%usage%", usageMessage), messages.prefixGeneral);
		}else message.send(sender, messages.invalidArguments.replace("%usage%", usageMessage), messages.prefixGeneral);
		
		return true;
	}
}