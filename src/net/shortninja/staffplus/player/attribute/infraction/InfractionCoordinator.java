package net.shortninja.staffplus.player.attribute.infraction;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.User;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InfractionCoordinator
{
	private PermissionHandler permission = StaffPlus.get().permission;
	private MessageCoordinator message = StaffPlus.get().message;
	private Options options = StaffPlus.get().options;
	private Messages messages = StaffPlus.get().messages;
	private UserManager userManager = StaffPlus.get().userManager;
	private static Map<UUID, Report> unresolvedReports = new HashMap<UUID, Report>();
	
	public Collection<Report> getUnresolvedReports()
	{
		return unresolvedReports.values();
	}
	
	public Report getUnresolvedReport(UUID uuid)
	{
		return unresolvedReports.get(uuid);
	}
	
	public void addUnresolvedReport(Report report)
	{
		unresolvedReports.put(report.getUuid(), report);
	}
	
	public void removeUnresolvedReport(UUID uuid)
	{
		unresolvedReports.remove(uuid);
	}
	
	public Set<Warning> getWarnings()
	{
		Set<Warning> warnings = new HashSet<Warning>();
		
		for(User user : userManager.getAll())
		{
			warnings.addAll(user.getWarnings());
		}
		
		return warnings;
	}
	
	public void sendReport(CommandSender sender, Report report)
	{
		User user = userManager.get(report.getUuid());
		
		if(user == null || user.getPlayer() == null)
		{
			message.send(sender, messages.playerOffline, messages.prefixGeneral);
			return;
		}
		
		if(permission.has(user.getPlayer(), options.permissionReportBypass))
		{
			message.send(sender, messages.bypassed, messages.prefixGeneral);
			return;
		}
		
		addUnresolvedReport(report);
		user.addReport(report);
		message.send(sender, messages.reported.replace("%player%", report.getReporterName()).replace("%target%", report.getName()).replace("%reason%", report.getReason()), messages.prefixReports);
		message.sendGroupMessage(messages.reportedStaff.replace("%target%", report.getReporterName()).replace("%player%", report.getName()).replace("%reason%", report.getReason()), options.permissionReport, messages.prefixReports);
		options.reportsSound.playForGroup(options.permissionReport);
	}
	
	public void clearReports(User user)
	{
		user.getReports().clear();
		
		if(unresolvedReports.containsKey(user.getUuid()))
		{
			unresolvedReports.remove(user.getUuid());
		}
	}
	
	public void sendWarning(CommandSender sender, Warning warning)
	{
		User user = userManager.get(warning.getUuid());
		Player p = user.getPlayer();
		
		if(user == null || p == null)
		{
			message.send(sender, messages.playerOffline, messages.prefixGeneral);
			return;
		}
		
		if(permission.has(user.getPlayer(), options.permissionWarnBypass))
		{
			message.send(sender, messages.bypassed, messages.prefixGeneral);
			return;
		}
		
		user.addWarning(warning);
		message.send(sender, messages.warned.replace("%target%", warning.getName()).replace("%reason%", warning.getReason()), messages.prefixWarnings);
		message.send(p, messages.warn.replace("%reason%", warning.getReason()), messages.prefixWarnings);
		options.warningsSound.play(p);
		
		if(user.getWarnings().size() >= options.warningsMaximum && options.warningsMaximum > 0)
		{
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), options.warningsBanCommand.replace("%player%", p.getName()));
		}
	}
}