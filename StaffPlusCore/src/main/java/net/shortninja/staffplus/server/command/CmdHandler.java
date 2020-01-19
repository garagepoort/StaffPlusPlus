package net.shortninja.staffplus.server.command;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.server.command.cmd.*;
import net.shortninja.staffplus.server.command.cmd.infraction.ReportCmd;
import net.shortninja.staffplus.server.command.cmd.infraction.TicketCmd;
import net.shortninja.staffplus.server.command.cmd.infraction.WarnCmd;
import net.shortninja.staffplus.server.command.cmd.mode.*;
import net.shortninja.staffplus.server.compatibility.IProtocol;
import net.shortninja.staffplus.server.data.config.Options;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

//import net.shortninja.staffplus.server.command.cmd.security.ChangePassCmd;
//import net.shortninja.staffplus.server.command.cmd.security.ResetPassCmd;
//import net.shortninja.staffplus.server.command.cmd.security.LoginCmd;
//import net.shortninja.staffplus.server.command.cmd.security.RegisterCmd;

public class CmdHandler {
    private IProtocol versionProtocol = StaffPlus.get().versionProtocol;
    private Options options = StaffPlus.get().options;
    /*
     * Yes this is a mess, but I need to define these things early for help commands
     * to work the way that they should.
     */
    public final BaseCmd[] BASES =
            {
                    new BaseCmd("staff-mode", new ModeCmd(options.commandStaffMode), true, options.permissionMode, "&7Enables or disables staff mode.", "{player} {enable | disable}"),
                    new BaseCmd("freeze", new FreezeCmd(options.commandFreeze), true, options.permissionFreeze, "&7Freezes or unfreezes the player", "{player} {enable | disable}"),
                    new BaseCmd("examine", new ExamineCmd(options.commandExamine), true, options.permissionExamine, "&7Examines the player's inventory", "{player}"),
                    new BaseCmd("notes", new NotesCmd(options.commandNotes), true, options.permissionExamine, "&7Adds or manages a player's notes", "[player] [note]"),
                    new BaseCmd("cps", new CpsCmd(options.commandCps), true, options.permissionCps, "&7Starts a CPS test on the player.", "{player}"),
                    new BaseCmd("staff-chat", new StaffChatCmd(options.commandStaffChat), options.staffChatEnabled, options.permissionStaffChat, "&7Sends a message or toggles staff chat.", "{message}"),
                    new BaseCmd("report", new ReportCmd(options.commandReport), options.reportsEnabled, "&7Sends a report with the given player and reason.", "[player] [reason]"),
                    new BaseCmd("warn", new WarnCmd(options.commandWarn), options.warningsEnabled, options.permissionWarn, "&7Sends or manages a warning.", "[player] [reason]"),
                    new BaseCmd("vanish", new VanishCmd(options.commandVanish), options.vanishEnabled, Arrays.asList(options.permissionVanishTotal, options.permissionVanishList), "&7Enables or disables the type of vanish for the player.", "[total | list] {player} {enable | disable}"),
                    new BaseCmd("chat", new ChatCmd(options.commandChat), options.chatEnabled, Arrays.asList(options.permissionChatClear, options.permissionChatSlow, options.permissionChatToggle), "&7Executes the given chat management action.", "[clear | toggle | slow] {enable | disable | time}"),
                    new BaseCmd("ticket", new TicketCmd(options.commandTicket), options.ticketsEnabled, "&7Sends a ticket to staff with your inquiry.", "[message]"),
                    new BaseCmd("alerts", new AlertsCmd(options.commandAlerts), true, Arrays.asList(options.permissionMention, options.permissionNameChange, options.permissionXray), "&7Enables or disables the alert type.", "[namechange | mention | xray] {player} {enable | disable}"),
                    new BaseCmd("follow", new FollowCmd(options.commandFollow), true, options.permissionFollow, "&7Follows or unfollows the player.", "{player}"),
                    new BaseCmd("revive", new ReviveCmd(options.commandRevive), true, options.permissionRevive, "&7Gives the player's previous inventory back.", "[player]"),
                    new BaseCmd("staff-list", new PersonnelCmd(options.commandStaffList), true, "&7Lists all registered staff members.", "{all | online | away | offline}"),
//                    new BaseCmd("login", new LoginCmd(options.commandLogin), options.loginEnabled, "&7Attempts to login with the given password.", "[password]"), // FIXME
//                    new BaseCmd("register", new RegisterCmd(options.commandRegister), options.loginEnabled, "&7Registers a password to login with.", "[password] [confirm-password] ~ inventory space must be available!"), // FIXME
                    new BaseCmd("strip", new StripCmd(options.commandStrip), true, "&7Completely removes the target player's armor.", "[player]"),
                    new BaseCmd("staffplus", new StaffPlusCmd("staffplus"), true, "Used for reloading config and lang file in use", "[reload]"),
                    new BaseCmd("clearInv", new ClearInvCmd(options.commandClearInv), true, "Used to clear a desired player's inventory", "[player]"),
//                    new BaseCmd("resetPass", new ResetPassCmd(options.commandRestPass),true,"Used to reset the password of a user who has forgotten theirs","[player] [password]"), // FIXME
//                    new BaseCmd("changePassword", new ChangePassCmd(options.commandChangePass),true,"Used to change your own password","[password] [change-password]"), // FIXME
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