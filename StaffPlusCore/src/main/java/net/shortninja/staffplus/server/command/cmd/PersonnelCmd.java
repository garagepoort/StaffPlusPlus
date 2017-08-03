package net.shortninja.staffplus.server.command.cmd;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.User;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.player.attribute.mode.handler.VanishHandler.VanishType;
import net.shortninja.staffplus.server.data.config.Messages;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class PersonnelCmd extends BukkitCommand
{
	private PermissionHandler permission = StaffPlus.get().permission;
	private MessageCoordinator message = StaffPlus.get().message;
	private Options options = StaffPlus.get().options;
	private Messages messages = StaffPlus.get().messages;
	private UserManager userManager = StaffPlus.get().userManager;
	
	public PersonnelCmd(String name)
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
			User user = userManager.get(player.getUniqueId());
			
			if(user == null)
			{
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
		VanishType vanishType = user.getVanishType();
		
		if(!permission.has(user.getPlayer(), options.permissionMember))
		{
			hasStatus = false;
			return hasStatus;
		}
		
		switch(status.toLowerCase())
		{
			case "online":
				hasStatus = vanishType == VanishType.NONE && user.isOnline();
				break;
			case "offline":
				hasStatus = (vanishType == VanishType.TOTAL || !user.isOnline()) || (vanishType == VanishType.LIST && !options.vanishShowAway);
				break;
			case "away":
				hasStatus = (vanishType == VanishType.NONE && user.isOnline()) || (vanishType == VanishType.LIST && options.vanishShowAway);
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