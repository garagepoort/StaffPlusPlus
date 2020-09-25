package net.shortninja.staffplus.server.command;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.staff.reporting.cmd.ReportCmd;
import net.shortninja.staffplus.staff.reporting.cmd.ReportPlayerCmd;
import net.shortninja.staffplus.staff.reporting.cmd.ReportsCmd;
import net.shortninja.staffplus.server.command.cmd.*;
import net.shortninja.staffplus.server.command.cmd.mode.*;
import net.shortninja.staffplus.server.compatibility.IProtocol;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.freeze.FreezeCmd;
import net.shortninja.staffplus.staff.tracing.TraceCmd;
import net.shortninja.staffplus.staff.warn.cmd.WarnCmd;
import net.shortninja.staffplus.staff.warn.cmd.WarnsCmd;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class CmdHandler {
    private IProtocol versionProtocol = StaffPlus.get().versionProtocol;
    private Options options = IocContainer.getOptions();
    /*
     * Yes this is a mess, but I need to define these things early for help commands
     * to work the way that they should.
     */
    public final BaseCmd[] BASES =
            {
                    new BaseCmd("staff-mode", new ModeCmd(options.commandStaffMode), true, options.permissionMode, "&7Enables or disables staff mode.", "{player} {enable | disable}"),
                    new BaseCmd("freeze", new FreezeCmd(options.commandFreeze), true, options.permissionFreeze, "&7Freezes or unfreezes the player", "{player} {enable | disable}"),
                    new BaseCmd("teleport", new TeleportCmd(options.commandTeleport), true, options.permissionTeleport, "&7Teleports the player to predefined locations", "{player} {location}"),
                    new BaseCmd("examine", new ExamineCmd(options.commandExamine), true, options.permissionExamine, "&7Examines the player's inventory", "{player}"),
                    new BaseCmd("notes", new NotesCmd(options.commandNotes), true, options.permissionExamine, "&7Adds or manages a player's notes", "[player] [note]"),
                    new BaseCmd("cps", new CpsCmd(options.commandCps), true, options.permissionCps, "&7Starts a CPS test on the player.", "{player}"),
                    new BaseCmd("staff-chat", new StaffChatCmd(options.commandStaffChat), options.staffChatEnabled, options.permissionStaffChat, "&7Sends a message or toggles staff chat.", "{message}"),
                    new BaseCmd("reports", new ReportsCmd(options.commandReports), options.reportConfiguration.isEnabled(), "&7Manage Reports.", "[get|clear] [player]"),
                    new BaseCmd("report", new ReportCmd(options.commandReport), options.reportConfiguration.isEnabled(), "&7Sends a report without a specific player.", "[reason]"),
                    new BaseCmd("reportPlayer", new ReportPlayerCmd(options.commandReportPlayer), options.reportConfiguration.isEnabled(), "&7Sends a report with the given player and reason.", "[player] [reason]"),
                    new BaseCmd("warn", new WarnCmd(options.commandWarn), options.warningConfiguration.isEnabled(), options.permissionWarn, "&7Issues a warning.", "[severity] [player] [reason]"),
                    new BaseCmd("warns", new WarnsCmd(options.commandWarns), options.warningConfiguration.isEnabled(), options.permissionWarn, "&7Clear or list all warnings of a player.", "[get|clear] [player]"),
                    new BaseCmd("vanish", new VanishCmd(options.commandVanish), options.vanishEnabled, Arrays.asList(options.permissionVanishTotal, options.permissionVanishList), "&7Enables or disables the type of vanish for the player.", "[total | list] {player} {enable | disable}"),
                    new BaseCmd("chat", new ChatCmd(options.commandChat), options.chatEnabled, Arrays.asList(options.permissionChatClear, options.permissionChatSlow, options.permissionChatToggle), "&7Executes the given chat management action.", "[clear | toggle | slow] {enable | disable | time}"),
                    new BaseCmd("alerts", new AlertsCmd(options.commandAlerts), true, Arrays.asList(options.permissionMention, options.permissionNameChange, options.permissionXray), "&7Enables or disables the alert type.", "[namechange | mention | xray] {player} {enable | disable}"),
                    new BaseCmd("follow", new FollowCmd(options.commandFollow), true, options.permissionFollow, "&7Follows or unfollows the player.", "{player}"),
                    new BaseCmd("revive", new ReviveCmd(options.commandRevive), true, options.permissionRevive, "&7Gives the player's previous inventory back.", "[player]"),
                    new BaseCmd("staff-list", new PersonnelCmd(options.commandStaffList), true, "&7Lists all registered staff members.", "{all | online | away | offline}"),
                    new BaseCmd("strip", new StripCmd(options.commandStrip), true, "&7Completely removes the target player's armor.", "[player]"),
                    new BaseCmd("staffplus", new StaffPlusCmd("staffplus"), true, "Used for reloading config and lang file in use", "[reload]"),
                    new BaseCmd("clearInv", new ClearInvCmd(options.commandClearInv), true, "Used to clear a desired player's inventory", "[player]"),
                    new BaseCmd("trace", new TraceCmd(options.commandTrace), true, "Used to start/stop tracing a player", "[start | stop] [player]"),
                    new BaseCmd("echest-view",new EChestView(options.commandEChestView), options.enderChestEnabled,"Used to view a players ender chest", "[player]")
            };

    public CmdHandler() {
        registerCommands();
    }

    public void attemptCommand(CommandSender sender, String label, String[] args) {
        Command command = null;

        for (BaseCmd baseCmd : BASES) {
            if (baseCmd.matches(label)) {
                command = baseCmd.getCommand();
            }
        }

        if (command != null) {
            command.execute(sender, label, args);
        }
    }

    private void registerCommands() {
        for (BaseCmd baseCmd : BASES) {
            if (baseCmd.isEnabled()) {
                versionProtocol.registerCommand(baseCmd.getMatch(), baseCmd.getCommand());
            }
        }
    }
}