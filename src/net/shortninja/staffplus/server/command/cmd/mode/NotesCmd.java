package net.shortninja.staffplus.server.command.cmd.mode;

import java.util.List;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.User;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import net.shortninja.staffplus.util.lib.JavaUtils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class NotesCmd extends BukkitCommand
{
	private PermissionHandler permission = StaffPlus.get().permission;
	private MessageCoordinator message = StaffPlus.get().message;
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
			boolean hasPermission = permission.has(sender, options.permissionExamine);
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
		User user = userManager.get(player.getUniqueId());
		
		if(user != null)
		{
			List<String> notes = user.getPlayerNotes();
			
			for(String message : messages.noteListStart)
			{
				this.message.send(sender, message.replace("%longline%", this.message.LONG_LINE).replace("%target%", player.getName()).replace("%notes%", Integer.toString(notes.size())), message.contains("%longline%") ? "" : messages.prefixGeneral);
			}

			for(int i = 0; i < notes.size(); i++)
			{
				String note = notes.get(i);
				
				message.send(sender, messages.noteListEntry.replace("%count%", Integer.toString(i + 1)).replace("%note%", note), messages.prefixGeneral);
			}

			for(String message : messages.noteListEnd)
			{
				this.message.send(sender, message.replace("%longline%", this.message.LONG_LINE).replace("%target%", player.getName()).replace("%notes%", Integer.toString(notes.size())), message.contains("%longline%") ? "" : messages.prefixGeneral);
			}
		}else message.send(sender, messages.playerOffline, messages.prefixGeneral);
	}
	
	private void clearNotes(CommandSender sender, Player player)
	{
		User user = userManager.get(player.getUniqueId());
		
		if(user != null)
		{
			user.getPlayerNotes().clear();
			message.send(sender, messages.noteCleared.replace("%target%", player.getName()), messages.prefixGeneral);
		}else message.send(sender, messages.playerOffline, messages.prefixGeneral);
	}
	
	private void addNote(CommandSender sender, String option, String note)
	{
		Player player = Bukkit.getPlayer(option);
		
		if(player != null)
		{
			userManager.get(player.getUniqueId()).addPlayerNote(note);;
			message.send(sender, messages.noteAdded.replace("%target%", player.getName()), messages.prefixGeneral);
		}else message.send(sender, messages.playerOffline, messages.prefixGeneral);
	}
	
	private void sendHelp(CommandSender sender)
	{
		message.send(sender, "&7" + message.LONG_LINE, "");
		message.send(sender, "&b/" + getName() + " &7" + getUsage(), messages.prefixGeneral);
		message.send(sender, "&b/" + getName() + " get &7[player]", messages.prefixGeneral);
		message.send(sender, "&b/" + getName() + " clear &7[player]", messages.prefixGeneral);
		message.send(sender, "&7" + message.LONG_LINE, "");
	}
}