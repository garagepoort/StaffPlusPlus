package net.shortninja.staffplus.core.application.config;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.configuration.ConfigTransformer;
import me.rayzr522.jsonmessage.JSONMessage;
import net.shortninja.staffplus.core.common.PlaceholderService;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.utils.Strings;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@IocBean
public class Messages {

    public static final List<String> LANG_FILES = Arrays.asList(
        "lang_de",
        "lang_en",
        "lang_es",
        "lang_fr",
        "lang_hr",
        "lang_hu",
        "lang_it",
        "lang_nl",
        "lang_no",
        "lang_pt",
        "lang_sv",
        "lang_zh"
    );
    public final String LONG_LINE = "&m" + Strings.repeat('-', 48);

    /*
     * Prefixes
     */
    @ConfigProperty("%lang%:general-prefix")
    public String prefixGeneral;
    @ConfigProperty("%lang%:protect-prefix")
    public String prefixProtect;
    @ConfigProperty("%lang%:warnings-prefix")
    public String prefixWarnings;
    @ConfigProperty("%lang%:trace-prefix")
    public String prefixTrace;
    @ConfigProperty("%lang%:investigations-prefix")
    public String prefixInvestigations;
    /*
     * General
     */
    @ConfigProperty("%lang%:staff-list-start")
    @ConfigTransformer(MessageMultiLineTransformer.class)
    public List<String> staffListStart;
    @ConfigProperty("%lang%:staff-list-end")
    @ConfigTransformer(MessageMultiLineTransformer.class)
    public List<String> staffListEnd;

    @ConfigProperty("%lang%:staff-list-member")
    public String staffListMember;
    @ConfigProperty("%lang%:lockdown")
    public String lockdown;
    @ConfigProperty("%lang%:revived-staff")
    public String revivedStaff;
    @ConfigProperty("%lang%:revived-user")
    public String revivedUser;
    @ConfigProperty("%lang%:command-blocked")
    public String commandBlocked;
    @ConfigProperty("%lang%:mode-command-blocked")
    public String modeCommandBlocked;
    @ConfigProperty("%lang%:on-cooldown")
    public String commandOnCooldown;
    @ConfigProperty("%lang%:no-permission")
    public String noPermission;
    @ConfigProperty("%lang%:player-offline")
    public String playerOffline;
    @ConfigProperty("%lang%:player-not-registered")
    public String playerNotRegistered = "This player does not exist";
    @ConfigProperty("%lang%:invalid-arguments")
    public String invalidArguments;
    @ConfigProperty("%lang%:only-players")
    public String onlyPlayers;
    @ConfigProperty("%lang%:no-found")
    public String noFound;
    @ConfigProperty("%lang%:type-input")
    public String typeInput;
    @ConfigProperty("%lang%:input-accepted")
    public String inputAccepted;
    @ConfigProperty("%lang%:enabled")
    public String enabled;
    @ConfigProperty("%lang%:disabled")
    public String disabled;

    /*
     * Reports
     */
    @ConfigProperty("%lang%:reports.prefix")
    public String prefixReports;
    @ConfigProperty("%lang%:reports.reporter.report-created")
    public String reporterReportCreated;
    @ConfigProperty("%lang%:reports.reporter.report-player-created")
    public String reporterReportPlayerCreated;
    @ConfigProperty("%lang%:reports.reporter.report-rejected")
    public String reporterReportRejected;
    @ConfigProperty("%lang%:reports.reporter.report-resolved")
    public String reporterReportResolved;
    @ConfigProperty("%lang%:reports.reporter.report-accepted")
    public String reporterReportAccepted;
    @ConfigProperty("%lang%:reports.reporter.view-reports-button")
    public String reporterViewReportsButton;
    @ConfigProperty("%lang%:reports.reporter.view-reports-button-tooltip")
    public String reporterViewReportsButtonTooltip;
    @ConfigProperty("%lang%:reports.report-created-notification")
    public String reportCreatedNotification;
    @ConfigProperty("%lang%:reports.report-player-created-notification")
    public String reportCulpritCreatedNotification;
    @ConfigProperty("%lang%:reports.report-reopened-notification")
    public String reportReopenedNotification;
    @ConfigProperty("%lang%:reports.report-deleted-notification")
    public String reportDeletedNotification;
    @ConfigProperty("%lang%:reports.report-closed-notification")
    public String reportClosedNotification;
    @ConfigProperty("%lang%:reports.report-accepted-notification")
    public String reportAcceptedNotification;
    @ConfigProperty("%lang%:reports.reports-cleared")
    public String reportsCleared;
    @ConfigProperty("%lang%:reports.reports-list-entry")
    public String reportsListEntry;

