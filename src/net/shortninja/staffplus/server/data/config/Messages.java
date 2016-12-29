package net.shortninja.staffplus.server.data.config;

import java.util.Arrays;
import java.util.List;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.util.lib.JavaUtils;

import org.bukkit.configuration.file.FileConfiguration;

public class Messages
{
	private FileConfiguration config = StaffPlus.get().languageFile.get();
	private int langVersion = config.contains("lang-version") ? config.getInt("lang-version") : 3;
	
	/*
	 * Prefixes
	 */
	public String prefixGeneral = config.getString("general-prefix");
	public String prefixReports = config.getString("reports-prefix");
	public String prefixWarnings = config.getString("warnings-prefix");
	public String prefixStaffChat = config.getString("staff-chat-prefix");
	public String prefixTickets = config.getString("tickets-prefix");
	
	/*
	 * General
	 */
	public String separatorColor = config.getString("separator-color");
	public List<String> staffListStart = JavaUtils.stringToList(config.getString("staff-list-start"));
	public String staffListMember = config.getString("staff-list-member");
	public List<String> staffListEnd = JavaUtils.stringToList(config.getString("staff-list-end"));
	public String lockdown = config.getString("lockdown");
	public String revivedStaff = config.getString("revived-staff");
	public String revivedUser = config.getString("revived-user");
	public String commandBlocked = config.getString("command-blocked");
	public String modeCommandBlocked = config.getString("mode-command-blocked");
	public String commandOnCooldown = config.getString("on-cooldown");
	public String noteAdded = langVersion >= 3.2 ? config.getString("note-added") : "&bNote added for &7%target%&b.";
	public String noteCleared = langVersion >= 3.2 ? config.getString("note-cleared") : "&bNotes cleared for &7%target%&b.";
	public List<String> noteListStart = langVersion >= 3.2 ? JavaUtils.stringToList(config.getString("note-list-start")) : Arrays.asList("&7%longline%");
	public String noteListEntry = langVersion >= 3.2 ? config.getString("note-list-entry") : "&b%count% &7%note%";
	public List<String> noteListEnd = langVersion >= 3.2 ? JavaUtils.stringToList(config.getString("note-list-end")) : Arrays.asList("&7%longline%");
	public String loginRegister = langVersion >= 3.58 ? config.getString("login-register") : "&bSince you are staff, you must register a password to protect your account from being hijacked. Use &7/register &bto register.";
	public String loginRegistered = langVersion >= 3.58 ? config.getString("login-registered") : "&bPassword accepted! You will be asked to use this password each time you login.";
	public String loginWaiting = langVersion >= 3.58 ? config.getString("login-waiting") : "&bYou have been frozen! Please enter your staff password to continue.";
	public String loginAccepted = langVersion >= 3.58 ? config.getString("login-accepted") : "&bLogged in.";
	public String noPermission = config.getString("no-permission");
	public String bypassed = langVersion >= 3.5 ? config.getString("bypassed") : "&cThat player bypassed that command!";
	public String playerOffline = config.getString("player-offline");
	public String unknownCommand = config.getString("unknown-command");
	public String invalidArguments = config.getString("invalid-arguments");
	public String onlyPlayers = config.getString("only-players");
	public String noFound = config.getString("no-found");
	public String typeInput = config.getString("type-input");
	public String inputAccepted = config.getString("input-accepted");
	
	/*
	 * Infractions
	 */
	public String reported = config.getString("reported");
	public String reportedStaff = config.getString("reported-staff");
	public String reportsCleared = config.getString("reports-cleared");
	public List<String> reportsListStart = JavaUtils.stringToList(config.getString("reports-list-start"));
	public String reportsListEntry = config.getString("reports-list-entry");
	public List<String> reportsListEnd = JavaUtils.stringToList(config.getString("reports-list-end"));
	public String warned = config.getString("warned");
	public String warn = config.getString("warn");
	public String warningsCleared = config.getString("warnings-cleared");
	public List<String> warningsListStart = JavaUtils.stringToList(config.getString("warnings-list-start"));
	public String warningsListEntry = config.getString("warnings-list-entry");
	public List<String> warningsListEnd = JavaUtils.stringToList(config.getString("warnings-list-end"));
	public List<String> infractionItem = JavaUtils.stringToList(config.getString("infraction-item"));
	
