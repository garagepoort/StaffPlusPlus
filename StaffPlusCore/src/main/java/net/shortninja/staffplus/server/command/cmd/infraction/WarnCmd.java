package net.shortninja.staffplus.server.command.cmd.infraction;

import java.util.List;
import java.util.UUID;

import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.User;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.player.attribute.infraction.Warning;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.util.lib.JavaUtils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class WarnCmd extends BukkitCommand
{
	private PermissionHandler permission = StaffPlus.get().permission;
	private MessageCoordinator message = StaffPlus.get().message;
	private Options options = StaffPlus.get().options;
	private Messages messages = StaffPlus.get().messages;
	private UserManager userManager = StaffPlus.get().userManager;
	
	public WarnCmd(String name)
	{
		super(name);
	}

	@Override
	public boolean execute(CommandSender sender, String alias, String[] args)
	{
		if(!permission.has(sender, options.permissionWarn))
		{
			message.send(sender, messages.noPermission, messages.prefixWarnings);
			return true;
		}
		
		if(args.length == 2)
		{
			String argument = args[0];
			String option = args[1];
			Player player = Bukkit.getPlayer(option);
			
			if(argument.equalsIgnoreCase("get"))
			{
				if(player == null)
				{
					message.send(sender, messages.playerOffline, messages.prefixWarnings);
				}else listWarnings(sender, player);
			}else if(argument.equalsIgnoreCase("clear"))
			{
				if(player == null)
				{
					message.send(sender, messages.playerOffline, messages.prefixWarnings);
				}else clearWarnings(sender, player);
			}else sendWarning(sender, argument, JavaUtils.compileWords(args, 1));
		}else if(args.length >= 3)
		{
			sendWarning(sender, args[0], JavaUtils.compileWords(args, 1));
		}else if(!permission.has(sender, options.permissionWarn))
		{
			message.send(sender, messages.invalidArguments.replace("%usage%", usageMessage), messages.prefixWarnings);
		}else sendHelp(sender);
		
		return true;
	}
	
	private void listWarnings(CommandSender sender, Player player)
	{
		User user = userManager.get(player.getUniqueId());
		
		if(user != null)
		{
			List<Warning> warnings = user.getWarnings();
			
			for(String message : messages.warningsListStart)
			{
				this.message.send(sender, message.replace("%longline%", this.message.LONG_LINE).replace("%target%", player.getName()).replace("%warnings%", Integer.toString(warnings.size())), message.contains("%longline%") ? "" : messages.prefixWarnings);
			}

			for(int i = 0; i < warnings.size(); i++)
			{
				Warning warning = warnings.get(i);
				
				message.send(sender, messages.warningsListEntry.replace("%count%", Integer.toString(i + 1)).replace("%reason%", warning.getReason()).replace("%issuer%", warning.getIssuerName()), messages.prefixWarnings);
			}

			for(String message : messages.warningsListEnd)
			{
				this.message.send(sender, message.replace("%longline%", this.message.LONG_LINE).replace("%target%", player.getName()).replace("%warnings%", Integer.toString(user.getReports().size())), message.contains("%longline%") ? "" : messages.prefixWarnings);
			}
		}else message.send(sender, messages.playerOffline, messages.prefixWarnings);
	}
	
	private void clearWarnings(CommandSender sender, Player player)
	{
		User user = userManager.get(player.getUniqueId());
		
		if(user != null)
		{
			user.getWarnings().clear();
			message.send(sender, messages.warningsCleared.replace("%target%", player.getName()), messages.prefixWarnings);
		}else message.send(sender, messages.playerOffline, messages.prefixWarnings);
	}
	
	private void sendWarning(CommandSender sender, String option, String reason)
	{
		String issuerName = "Console"; // The name "Console" is taken, but it is capitalized as "CONSOLE".
		UUID issuerUuid = null;
		Player warned = Bukkit.getPlayer(option);
		
		if(warned != null)
		{
			Player issuer = null;
			
			if(sender instanceof Player)
			{
				issuer = (Player) sender;
				issuerName = issuer.getName();
				issuerUuid = issuer.getUniqueId();
			}
			
			Warning warning = new Warning(warned.getUniqueId(), warned.getName(), reason, issuerName, issuerUuid, System.currentTimeMillis());
			
			StaffPlus.get().infractionCoordinator.sendWarning(sender, warning);
		}else message.send(sender, messages.playerOffline, messages.prefixGeneral);
	}
	
	private void sendHelp(CommandSender sender)
	{
		message.send(sender, "&7" + message.LONG_LINE, "");
		message.send(sender, "&b/" + getName() + " &7[player] [reason]", messages.prefixReports);
		message.send(sender, "&b/" + getName() + " get &7[player]", messages.prefixReports);
		message.send(sender, "&b/" + getName() + " clear &7[player]", messages.prefixReports);
		message.send(sender, "&7" + message.LONG_LINE, "");
	}
}