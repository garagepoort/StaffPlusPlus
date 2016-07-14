package net.shortninja.staffplus.command;

import java.util.ArrayList;
import java.util.List;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.data.config.Messages;
import net.shortninja.staffplus.util.Message;

import org.bukkit.command.Command;

public class BaseCmd
{
	private Message message = StaffPlus.get().message;
	private Messages messages = StaffPlus.get().messages;
	private String node;
	private Command command;
	private boolean isEnabled;
	private String match;
	private String description;
	private String usage;
	private List<String> permissions = new ArrayList<String>();
	
	public BaseCmd(String node, Command command, boolean isEnabled, String permission, String description, String usage)
	{
		this.node = node;
		this.command = command;
		this.isEnabled = isEnabled;
		this.match = command.getName();
		this.description = message.colorize(description);
		this.usage = "/" + match + " " + usage;
		
		this.command.setPermissionMessage(message.colorize(messages.noPermission));
		this.command.setDescription(description);
		this.command.setUsage(usage);
		
		if(!permission.isEmpty())
		{
			this.command.setPermission(permission);
			permissions.add(permission);
		}
	}
	
	public BaseCmd(String node, Command command, boolean isEnabled, List<String> permission, String description, String usage)
	{
		this.node = node;
		this.command = command;
		this.isEnabled = isEnabled;
		this.match = command.getName();
		this.description = description;
		this.usage = "/" + match + " " + usage;
		
		this.command.setPermissionMessage(message.colorize(messages.noPermission));
		this.command.setDescription(description);
		this.command.setUsage(usage);
		permissions.addAll(permission);
	}
	
	public BaseCmd(String node, Command command, boolean isEnabled, String description, String usage)
	{
		this(node, command, isEnabled, "", description, usage);
	}
	
	public String getNode()
	{
		return node;
	}
	
	public boolean isEnabled()
	{
		return !match.isEmpty() && isEnabled;
	}
	
	public Command getCommand()
	{
		return command;
	}
	
	public String getMatch()
	{
		return match;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public String getUsage()
	{
		return usage;
	}
	
	public boolean matches(String string)
	{
		return match.equalsIgnoreCase(string);
	}
	
	public List<String> getPermissions()
	{
		return permissions;
	}
}