package net.shortninja.staffplus.server.listener.player;

import java.util.UUID;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.player.attribute.mode.ModeCoordinator;
import net.shortninja.staffplus.server.command.BaseCmd;
import net.shortninja.staffplus.server.command.CmdHandler;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandPreprocess implements Listener
{
	private PermissionHandler permission = StaffPlus.get().permission;
	private MessageCoordinator message = StaffPlus.get().message;
	private Options options = StaffPlus.get().options;
	private Messages messages = StaffPlus.get().messages;
	private UserManager userManager = StaffPlus.get().userManager;
	private CmdHandler cmdHandler = StaffPlus.get().cmdHandler;
	private ModeCoordinator modeCoordinator = StaffPlus.get().modeCoordinator;
	
	public PlayerCommandPreprocess()
	{
		Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCommand(PlayerCommandPreprocessEvent event)
	{
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		String command = event.getMessage();
		
		if(command.startsWith("/help staffplus") || command.startsWith("/help staff+"))
		{
			sendHelp(player);
			event.setCancelled(true);
			return;
		}
		
		if(!permission.hasOnly(player, options.permissionBlock) || event.isCancelled())
		{
			return;
		}
		
		if(options.blockedCommands.contains(command))
		{
			message.send(player, messages.commandBlocked, messages.prefixGeneral);
			event.setCancelled(true);
		}else if(modeCoordinator.isInMode(uuid) && options.blockedModeCommands.contains(command))
		{
			message.send(player, messages.modeCommandBlocked, messages.prefixGeneral);
			event.setCancelled(true);
		}else if(userManager.get(uuid).isFrozen() && !options.modeFreezeChat && !permission.has(player, options.permissionMember))
		{
			message.send(player, messages.chatPrevented, messages.prefixGeneral);
			event.setCancelled(true);
		}
	}
	
	private void sendHelp(Player player)
	{
		int count = 0;
		
		message.send(player, "&7" + message.LONG_LINE, "");
		
		for(BaseCmd baseCmd : cmdHandler.BASES)
		{
			if(baseCmd.getPermissions().isEmpty())
			{
				message.send(player, "&b/" + baseCmd.getMatch() + " &8» " + baseCmd.getDescription().toLowerCase(), "");
				count++;
				continue;
			}
			
			for(String permission : baseCmd.getPermissions())
			{
				if(this.permission.has(player, permission))
				{
					message.send(player, "&b/" + baseCmd.getMatch() + " &8» " + baseCmd.getDescription().toLowerCase(), "");
					count++;
					break;
				}
			}
		}
		
		if(count == 0)
		{
			message.send(player, messages.noPermission, messages.prefixGeneral);
		}
		
		message.send(player, "&7" + message.LONG_LINE, "");
	}
}