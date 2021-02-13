package net.shortninja.staffplus.server.data.config;

import net.shortninja.staffplus.server.data.file.LanguageFile;
import net.shortninja.staffplus.util.lib.JavaUtils;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.List;

public class Messages {
    /*
     * Prefixes
     */
    public String prefixGeneral;
    public String prefixProtect;
    public String prefixReports;
    public String prefixWarnings;
    public String prefixStaffChat;
    public String prefixTrace;
    /*
     * General
     */
    public String separatorColor;
    public List<String> staffListStart;
    public String staffListMember;
    public List<String> staffListEnd;
    public String lockdown;
    public String revivedStaff;
    public String revivedUser;
    public String commandBlocked;
    public String modeCommandBlocked;
    public String commandOnCooldown;
    public String noPermission;
    public String playerOffline;
    public String playerNotRegistered;
    public String unknownCommand;
    public String invalidArguments;
    public String onlyPlayers;
    public String noFound;
    public String typeInput;
    public String inputAccepted;

    public String enabled;
    public String disabled;

    /*
     * Infractions
     */
    public String reported;
    public String reportedStaff;
    public String reportsCleared;
    public List<String> reportsListStart;
    public String reportsListEntry;
    public List<String> reportsListEnd;
    public String warned;
    public String warn;
    public String warningsCleared;
    public String warningsNotify;
    public List<String> warningsListStart;
    public String warningsListEntry;
    public List<String> warningsListEnd;
    public List<String> infractionItem;
    /*
     * Chat
     */
    public String staffChat;
    public String chatClearLine;
    public String chatCleared;
    public String chatToggled;
    public String chatPrevented;
    public String chatSlowed;
    public String chattingFast;
    public String blacklistHover;
    public String blacklistCensorColor;
    public String blacklistChatFormat;
    /*
     * Vanish
     */
    public String totalVanish;
    public String listVanish;
    public String vanishEnabled;
    /*
     * Alerts
     */
    public String alertChanged;
    public String alertsName;
    public String alertsMention;
    public String alertsXray;
    /*
     * Staff Mode
     */
    public String modeStatus;
    public String modeOriginalLocation;
    public String modeRandomTeleport;
    public String modeNotEnoughPlayers;
    public String guiReports;
    public String guiMine;
    public String guiMiner;
    public String guiCounted;
    public List<String> freeze;
    public List<String> unfrozen;
    public String staffFroze;
    public String staffUnfroze;
    public String cpsStart;
    public String cpsFinishNormal;
    public String cpsFinishMax;
    public List<String> examineFood;
    public String examineIp;
    public String examineGamemode;
    public String examineLocation;
    public String examineWarn;
    public String examineFreeze;
    public String examineNotes;
    public String examineNotesNote;
    public String follow;
    private int langVersion;
    public String noteAdded;
    public String noteCleared;
    public List<String> noteListStart;
    public String noteListEntry;
    public List<String> noteListEnd;
    public String bypassed;
    public String staffChatStatus;
    public String freezeLogout;
    public String strip;

    public String kickedNotify;
    public String kickMessage;

    public String unbanned;
    public String permanentBanned;
    public String tempBanned;
    public String permanentBannedKick;
    public String tempBannedKick;

    public String muteExpired;
    public String unmuted;
    public String permanentMuted;
    public String tempMuted;
    public String muted;

    public String appealCreated;
    public String appealApproved;
    public String appealApprove;
    public String appealRejected;
    public String appealReject;
    public String openAppealsNotify;

    public Messages() {
        reload();
    }

