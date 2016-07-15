package net.shortninja.staffplus.command.cmd.mode;

import java.util.List;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.data.config.Messages;
import net.shortninja.staffplus.data.config.Options;
import net.shortninja.staffplus.player.User;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.util.Message;
import net.shortninja.staffplus.util.Permission;
import net.shortninja.staffplus.util.lib.JavaUtils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class NotesCmd extends BukkitCommand
{
	private Permission permission = StaffPlus.get().permission;
	private Message message = StaffPlus.get().message;
	private Options options = StaffPlus.get().options;
	private Messages messages = StaffPlus.get().messages;
	private UserManager userManager = StaffPlus.get().userManager;
	
	public NotesCmd(String name)
	{
		super(name);
	}

	@Override
	public boolean execute(CommandSender sender, String alias, String[] args)
	{
		if(!permission.has(sender, options.permissionExamine))
		{
			message.send(sender, messages.noPermission, messages.prefixGeneral);
			return true;
		}
		
		if(args.length == 2)
		{
			String argument = args[0];
			String option = args[1];
			boolean hasPermission = permission.has(sender, options.permissionReport);
			Player player = Bukkit.getPlayer(option);
			
			if(argument.equalsIgnoreCase("get") && hasPermission)
			{
				if(player == null)
				{
					message.send(sender, messages.playerOffline, messages.prefixGeneral);
				}else listNotes(sender, player);
			}else if(argument.equalsIgnoreCase("clear") && hasPermission)
			{
				if(player == null)
				{
					message.send(sender, messages.playerOffline, messages.prefixGeneral);
				}else clearNotes(sender, player);
			}else addNote(sender, argument, JavaUtils.compileWords(args, 1));
		}else if(args.length >= 3)
		{
			addNote(sender, args[0], JavaUtils.compileWords(args, 1));
		}else sendHelp(sender);
		
		return true;
	}
	
	private void listNotes(CommandSender sender, Player player)
	{
		User user = userManager.getUser(player.getUniqueId());
		
		if(user != null)
		{
			List<String> notes = user.getPlayerNotes();
			
			message.send(sender, "&7" + message.LONG_LINE, "");
			message.send(sender, "&7" + player.getName() + " &bhas &7" + notes.size() + " &bnotes.", messages.prefixGeneral);
			
			for(String note : notes)
			{
				message.send(sender, "&7" + note, messages.prefixGeneral);
			}
			
			message.send(sender, "&7" + message.LONG_LINE, "");
		}else message.send(sender, messages.playerOffline, messages.prefixGeneral);
	}
	
	private void clearNotes(CommandSender sender, Player player)
	{
		User user = userManager.getUser(player.getUniqueId());
		
		if(user != null)
		{
			user.getPlayerNotes().clear();
			message.send(sender, messages.inputAccepted, messages.prefixGeneral);
		}else message.send(sender, messages.playerOffline, messages.prefixGeneral);
	}
	
	private void addNote(CommandSender sender, String option, String note)
	{
		Player player = Bukkit.getPlayer(option);
		
		if(player != null)
		{
			userManager.getUser(player.getUniqueId()).addPlayerNote(note);;
			message.send(sender, messages.inputAccepted, messages.prefixGeneral);
		}else message.send(sender, messages.playerOffline, messages.prefixGeneral);
	}
	
	private void sendHelp(CommandSender sender)
	{
		message.send(sender, message.LONG_LINE, "");
		message.send(sender, "&b/" + getName() + " &7" + getUsage(), messages.prefixGeneral);
		message.send(sender, "&b/" + getName() + " get &7[player]", messages.prefixGeneral);
		message.send(sender, "&b/" + getName() + " clear &7[player]", messages.prefixGeneral);
		message.send(sender, message.LONG_LINE, "");
	}
}