	/*
	 * Chat
	 */
	public String staffChat = config.getString("staff-chat");
	public String staffChatStatus = langVersion >= 3.2 ? config.getString("staff-chat-status") : "&bStaff chat &7%status%&b.";
	public String chatClearLine = config.getString("chat-clear-line");
	public String chatCleared = config.getString("chat-cleared");
	public String chatToggled = config.getString("chat-toggled");
	public String chatPrevented = config.getString("chat-prevented");
	public String chatSlowed = config.getString("chat-slowed");
	public String chattingFast = config.getString("chatting-fast");
	public String blacklistHover = config.getString("blacklist-hover");
	public String blacklistCensorColor = config.getString("blacklist-censor-color");
	public String blacklistChatFormat = config.getString("blacklist-chat-format");
	
	/*
	 * Vanish
	 */
	public String totalVanish = config.getString("total-vanish");
	public String listVanish = config.getString("list-vanish");
	
	/*
	 * Tickets
	 */
	public String ticketSubmitted = config.getString("ticket-submitted");
	public String ticketOpened = config.getString("ticked-opened");
	public String ticketRemoved = config.getString("ticket-removed");
	public String ticketResponseStaff = config.getString("ticket-response-staff");
	public String ticketResponseUser = config.getString("ticket-response-user");
	public String ticket = config.getString("ticket");
	public String ticketNotFound = config.getString("ticket-not-found");
	public List<String> ticketListStart = JavaUtils.stringToList(config.getString("ticket-list-start"));
	public String ticketsListEntry = config.getString("ticket-list-entry");
	public List<String> ticketListEnd = JavaUtils.stringToList(config.getString("ticket-list-end"));
	
	/*
	 * Alerts
	 */
	public String alertChanged = config.getString("alert-changed");
	public String alertsName = config.getString("alerts-name");
	public String alertsMention = config.getString("alerts-mention");
	public String alertsXray = config.getString("alerts-xray");
	
	/*
	 * Staff Mode
	 */
	public String modeStatus = config.getString("mode-status");
	public String modeOriginalLocation = config.getString("mode-original-location");
	public String modeRandomTeleport = config.getString("mode-random-teleport");
	public String modeNotEnoughPlayers = config.getString("mode-not-enough-players");
	public String guiReports = config.getString("gui-reports");
	public String guiMine = config.getString("gui-mine");
	public String guiMiner = config.getString("gui-miner");
	public String guiCounted = config.getString("gui-Counted");
	public List<String> freeze = JavaUtils.stringToList(config.getString("freeze"));
	public List<String> unfrozen = JavaUtils.stringToList(config.getString("unfrozen"));
	public String staffFroze = config.getString("staff-froze");
	public String staffUnfroze = config.getString("staff-unfroze");
	public String freezeLogout = langVersion >= 3.57 ? config.getString("freeze-logout") : "&7%player% &blogged out while frozen!";
	public String cpsStart = config.getString("cps-start");
	public String cpsFinishNormal = config.getString("cps-finish-normal");
	public String cpsFinishMax = config.getString("cps-finish-max");
	public List<String> examineFood = JavaUtils.stringToList(config.getString("examine-food"));
	public String examineIp = config.getString("examine-ip");
	public String examineGamemode = config.getString("examine-gamemode");
	public String examineLocation = config.getString("examine-location");
	public String examineWarn = config.getString("examine-warn");
	public String examineFreeze = config.getString("examine-freeze");
	public String examineNotes = config.getString("examine-notes");
	public String examineNotesNote = config.getString("examine-notes-note");
	public String follow = config.getString("follow");
	public String strip = langVersion >= 6194 ? config.getString("strip") : "&7%player%'s armor has been removed!";
}