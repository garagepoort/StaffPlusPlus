package net.shortninja.staffplus.player.attribute.infraction;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.data.config.Messages;
import net.shortninja.staffplus.data.config.Options;
import net.shortninja.staffplus.player.User;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.util.Message;
import net.shortninja.staffplus.util.Permission;

import org.bukkit.entity.Player;

public class InfractionCoordinator
{
	private Permission permission = StaffPlus.get().permission;
	private Message message = StaffPlus.get().message;
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
		
		for(User user : userManager.getUsers())
		{
			warnings.addAll(user.getWarnings());
		}
		
		return warnings;
	}
	
	public void sendReport(Player player, Report report)
	{
		User user = userManager.getUser(report.getUuid());
		
		if(user == null || user.getPlayer() == null)
		{
			return;
		}
		
		if(permission.has(user.getPlayer(), options.permissionReportBypass))
		{
			message.send(player, messages.noPermission, messages.prefixGeneral);
			return;
		}
		
		addUnresolvedReport(report);
		user.addReport(report);
		message.send(player, messages.reported.replace("%player%", report.getReporterName()).replace("%target%", report.getName()).replace("%reason%", report.getReason()), messages.prefixReports);
		message.sendGroupMessage(messages.reportedStaff.replace("%player%", report.getReporterName()).replace("%target%", report.getName()).replace("%reason%", report.getReason()), options.permissionReport, messages.prefixReports);
		options.reportsSound.playForGroup(options.permissionReport);
	}
	
	public void sendWarning(Player player, Warning warning)
	{
		User user = userManager.getUser(warning.getUuid());
		
		if(user == null || user.getPlayer() == null)
		{
			return;
		}
		
		if(permission.has(user.getPlayer(), options.permissionWarnBypass))
		{
			message.send(player, messages.noPermission, messages.prefixGeneral);
			return;
		}
		
		user.addWarning(warning);
		message.send(player, messages.warned.replace("%target%", warning.getName()).replace("%reason%", warning.getReason()), messages.prefixWarnings);
		message.send(user.getPlayer(), messages.warn.replace("%reason%", warning.getReason()), messages.prefixWarnings);
		options.warningsSound.play(user.getPlayer());
		
		if(user.getWarnings().size() >= options.warningsMaximum && options.warningsMaximum > 0)
		{
			
		}
	}
}