package net.shortninja.staffplus.command.cmd;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.data.config.Messages;
import net.shortninja.staffplus.data.config.Options;
import net.shortninja.staffplus.player.User;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.player.attribute.mode.handler.VanishHandler.VanishType;
import net.shortninja.staffplus.util.Message;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class ListCmd extends BukkitCommand
{
	private Message message = StaffPlus.get().message;
	private Options options = StaffPlus.get().options;
	private Messages messages = StaffPlus.get().messages;
	private UserManager userManager = StaffPlus.get().userManager;
	
	public ListCmd(String name)
	{
		super(name);
	}

	@Override
	public boolean execute(CommandSender sender, String alias, String[] args)
	{
		String status = "all";
		
		if(args.length == 1)
		{
			status = args[0];
		}
		
		for(String message : messages.staffListStart)
		{
			this.message.send(sender, message.replace("%longline%", this.message.LONG_LINE), message.contains("%longline%") ? "" : messages.prefixGeneral);
		}
		
		for(Player player : Bukkit.getOnlinePlayers())
		{
			User user = userManager.getUser(player.getUniqueId());
			
			if(user == null)
			{
				System.out.println("User is null");
				continue;
			}
			
			if(hasStatus(user, status))
			{
				message.send(sender, messages.staffListMember.replace("%player%", player.getName()).replace("%statuscolor%", getStatusColor(user)), messages.prefixGeneral);
			}
		}
		
		for(String message : messages.staffListEnd)
		{
			this.message.send(sender, message.replace("%longline%", this.message.LONG_LINE), message.contains("%longline%") ? "" : messages.prefixGeneral);
		}
		
		return true;
	}
	
	private boolean hasStatus(User user, String status)
	{
		boolean hasStatus = true;
		
		switch(status.toLowerCase())
		{
			case "online":
				hasStatus = user.getVanishType() == VanishType.NONE && user.isOnline();
				break;
			case "offline":
				hasStatus = user.getVanishType() != VanishType.NONE && !user.isOnline();
				break;
			case "away":
				hasStatus = user.getVanishType() != VanishType.LIST && user.isOnline() && options.vanishShowAway;
				break;
		}
		
		return hasStatus;
	}
	
	private String getStatusColor(User user)
	{
		String statusColor = "4";
		
		if(hasStatus(user, "online"))
		{
			statusColor = "a";
		}else if(hasStatus(user, "away"))
		{
			statusColor = "e";
		}
		
		return statusColor;
	}
}