    @ConfigProperty("%lang%:reports.reports-list-start")
    @ConfigTransformer(MessageMultiLineTransformer.class)
    public List<String> reportsListStart;
    @ConfigProperty("%lang%:reports.reports-list-end")
    @ConfigTransformer(MessageMultiLineTransformer.class)
    public List<String> reportsListEnd;

    @ConfigProperty("%lang%:warned")
    public String warned;
    @ConfigProperty("%lang%:warned-announcement")
    public String warnedAnnouncement;
    @ConfigProperty("%lang%:warn")
    public String warn;
    @ConfigProperty("%lang%:warnings-notify")
    public String warningsNotify;
    @ConfigProperty("%lang%:warnings-list-entry")
    public String warningsListEntry;
    @ConfigProperty("%lang%:warnings-list-start")
    @ConfigTransformer(MessageMultiLineTransformer.class)
    public List<String> warningsListStart;
    @ConfigProperty("%lang%:warnings-list-end")
    @ConfigTransformer(MessageMultiLineTransformer.class)
    public List<String> warningsListEnd;

    @ConfigProperty("%lang%:infraction-item")
    @ConfigTransformer(MessageMultiLineTransformer.class)
    public List<String> infractionItem;

    /*
     * Chat
     */
    @ConfigProperty("%lang%:chat-clear-line")
    public String chatClearLine;
    @ConfigProperty("%lang%:chat-cleared")
    public String chatCleared;
    @ConfigProperty("%lang%:chat-toggled")
    public String chatToggled;
    @ConfigProperty("%lang%:chat-prevented")
    public String chatPrevented;
    @ConfigProperty("%lang%:chat-slowed")
    public String chatSlowed;
    @ConfigProperty("%lang%:chatting-fast")
    public String chattingFast;

    @ConfigProperty("%lang%:blacklist-chat-format")
    public String blacklistChatFormat;
    /*
     * Vanish
     */
    @ConfigProperty("%lang%:total-vanish")
    public String totalVanish;
    @ConfigProperty("%lang%:list-vanish")
    public String listVanish;
    @ConfigProperty("%lang%:player-vanish")
    public String playerVanish;
    @ConfigProperty("%lang%:vanish-enabled")
    public String vanishEnabled;
    /*
     * Alerts
     */
    @ConfigProperty("%lang%:alert-changed")
    public String alertChanged;
    @ConfigProperty("%lang%:alerts-name")
    public String alertsName;
    @ConfigProperty("%lang%:alerts-chat-phrase-detected")
    public String alertsChatPhraseDetected;
    @ConfigProperty("%lang%:alerts-mention")
    public String alertsMention;
    @ConfigProperty("%lang%:alerts-xray")
    public String alertsXray;
    /*
     * Staff Mode
     */
    @ConfigProperty("%lang%:mode-status")
    public String modeStatus;
    @ConfigProperty("%lang%:mode-original-location")
    public String modeOriginalLocation;
    @ConfigProperty("%lang%:mode-random-teleport")
    public String modeRandomTeleport;
    @ConfigProperty("%lang%:mode-not-enough-players")
    public String modeNotEnoughPlayers;

    @ConfigProperty("%lang%:freeze")
    @ConfigTransformer(MessageMultiLineTransformer.class)
    public List<String> freeze;
    @ConfigProperty("%lang%:unfrozen")
    @ConfigTransformer(MessageMultiLineTransformer.class)
    public List<String> unfrozen;
    @ConfigProperty("%lang%:staff-froze")
    public String staffFroze;
    @ConfigProperty("%lang%:staff-unfroze")
    public String staffUnfroze;
    @ConfigProperty("%lang%:freeze-logout")
    public String freezeLogout;
    @ConfigProperty("%lang%:freeze-title")
    public String freezeTitle;
    @ConfigProperty("%lang%:freeze-subtitle")
    public String freezeSubtitle;

