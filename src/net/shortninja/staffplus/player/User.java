package net.shortninja.staffplus.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.data.config.Messages;
import net.shortninja.staffplus.data.config.Options;
import net.shortninja.staffplus.player.attribute.gui.AbstractGui;
import net.shortninja.staffplus.player.attribute.gui.AbstractGui.AbstractAction;
import net.shortninja.staffplus.player.attribute.infraction.Report;
import net.shortninja.staffplus.player.attribute.infraction.Warning;
import net.shortninja.staffplus.player.attribute.mode.handler.VanishHandler.VanishType;
import net.shortninja.staffplus.server.AlertCoordinator.AlertType;
import net.shortninja.staffplus.util.Message;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class User
{
	private Message message = StaffPlus.get().message;
	private Options options = StaffPlus.get().options;
	private Messages messages = StaffPlus.get().messages;
	private UUID uuid;
	private String name;
	private short glassColor;
	private List<Report> reports = new ArrayList<Report>();
	private List<Warning> warnings = new ArrayList<Warning>();
	private VanishType vanishType = VanishType.NONE;
	private List<String> playerNotes = new ArrayList<String>();
	private AbstractGui currentGui = null;
	private AbstractAction queuedAction = null;
	private Map<AlertType, Boolean> alertOptions = new HashMap<AlertType, Boolean>();
	private boolean isOnline = true;
	private boolean isChatting = false;
	private boolean isFrozen = false;
	
	public User(UUID uuid, String name, short glassColor, List<Report> reports, List<Warning> warnings, List<String> playerNotes, Map<AlertType, Boolean> alertOptions)
	{
		this.uuid = uuid;
		this.name = name;
		this.glassColor = glassColor;
		this.reports = reports;
		this.warnings = warnings;
		this.playerNotes = playerNotes;
		this.alertOptions = alertOptions;
	}
	
	public User(UUID uuid, String name)
	{
		this.uuid = uuid;
		this.glassColor = options.glassColor;
		this.name = name;
		
		for(AlertType alertType : AlertType.values())
		{
			setAlertOption(alertType, true);
		}
	}
	
	/**
	 * This method can return a null player if the user is not online, so be sure
	 * to check!
	 */
	public Player getPlayer()
	{
		return Bukkit.getPlayer(name);
	}
	
	public UUID getUuid()
	{
		return uuid;
	}
	
	public String getName()
	{
		return name;
	}
	
	public short getGlassColor()
	{
		return glassColor;
	}
	
	public List<Report> getReports()
	{
		return reports;
	}
	
	public List<Warning> getWarnings()
	{
		return warnings;
	}
	
	public List<String> getPlayerNotes()
	{
		return playerNotes;
	}
	
	public VanishType getVanishType()
	{
		return vanishType;
	}
	
	public AbstractGui getCurrentGui()
	{
		return currentGui;
	}
	
	public AbstractAction getQueuedAction()
	{
		return queuedAction;
	}
	
	public boolean shouldNotify(AlertType alertType)
	{
		return alertOptions.get(alertType) == null ? false : alertOptions.get(alertType);
	}
	
	public boolean isOnline()
	{
		return isOnline;
	}
	
	public boolean isChatting()
	{
		return isChatting;
	}
	
	public boolean isFrozen()
	{
		return isFrozen;
	}
	
	public void setGlassColor(short glassColor)
	{
		this.glassColor = glassColor;
	}
	
	/**
	 * This method should NOT be used if you want to update the user's vanish
	 * type! Use the vanish handler!
	 */
	public void setVanishType(VanishType vanishType)
	{
		this.vanishType = vanishType;
	}
	
	public void setCurrentGui(AbstractGui currentGui)
	{
		this.currentGui = currentGui;
	}
	
	public void setQueuedAction(AbstractAction queuedAction)
	{
		this.queuedAction = queuedAction;
	}
	
	public void setAlertOption(AlertType alertType, boolean isEnabled)
	{
		alertOptions.put(alertType, isEnabled);
	}
	
	public void setOnline(boolean isOnline)
	{
		this.isOnline = isOnline;
	}
	
	public void setChatting(boolean isChatting)
	{
		this.isChatting = isChatting;
	}
	
	public void setFrozen(boolean isFrozen)
	{
		this.isFrozen = isFrozen;
	}
	
	public void addReport(Report report)
	{
		reports.add(report);
	}
	
	public void removeReport(String uuid)
	{
		reports.remove(uuid);
	}
	
	public void addWarning(Warning warning)
	{
		warnings.add(warning);
	}
	
	public void removeWarning(UUID uuid)
	{
		warnings.remove(uuid);
	}
	
	public void addPlayerNote(String note)
	{
		playerNotes.add(note);
	}
}