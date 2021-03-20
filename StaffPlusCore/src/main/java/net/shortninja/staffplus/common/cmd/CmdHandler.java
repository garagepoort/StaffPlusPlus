package net.shortninja.staffplus.common.cmd;

import be.garagepoort.staffplusplus.craftbukkit.common.IProtocol;
import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.application.StaffPlusCmd;
import net.shortninja.staffplus.domain.confirmation.ConfirmActionCmd;
import net.shortninja.staffplus.domain.chat.ChatCmd;
import net.shortninja.staffplus.common.config.Options;
import net.shortninja.staffplus.domain.staff.PersonnelCmd;
import net.shortninja.staffplus.domain.staff.alerts.AlertsCmd;
import net.shortninja.staffplus.domain.staff.altaccountdetect.cmd.AltDetectWhitelistCmd;
import net.shortninja.staffplus.domain.staff.ban.cmd.BanCmd;
import net.shortninja.staffplus.domain.staff.ban.cmd.TempBanCmd;
import net.shortninja.staffplus.domain.staff.ban.cmd.UnbanCmd;
import net.shortninja.staffplus.domain.staff.broadcast.cmd.BroadcastCmd;
import net.shortninja.staffplus.domain.staff.examine.ClearInvCmd;
import net.shortninja.staffplus.domain.staff.examine.EChestView;
import net.shortninja.staffplus.domain.staff.examine.ExamineCmd;
import net.shortninja.staffplus.domain.staff.freeze.FreezeCmd;
import net.shortninja.staffplus.domain.staff.infractions.cmd.InfractionsCmd;
import net.shortninja.staffplus.domain.staff.infractions.cmd.InfractionsTopCmd;
import net.shortninja.staffplus.domain.staff.kick.cmd.KickCmd;
import net.shortninja.staffplus.domain.staff.mode.cmd.*;
import net.shortninja.staffplus.domain.staff.mute.cmd.MuteCmd;
import net.shortninja.staffplus.domain.staff.mute.cmd.TempMuteCmd;
import net.shortninja.staffplus.domain.staff.mute.cmd.UnmuteCmd;
import net.shortninja.staffplus.domain.staff.protect.cmd.ProtectAreaCmd;
import net.shortninja.staffplus.domain.staff.protect.cmd.ProtectPlayerCmd;
import net.shortninja.staffplus.domain.staff.reporting.cmd.*;
import net.shortninja.staffplus.domain.staff.revive.ReviveCmd;
import net.shortninja.staffplus.domain.staff.staffchat.cmd.StaffChatCmd;
import net.shortninja.staffplus.domain.staff.teleport.cmd.TeleportBackCmd;
import net.shortninja.staffplus.domain.staff.teleport.cmd.TeleportHereCmd;
import net.shortninja.staffplus.domain.staff.teleport.cmd.TeleportToLocationCmd;
import net.shortninja.staffplus.domain.staff.teleport.cmd.TeleportToPlayerCmd;
import net.shortninja.staffplus.domain.staff.tracing.TraceCmd;
import net.shortninja.staffplus.domain.staff.vanish.VanishCmd;
import net.shortninja.staffplus.domain.staff.warn.warnings.cmd.*;

import java.util.Arrays;

public class CmdHandler {
    private final IProtocol versionProtocol = StaffPlus.get().versionProtocol;
    private final Options options = IocContainer.getOptions();
    /*
     * Yes this is a mess, but I need to define these things early for help commands
     * to work the way that they should.
     */
    public BaseCmd[] commands;

    public CmdHandler() {
        registerCommands();
    }

    public void reload() {
        unregisterCommands();
        registerCommands();
    }

    private void registerCommands() {
        commands = loadCommands();
        for (BaseCmd baseCmd : commands) {
            if (baseCmd.isEnabled()) {
                versionProtocol.registerCommand(baseCmd.getMatch(), baseCmd.getCommand());
            }
        }
    }

    private void unregisterCommands() {
        for (BaseCmd baseCmd : commands) {
            if (baseCmd.isEnabled()) {
                versionProtocol.unregisterCommand(baseCmd.getMatch(), baseCmd.getCommand());
            }
        }
    }