    @ConfigProperty("%lang%:cps-start")
    public String cpsStart;
    @ConfigProperty("%lang%:cps-finish-normal")
    public String cpsFinishNormal;
    @ConfigProperty("%lang%:cps-finish-max")
    public String cpsFinishMax;
    @ConfigProperty("%lang%:examine-food")
    @ConfigTransformer(MessageMultiLineTransformer.class)
    public List<String> examineFood;
    @ConfigProperty("%lang%:examine-ip")
    public String examineIp;
    @ConfigProperty("%lang%:examine-gamemode")
    public String examineGamemode;
    @ConfigProperty("%lang%:examine-location")
    public String examineLocation;
    @ConfigProperty("%lang%:examine-warn")
    public String examineWarn;
    @ConfigProperty("%lang%:examine-freeze")
    public String examineFreeze;
    @ConfigProperty("%lang%:examine-notes")
    public String examineNotes;
    @ConfigProperty("%lang%:follow")
    public String follow;
    @ConfigProperty("%lang%:strip")
    public String strip;

    @ConfigProperty("%lang%:note-added")
    public String noteAdded;
    @ConfigProperty("%lang%:note-cleared")
    public String noteCleared;
    @ConfigProperty("%lang%:note-list-start")
    @ConfigTransformer(MessageMultiLineTransformer.class)
    public List<String> noteListStart;
    @ConfigProperty("%lang%:note-list-entry")
    public String noteListEntry;
    @ConfigProperty("%lang%:note-list-end")
    @ConfigTransformer(MessageMultiLineTransformer.class)
    public List<String> noteListEnd;

    @ConfigProperty("%lang%:bypassed")
    public String bypassed;
    @ConfigProperty("%lang%:staff-chat-status")
    public String staffChatStatus;
    @ConfigProperty("%lang%:staff-chat-muted")
    public String staffChatMuted;
    @ConfigProperty("%lang%:staff-chat-unmuted")
    public String staffChatUnmuted;

    @ConfigProperty("%lang%:kick-notifyplayers")
    public String kickedNotify;
    @ConfigProperty("%lang%:kick-kickmessage")
    public String kickMessage;

    @ConfigProperty("%lang%:ban-unbanned")
    public String unbanned;
    @ConfigProperty("%lang%:ban-permabanned")
    public String permanentBanned;
    @ConfigProperty("%lang%:ban-tempbanned")
    public String tempBanned;
    @ConfigProperty("%lang%:ban-permabanned-kick")
    public String permanentBannedKick;
    @ConfigProperty("%lang%:ban-tempbanned-kick")
    public String tempBannedKick;

    @ConfigProperty("%lang%:ipbans.prefix")
    public String prefixBans;
    @ConfigProperty("%lang%:ipbans.permabanned")
    public String ipbanPermabanned;
    @ConfigProperty("%lang%:ipbans.tempbanned")
    public String ipbanTempbanned;
    @ConfigProperty("%lang%:ipbans.unbanned")
    public String ipbanUnbanned;
    @ConfigProperty("%lang%:ipbans.tempbanned-kick")
    public String ipbanTempbannedKick;
    @ConfigProperty("%lang%:ipbans.permabanned-kick")
    public String ipbanPermabannedKick;

    @ConfigProperty("%lang%:mute-expired")
    public String muteExpired;
    @ConfigProperty("%lang%:mute-unmuted")
    public String unmuted;
    @ConfigProperty("%lang%:mute-permamuted")
    public String permanentMuted;
    @ConfigProperty("%lang%:mute-tempmuted")
    public String tempMuted;
    @ConfigProperty("%lang%:mute-muted")
    public String muted;

