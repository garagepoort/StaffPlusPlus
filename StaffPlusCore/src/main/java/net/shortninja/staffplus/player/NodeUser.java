package net.shortninja.staffplus.player;

import java.util.ArrayList;
import java.util.List;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.attribute.infraction.Report;
import net.shortninja.staffplus.player.attribute.SecurityHandler;
import net.shortninja.staffplus.player.attribute.infraction.Warning;
import net.shortninja.staffplus.server.AlertCoordinator.AlertType;

public class NodeUser
{
	private SecurityHandler securityHandler = StaffPlus.get().securityHandler;
	private User user;
	private String prefix;
	
	public NodeUser(User user)
	{
		this.user = user;
		this.prefix = user.getUuid() + ".";
	}
	
	public String prefix()
	{
		return prefix;
	}
	
	public String name()
	{
		return user.getName();
	}
	
	public String password()
	{
		return securityHandler.getPassword(user.getUuid());
	}
	
	public short glassColor()
	{
		return user.getGlassColor();
	}

	public List<String> reports()
	{
		List<String> reportsList = new ArrayList<String>();
		
		for(Report report : user.getReports())
		{
			reportsList.add(report.getReason() + ";" + report.getReporterName() + ";" + (report.getReporterUuid() == null ? "null" : report.getReporterUuid().toString()));
		}
		
		return reportsList;
	}
	
	public List<String> warnings()
	{
		List<String> warningsList = new ArrayList<String>();
		
		for(Warning warning : user.getWarnings())
		{
			warningsList.add(warning.getReason() + ";" + warning.getIssuerName() + ";" +  (warning.getIssuerUuid() == null ? "null" : warning.getIssuerUuid().toString()) + ";" + Long.toString(warning.getTime()));
		}
		
		return warningsList;
	}
	
	public List<String> playerNotes()
	{
		return user.getPlayerNotes();
	}
	
	public List<String> alertOptions()
	{
		List<String> alertsList = new ArrayList<String>();
		
		for(AlertType alertType : AlertType.values())
		{
			alertsList.add(alertType.name() + ";" + Boolean.toString(user.shouldNotify(alertType)));
		}
		
		return alertsList;
	}
}