    private BaseCmd[] loadCommands() {
        return new BaseCmd[]{
            new BaseCmd(new ModeCmd(options.commandStaffMode), true, options.permissionMode, "Enables or disables staff mode.", "{player} {enable | disable}"),
            new BaseCmd(new FreezeCmd(options.commandFreeze), true, options.permissionFreeze, "Freezes or unfreezes the player", "{player} {enable | disable}"),
            new BaseCmd(new TeleportToPlayerCmd(options.commandTeleportToPlayer), true, options.permissionTeleportToPlayer, "Teleport yourself to a specific player", "{player}"),
            new BaseCmd(new TeleportHereCmd(options.commandTeleportHere), true, options.permissionTeleportHere, "Teleport a player to your position", "{player}"),
            new BaseCmd(new TeleportToLocationCmd(options.commandTeleportToLocation), true, options.permissionTeleportToLocation, "Teleports the player to predefined locations", "{player} {location}"),
            new BaseCmd(new TeleportBackCmd(options.commandTeleportBack), true, options.permissionTeleportToLocation, "Teleports the player to his last known location before teleportation happened", "{player}"),
            new BaseCmd(new ExamineCmd(options.examineConfiguration.getCommandExamine()), true, options.examineConfiguration.getPermissionExamine(), "Examines the player's inventory", "{player}"),
            new BaseCmd(new NotesCmd(options.commandNotes), true, options.examineConfiguration.getPermissionExamine(), "Adds or manages a player's notes", "[player] [note]"),
            new BaseCmd(new CpsCmd(options.commandCps), true, options.permissionCps, "Starts a CPS test on the player.", "{player}"),
            new BaseCmd(new StaffChatCmd(options.commandStaffChat), options.staffChatConfiguration.isEnabled(), options.staffChatConfiguration.getPermissionStaffChat(), "Sends a message or toggles staff chat.", "{message}"),

            new BaseCmd(new ManageReportsGuiCmd(options.manageReportConfiguration.getCommandManageReportsGui()), options.reportConfiguration.isEnabled(), options.manageReportConfiguration.getPermissionView(), "Open the manage Reports GUI.", ""),
            new BaseCmd(new ReportsCmd(options.commandReports), options.reportConfiguration.isEnabled(), "Manage Reports.", "[get|clear] [player]"),
            new BaseCmd(new MyReportsCmd(options.reportConfiguration.getMyReportsCmd()), options.reportConfiguration.isEnabled(), options.reportConfiguration.getMyReportsPermission(), "Open my reports gui", ""),
            new BaseCmd(new ReportCmd(options.commandReport), options.reportConfiguration.isEnabled(), "Sends a report without a specific player.", "[reason]"),
            new BaseCmd(new ReportPlayerCmd(options.commandReportPlayer), options.reportConfiguration.isEnabled(), "Sends a report with the given player and reason.", "[player] [reason]"),
            new BaseCmd(new FindReportsCmd(options.commandFindReports), options.reportConfiguration.isEnabled(), "Find reports.", "[filters...]"),
            new BaseCmd(new TeleportToReportLocationCmd("teleport-to-report"), options.reportConfiguration.isEnabled(), "Teleport to report location", "[reportId]"),

            new BaseCmd(new WarnCmd(options.commandWarn), options.warningConfiguration.isEnabled(), options.permissionWarn, "Issues a warning.", "[severity] [player] [reason]"),
            new BaseCmd(new WarnsCmd(options.commandWarns), options.warningConfiguration.isEnabled(), options.permissionWarn, "List all warnings of a player.", "[get] [player]"),
            new BaseCmd(new MyWarningsCmd(options.warningConfiguration.getMyWarningsCmd()), options.warningConfiguration.isEnabled(), options.warningConfiguration.getMyWarningsPermission(), "Open my warnings gui", ""),
            new BaseCmd(new ManageWarningsGuiCmd(options.manageWarningsConfiguration.getCommandManageWarningsGui()), options.warningConfiguration.isEnabled(), options.manageWarningsConfiguration.getPermissionView(), "Open the manage Warnings GUI.", "[playername]"),
            new BaseCmd(new ManageAppealedWarningsGuiCmd(options.manageWarningsConfiguration.getCommandManageAppealedWarningsGui()), options.appealConfiguration.isEnabled(), options.manageWarningsConfiguration.getPermissionView(), "Open the manage Appealed Warnings GUI.", ""),

            new BaseCmd(new VanishCmd(options.commandVanish), options.vanishEnabled, options.permissionVanishCommand, "Enables or disables the type of vanish for the player.", "[total | list] {player} {enable | disable}"),
            new BaseCmd(new ChatCmd(options.commandChat), options.chatConfiguration.isChatEnabled(), Arrays.asList(options.permissionChatClear, options.permissionChatSlow, options.permissionChatToggle), "Executes the given chat management action.", "[clear | toggle | slow] {enable | disable | time}"),
            new BaseCmd(new AlertsCmd(options.alertsConfiguration.getCommandAlerts()), true, options.alertsConfiguration.getAllAlertsPermissions(), "Enables or disables the alert type.", "[namechange | mention | xray] {player} {enable | disable}"),
            new BaseCmd(new FollowCmd(options.commandFollow), true, options.permissionFollow, "Follows or unfollows the player.", "{player}"),
            new BaseCmd(new ReviveCmd(options.commandRevive), true, options.permissionRevive, "Gives the player's previous inventory back.", "[player]"),
            new BaseCmd(new PersonnelCmd(options.commandStaffList), true, "Lists all registered staff members.", "{all | online | away | offline}"),
            new BaseCmd(new StripCmd(options.commandStrip), true, "Completely removes the target player's armor.", "[player]"),
            new BaseCmd(new StaffPlusCmd("staffplus"), true, "Used for reloading config and lang file in use", "[reload]"),
            new BaseCmd(new ClearInvCmd(options.commandClearInv), true, "Used to clear a desired player's inventory", "[player]"),
            new BaseCmd(new TraceCmd(options.commandTrace), true, "Used to start/stop tracing a player", "[start | stop] [player]"),
            new BaseCmd(new EChestView(options.enderchestsConfiguration.getCommandOpenEnderChests()), options.enderchestsConfiguration.isEnabled(), "Used to view a players ender chest", "[player]"),
            new BaseCmd(new BroadcastCmd(options.commandBroadcast), true, "Broadcast messages to all players (over all servers)", "[server] [message]"),
            new BaseCmd(new AltDetectWhitelistCmd(options.altDetectConfiguration.getCommandWhitelist()), options.altDetectConfiguration.isEnabled(), "Add/Remove players from the alt account detection whitelist", "[add/remove] [player1] [player2]"),

            new BaseCmd(new ProtectPlayerCmd(options.protectConfiguration.getCommandProtectPlayer()), options.protectConfiguration.isPlayerProtectEnabled(), "Protect a player from all damage", "[player]"),
            new BaseCmd(new ProtectAreaCmd(options.protectConfiguration.getCommandProtectArea()), options.protectConfiguration.isAreaProtectEnabled(), "Protect an area around you.", "[radius] [area name]"),

            new BaseCmd(new TempBanCmd(options.banConfiguration.getCommandTempBanPlayer()), options.banConfiguration.isEnabled(), "Temporary ban a player", "[player] [amount] [unit] [reason]"),
            new BaseCmd(new BanCmd(options.banConfiguration.getCommandBanPlayer()), options.banConfiguration.isEnabled(), "Permanent ban a player", "[player] [-template=?] [reason]"),
            new BaseCmd(new UnbanCmd(options.banConfiguration.getCommandUnbanPlayer()), options.banConfiguration.isEnabled(), "Unban a player", "[player] [reason]"),

            new BaseCmd(new KickCmd(options.kickConfiguration.getCommandKickPlayer()), options.kickConfiguration.isEnabled(), "Kick a player", "[player] [reason]"),
            new BaseCmd(new TempMuteCmd(options.muteConfiguration.getCommandTempMutePlayer()), options.muteConfiguration.isEnabled(), "Temporary mute a player", "[player] [amount] [unit] [reason]"),

            new BaseCmd(new MuteCmd(options.muteConfiguration.getCommandMutePlayer()), options.muteConfiguration.isEnabled(), "Permanent mute a player", "[player] [reason]"),
            new BaseCmd(new UnmuteCmd(options.muteConfiguration.getCommandUnmutePlayer()), options.muteConfiguration.isEnabled(), "Unmute a player", "[player] [reason]"),

            new BaseCmd(new InfractionsCmd(options.infractionsConfiguration.getCommandOpenGui()), options.infractionsConfiguration.isEnabled(), "View all player's infractions", "[player]"),
            new BaseCmd(new InfractionsTopCmd(options.infractionsConfiguration.getCommandOpenTopGui()), options.infractionsConfiguration.isEnabled(), "View the top list of players with the most infractions", "[infractionType?]"),

            new BaseCmd(new ConfirmActionCmd("confirm-action"), true, "Confirms or cancels an action.", "[confirm|cancel] [actionUuid]")
        };
    }
}