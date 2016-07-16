package net.shortninja.staffplus.server;

import java.util.HashSet;
import java.util.Set;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.data.config.Messages;
import net.shortninja.staffplus.data.config.Options;
import net.shortninja.staffplus.player.User;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.player.attribute.infraction.Report;
import net.shortninja.staffplus.player.attribute.infraction.Warning;
import net.shortninja.staffplus.util.Message;
import net.shortninja.staffplus.util.Permission;
import net.shortninja.staffplus.util.lib.JavaUtils;

import org.bukkit.Location;
import org.bukkit.Material;

public class AlertCoordinator
{
	private Permission permission = StaffPlus.get().permission;
	private Message message = StaffPlus.get().message;
	private Options options = StaffPlus.get().options;
	private Messages messages = StaffPlus.get().messages;
	private UserManager userManager = StaffPlus.get().userManager;
	private static Set<Location> notifiedLocations = new HashSet<Location>();
	
	public boolean hasNotified(Location location)
	{
		return notifiedLocations.contains(location);
	}
	
	public int getNotifiedAmount()
	{
		return notifiedLocations.size();
	}
	
	public void addNotified(Location location)
	{
		notifiedLocations.add(location);
	}
	
	public void clearNotified()
	{
		notifiedLocations.clear();
	}
	
	public void onNameChange(String originalName, String newName)
	{
		if(!options.alertsNameNotify)
		{
			return;
		}
		
		for(User user : userManager.getUsers())
		{
			if(user.shouldNotify(AlertType.NAME_CHANGE) && permission.has(user.getPlayer(), options.permissionNameChange))
			{
				message.send(user.getPlayer(), messages.alertsName.replace("%old%", originalName).replace("%new%", newName), messages.prefixGeneral, options.permissionNameChange);
			}
		}
		
		fixInfractionNames(originalName, newName);
	}
	
	public void onMention(User user, String mentioner)
	{
		if(!options.alertsMentionNotify || user == null)
		{
			return;
		}
		
		if(user.shouldNotify(AlertType.MENTION) && permission.has(user.getPlayer(), options.permissionMention))
		{
			message.send(user.getPlayer(), messages.alertsMention.replace("%target%", mentioner), messages.prefixGeneral, options.permissionMention);
			options.alertsSound.play(user.getPlayer());
		}
	}
	
	public void onXray(String miner, int amount, Material type, int lightLevel)
	{
		if(!options.alertsMentionNotify)
		{
			return;
		}
		
		for(User user : userManager.getUsers())
		{
			if(user.shouldNotify(AlertType.XRAY) && permission.has(user.getPlayer(), options.permissionXray))
			{
				message.send(user.getPlayer(), messages.alertsXray.replace("%target%", miner).replace("%count%", Integer.toString(amount)).replace("%itemtype%", JavaUtils.formatTypeName(type)).replace("%lightlevel%", Integer.toString(lightLevel)), messages.prefixGeneral, options.permissionXray);
			}
		}
	}
	
	/**
	 * This fixes the issue with reports and warnings having incorrect names after
	 * a player changes his or her name. Any unhandled fixes will be done on reload.
	 */
	private void fixInfractionNames(String originalName, String newName)
	{
		for(User user : userManager.getUsers())
		{
			for(Report report : user.getReports())
			{
				if(report.getReporterName().equals(originalName))
				{
					report.setReporterName(newName);
				}
			}
			
			for(Warning warning : user.getWarnings())
			{
				if(warning.getIssuerName().equals(originalName))
				{
					warning.setIssuerName(newName);
				}
			}
		}
	}
	
	public enum AlertType
	{
		NAME_CHANGE, MENTION, XRAY;
	}
}