    public void reload() {
        FileConfiguration config = new LanguageFile().get();
        /*
         * Prefixes
         */
        prefixGeneral = config.getString("general-prefix");
        prefixProtect = config.getString("protect-prefix", "&dProtected &8»");
        prefixReports = config.getString("reports-prefix");
        prefixWarnings = config.getString("warnings-prefix");
        prefixStaffChat = config.getString("staff-chat-prefix");
        prefixTrace = config.getString("trace-prefix", "&dTrace &8»");
        /*
         * General
         */
        separatorColor = config.getString("separator-color");
        staffListStart = JavaUtils.stringToList(config.getString("staff-list-start"));
        staffListMember = config.getString("staff-list-member");
        staffListEnd = JavaUtils.stringToList(config.getString("staff-list-end"));
        lockdown = config.getString("lockdown");
        revivedStaff = config.getString("revived-staff");
        revivedUser = config.getString("revived-user");
        commandBlocked = config.getString("command-blocked");
        modeCommandBlocked = config.getString("mode-command-blocked");
        commandOnCooldown = config.getString("on-cooldown");
        noPermission = config.getString("no-permission");
        playerOffline = config.getString("player-offline");
        playerNotRegistered = config.getString("player-not-registered", "This player does not exist");
        unknownCommand = config.getString("unknown-command");
        invalidArguments = config.getString("invalid-arguments");
        onlyPlayers = config.getString("only-players");
        noFound = config.getString("no-found");
        typeInput = config.getString("type-input");
        inputAccepted = config.getString("input-accepted");

        enabled = config.getString("enabled", "enabled");
        disabled = config.getString("disabled", "disabled");

        /*
         * Infractions
         */
        reported = config.getString("reported");
        reportedStaff = config.getString("reported-staff");
        reportsCleared = config.getString("reports-cleared");
        reportsListStart = JavaUtils.stringToList(config.getString("reports-list-start"));
        reportsListEntry = config.getString("reports-list-entry");
        reportsListEnd = JavaUtils.stringToList(config.getString("reports-list-end"));
        warned = config.getString("warned");
        warn = config.getString("warn");
        warningsCleared = config.getString("warnings-cleared");
        warningsNotify = config.getString("warnings-notify");
        warningsListStart = JavaUtils.stringToList(config.getString("warnings-list-start"));
        warningsListEntry = config.getString("warnings-list-entry");
        warningsListEnd = JavaUtils.stringToList(config.getString("warnings-list-end"));
        infractionItem = JavaUtils.stringToList(config.getString("infraction-item"));
        /*
         * Chat
         */
        staffChat = config.getString("staff-chat");
        chatClearLine = config.getString("chat-clear-line");
        chatCleared = config.getString("chat-cleared");
        chatToggled = config.getString("chat-toggled");
        chatPrevented = config.getString("chat-prevented");
        chatSlowed = config.getString("chat-slowed");
        chattingFast = config.getString("chatting-fast");
        blacklistHover = config.getString("blacklist-hover");
        blacklistCensorColor = config.getString("blacklist-censor-color");
        blacklistChatFormat = config.getString("blacklist-chat-format");
        /*
         * Vanish
         */
        totalVanish = config.getString("total-vanish");
        listVanish = config.getString("list-vanish");
        vanishEnabled = config.getString("vanish-enabled");
        /*
         * Alerts
         */
        alertChanged = config.getString("alert-changed");
        alertsName = config.getString("alerts-name");
        alertsMention = config.getString("alerts-mention");
        alertsXray = config.getString("alerts-xray");
        /*
         * Staff Mode
         */
        modeStatus = config.getString("mode-status");
        modeOriginalLocation = config.getString("mode-original-location");
        modeRandomTeleport = config.getString("mode-random-teleport");
        modeNotEnoughPlayers = config.getString("mode-not-enough-players");
        guiReports = config.getString("gui-reports");
        guiMine = config.getString("gui-mine");
        guiMiner = config.getString("gui-miner");
        guiCounted = config.getString("gui-Counted");
        freeze = JavaUtils.stringToList(config.getString("freeze"));
        unfrozen = JavaUtils.stringToList(config.getString("unfrozen"));
        staffFroze = config.getString("staff-froze");
        staffUnfroze = config.getString("staff-unfroze");
        cpsStart = config.getString("cps-start");
        cpsFinishNormal = config.getString("cps-finish-normal");
        cpsFinishMax = config.getString("cps-finish-max");
        examineFood = JavaUtils.stringToList(config.getString("examine-food"));
        examineIp = config.getString("examine-ip");
        examineGamemode = config.getString("examine-gamemode");
        examineLocation = config.getString("examine-location");
        examineWarn = config.getString("examine-warn");
        examineFreeze = config.getString("examine-freeze");
        examineNotes = config.getString("examine-notes");
        examineNotesNote = config.getString("examine-notes-note");
        follow = config.getString("follow");
        langVersion = config.contains("lang-version") ? config.getInt("lang-version") : 3;
        noteAdded = langVersion >= 3.2 ? config.getString("note-added") : "&bNote added for &7%target%&b.";
        noteCleared = langVersion >= 3.2 ? config.getString("note-cleared") : "&bNotes cleared for &7%target%&b.";
        noteListStart = langVersion >= 3.2 ? JavaUtils.stringToList(config.getString("note-list-start")) : Arrays.asList("&7%longline%");
        noteListEntry = langVersion >= 3.2 ? config.getString("note-list-entry") : "&b%count% &7%note%";
        noteListEnd = langVersion >= 3.2 ? JavaUtils.stringToList(config.getString("note-list-end")) : Arrays.asList("&7%longline%");
        bypassed = langVersion >= 3.5 ? config.getString("bypassed") : "&cThat player bypassed that command!";
        staffChatStatus = langVersion >= 3.2 ? config.getString("staff-chat-status") : "&bStaff chat &7%status%&b.";
        freezeLogout = langVersion >= 3.57 ? config.getString("freeze-logout") : "&7%player% &blogged out while frozen!";
        strip = langVersion >= 6194 ? config.getString("strip") : "&7%player%'s armor has been removed!";

        kickedNotify = config.getString("kick-notifyplayers", "");
        kickMessage = config.getString("kick-kickmessage", "");

        unbanned = config.getString("ban-unbanned", "");
        permanentBanned = config.getString("ban-permabanned", "");
        tempBanned = config.getString("ban-tempbanned", "");
        permanentBannedKick = config.getString("ban-permabanned-kick", "");
        tempBannedKick = config.getString("ban-tempbanned-kick", "");

        muteExpired = config.getString("mute-expired", "");
        unmuted = config.getString("mute-unmuted", "");
        permanentMuted = config.getString("mute-permamuted", "");
        tempMuted = config.getString("mute-tempmuted", "");
        muted = config.getString("mute-muted", "");

        appealCreated = config.getString("appeal-created", "");
        appealApproved = config.getString("appeal-approved", "");
        appealRejected = config.getString("appeal-rejected", "");
        appealApprove = config.getString("appeal-approve", "");
        appealReject = config.getString("appeal-reject", "");
        openAppealsNotify = config.getString("appeal-open-notify", "");
    }
}