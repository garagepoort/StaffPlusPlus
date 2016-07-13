package net.shortninja.staffplus.command.cmd;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.data.config.Messages;
import net.shortninja.staffplus.data.config.Options;
import net.shortninja.staffplus.server.chat.ChatHandler;
import net.shortninja.staffplus.util.Message;
import net.shortninja.staffplus.util.Permission;
import net.shortninja.staffplus.util.lib.JavaUtils;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class ChatCmd extends BukkitCommand
{
	private Permission permission = StaffPlus.get().permission;
	private Message message = StaffPlus.get().message;
	private Options options = StaffPlus.get().options;
	private Messages messages = StaffPlus.get().messages;
	private ChatHandler chatHandler = StaffPlus.get().chatHandler;
	
	public ChatCmd(String name)
	{
		super(name);
	}

	@Override
	public boolean execute(CommandSender sender, String alias, String[] args)
	{
		if(args.length >= 2 && permission.isOp(sender))
		{
			handleChatArgument(sender, args[0], args[1], false);
		}else if(args.length == 1)
		{
			handleChatArgument(sender, args[0], "", true);
		}else sendHelp(sender);
		
		return true;
	}
	
	private void handleChatArgument(CommandSender sender, String argument, String option, boolean shouldCheckPermission)
	{
		String name = sender instanceof Player ? sender.getName() : "Console";
		
		switch(argument.toLowerCase())
		{
			case "clear":
				if(permission.has(sender, options.permissionChatClear) || !shouldCheckPermission)
				{
					chatHandler.clearChat(name);
				}else message.send(sender, messages.noPermission, messages.prefixGeneral);
				break;
			case "toggle":
				if(permission.has(sender, options.permissionChatToggle) || !shouldCheckPermission)
				{
					chatHandler.setChatEnabled(name, option.isEmpty() ? !chatHandler.isChatEnabled() : Boolean.valueOf(option));
				}else message.send(sender, messages.noPermission, messages.prefixGeneral);
				break;
			case "slow":
				if(permission.has(sender, options.permissionChatSlow) || !shouldCheckPermission)
				{
					if(JavaUtils.isInteger(option))
					{
						chatHandler.setChatSlow(name, Integer.parseInt(option));
					}else message.send(sender, messages.invalidArguments.replace("%usage%", getName() + " &7" + getUsage()), messages.prefixGeneral);
				}else message.send(sender, messages.noPermission, messages.prefixGeneral);
				break;
			default:
				message.send(sender, messages.invalidArguments.replace("%usage%", getName() + " &7" + getUsage()), messages.prefixGeneral);
				break;
		}
	}
	
	private void sendHelp(CommandSender sender)
	{
		message.send(sender, message.LONG_LINE, "");
		message.send(sender, "&b/" + getName() + " &7" + getUsage(), messages.prefixGeneral);
		message.send(sender, message.LONG_LINE, "");
	}
}