    @ConfigProperty("%lang%:appeal-created")
    public String appealCreated;
    @ConfigProperty("%lang%:appeal-approved")
    public String appealApproved;
    @ConfigProperty("%lang%:appeal-approve")
    public String appealApprove;
    @ConfigProperty("%lang%:appeal-rejected")
    public String appealRejected;
    @ConfigProperty("%lang%:appeal-reject")
    public String appealReject;
    @ConfigProperty("%lang%:appeal-open-notify")
    public String openAppealsNotify;

    // Investigations
    @ConfigProperty("%lang%:investigated.investigation-started")
    public String investigatedInvestigationStarted;
    @ConfigProperty("%lang%:investigated.investigation-paused")
    public String investigatedInvestigationPaused;
    @ConfigProperty("%lang%:investigated.investigation-concluded")
    public String investigatedInvestigationConcluded;
    @ConfigProperty("%lang%:investigated.under-investigation-title")
    public String underInvestigationTitle;
    @ConfigProperty("%lang%:investigated.under-investigation-join")
    public String underInvestigationJoin;
    @ConfigProperty("%lang%:investigation.staff-notification-started")
    public String investigationStaffNotificationsStarted;
    @ConfigProperty("%lang%:investigation.staff-notification-concluded")
    public String investigationStaffNotificationsConcluded;
    @ConfigProperty("%lang%:investigation.staff-notification-paused")
    public String investigationStaffNotificationsPaused;
    @ConfigProperty("%lang%:investigation.staff-notification-evidence-linked")
    public String investigationEvidenceLinked;
    @ConfigProperty("%lang%:investigation.staff-notification-evidence-unlinked")
    public String investigationEvidenceUnlinked;
    @ConfigProperty("%lang%:investigation.staff-notification-note-added")
    public String investigationNoteAdded;
    @ConfigProperty("%lang%:investigation.staff-notification-note-deleted")
    public String investigationNoteDeleted;

    @ConfigProperty("%lang%:ips.prefix")
    public String ipsPrefix;
    @ConfigProperty("%lang%:ips.issuer.history-cleared")
    public String ipsHistoryCleared;
    @ConfigProperty("%lang%:ips.staff-notifications.history-cleared")
    public String ipsHistoryClearedNotification;


    private final PermissionHandler permission;
    private final PlaceholderService placeholderService;

    public Messages(PermissionHandler permission, PlaceholderService placeholderService) {
        this.permission = permission;
        this.placeholderService = placeholderService;
    }

    public String colorize(String message) {
        message = message.replaceAll("&&", "<ampersand>");
        message = ChatColor.translateAlternateColorCodes('&', message);
        return message.replaceAll("<ampersand>", "&");
    }

    public String parse(Player player, String message) {
        return colorize(placeholderService.setPlaceholders(player, message));
    }

    public void send(Player player, String message, String prefix, String permission) {
        if (!this.permission.has(player, permission)) {
            return;
        }

        message = placeholderService.setPlaceholders(player, message);
        player.sendMessage(buildMessage(prefix, message));
    }

    public void send(CommandSender sender, String message, String prefix) {
        message = placeholderService.setPlaceholders(sender, message);
        sender.sendMessage(buildMessage(prefix, message));
    }

    public void sendGlobalMessage(String message, String prefix) {
        Bukkit.broadcastMessage(buildMessage(prefix, message));
    }

    public void sendGroupMessage(String message, String permission, String prefix) {
        if (message == null) {
            return;
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            message = placeholderService.setPlaceholders(player, message);
            send(player, message, prefix, permission);
        }
    }

    public void sendGroupMessage(JSONMessage jsonMessage, String permission) {
        Bukkit.getOnlinePlayers().stream()
            .filter(p -> this.permission.has(p, permission))
            .forEach(jsonMessage::send);
    }

    public void sendCollectedMessage(Player player, Collection<String> messages, String prefix) {
        for (String message : messages) {
            message = placeholderService.setPlaceholders(player, message);
            send(player, message, prefix);
        }
    }

    private String buildMessage(String prefix, String message) {
        if (StringUtils.isEmpty(prefix)) {
            return colorize(message);
        } else {
            return colorize(prefix + " " + message);
        }
    }
}