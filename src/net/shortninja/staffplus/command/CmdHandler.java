package net.shortninja.staffplus.command;

import java.util.Arrays;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.command.cmd.AlertsCmd;
import net.shortninja.staffplus.command.cmd.ChatCmd;
import net.shortninja.staffplus.command.cmd.PersonnelCmd;
import net.shortninja.staffplus.command.cmd.ReviveCmd;
import net.shortninja.staffplus.command.cmd.StaffChatCmd;
import net.shortninja.staffplus.command.cmd.infraction.ReportCmd;
import net.shortninja.staffplus.command.cmd.infraction.TicketCmd;
import net.shortninja.staffplus.command.cmd.infraction.WarnCmd;
import net.shortninja.staffplus.command.cmd.mode.CpsCmd;
import net.shortninja.staffplus.command.cmd.mode.ExamineCmd;
import net.shortninja.staffplus.command.cmd.mode.FollowCmd;
import net.shortninja.staffplus.command.cmd.mode.FreezeCmd;
import net.shortninja.staffplus.command.cmd.mode.ModeCmd;
import net.shortninja.staffplus.command.cmd.mode.VanishCmd;
import net.shortninja.staffplus.data.config.Options;
import net.shortninja.staffplus.server.compatibility.IProtocol;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CmdHandler
{
	private IProtocol versionProtocol = StaffPlus.get().versionProtocol;
	private Options options = StaffPlus.get().options;
	
	public CmdHandler()
	{
		try
		{
			registerCommands();
		}catch(IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException exception)
		{
			exception.printStackTrace();
		}
	}
	
	public void attemptCommand(CommandSender sender, String label, String[] args)
	{
		Command command = null;
		
		for(BaseCmd baseCmd : BASES)
		{
			if(baseCmd.matches(label))
			{
				command = baseCmd.getCommand();
			}
		}
		
		if(command != null)
		{
			command.execute(sender, label, args);
		}
	}
	
	private void registerCommands() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
	{
		for(BaseCmd baseCmd : BASES)
		{
			if(baseCmd.isEnabled())
			{
				versionProtocol.registerCommand(baseCmd.getMatch(), baseCmd.getCommand());
			}
		}
	}
	
	/*
	 * Yes this is a mess, but I need to define these things early for help commands
	 * to work the way that they should.
	 */
	public final BaseCmd[] BASES =
	{
		new BaseCmd("staff-mode", new ModeCmd(options.commandStaffMode), true, options.permissionMode, "&7Enables or disables staff mode.", "{player} {enable | disable}"),
		new BaseCmd("freeze", new FreezeCmd(options.commandFreeze), true, options.permissionFreeze, "&7Freezes or unfreezes the player", "{player} {enable | disable}"),
		new BaseCmd("examine", new ExamineCmd(options.commandExamine), true, options.permissionExamine, "&7Examines the player's inventory", "{player}"),
		new BaseCmd("cps", new CpsCmd(options.commandCps), true, options.permissionCps, "&7Starts a CPS test on the player.", "{player}"),
		new BaseCmd("staff-chat", new StaffChatCmd(options.commandStaffChat), options.staffChatEnabled, options.permissionStaffChat, "&7Sends a message or toggles staff chat.", "{message}"),
		new BaseCmd("report", new ReportCmd(options.commandReport), options.reportsEnabled, "&7Sends a report with the given player and reason.", "[player] [reason]"),
		new BaseCmd("warn", new WarnCmd(options.commandWarn), options.warningsEnabled, options.permissionWarn, "&7Sends or manages a warning.", "[player | action] [reason | player]"),
		new BaseCmd("vanish", new VanishCmd(options.commandVanish), options.vanishEnabled, Arrays.asList(options.permissionVanishTotal, options.permissionVanishList), "&7Enables or disables the type of vanish for the player.", "[total | list] {player} {enable | disable}"),
		new BaseCmd("chat", new ChatCmd(options.commandChat), options.chatEnabled, Arrays.asList(options.permissionChatClear, options.permissionChatSlow, options.permissionChatToggle), "&7Executes the given chat management action.", "[clear | toggle | slow] {enable | disable | time}"),
		new BaseCmd("ticket", new TicketCmd(options.commandTicket), options.ticketsEnabled, "&7Sends a ticket to staff with your inquiry.", "[message]"),
		new BaseCmd("alerts", new AlertsCmd(options.commandAlerts), true, Arrays.asList(options.permissionMention, options.permissionNameChange, options.permissionXray), "&7Enables or disables the alert type.", "[namechange | mention | xray] {player} {enable | disable}"),
		new BaseCmd("follow", new FollowCmd(options.commandFollow), true, options.permissionFollow, "&7Follows or unfollows the player.", "{player}"),
		new BaseCmd("revive", new ReviveCmd(options.commandRevive), true, options.permissionRevive, "&7Gives the player's previous inventory back.", "[player]"),
		new BaseCmd("staff-list", new PersonnelCmd(options.commandStaffList), true, "&7Lists all registered staff members.", "{all | online | away | offline}"),
	};
}