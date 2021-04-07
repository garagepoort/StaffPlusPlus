package net.shortninja.staffplus.core.common.config;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.authentication.AuthenticationConfiguration;
import net.shortninja.staffplus.core.authentication.AuthenticationConfigurationLoader;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.utils.Materials;
import net.shortninja.staffplus.core.domain.chat.blacklist.BlackListConfiguration;
import net.shortninja.staffplus.core.domain.chat.blacklist.BlackListConfigurationLoader;
import net.shortninja.staffplus.core.domain.chat.configuration.ChatConfiguration;
import net.shortninja.staffplus.core.domain.chat.configuration.ChatModuleLoader;
import net.shortninja.staffplus.core.domain.staff.alerts.config.AlertsConfiguration;
import net.shortninja.staffplus.core.domain.staff.alerts.config.AlertsModuleLoader;
import net.shortninja.staffplus.core.domain.staff.altaccountdetect.config.AltDetectConfiguration;
import net.shortninja.staffplus.core.domain.staff.altaccountdetect.config.AltDetectModuleLoader;
import net.shortninja.staffplus.core.domain.staff.ban.config.BanConfiguration;
import net.shortninja.staffplus.core.domain.staff.ban.config.BanModuleLoader;
import net.shortninja.staffplus.core.domain.staff.broadcast.config.BroadcastConfiguration;
import net.shortninja.staffplus.core.domain.staff.broadcast.config.BroadcastConfigurationLoader;
import net.shortninja.staffplus.core.domain.staff.chests.config.EnderchestsConfiguration;
import net.shortninja.staffplus.core.domain.staff.chests.config.EnderchestsModuleLoader;
import net.shortninja.staffplus.core.domain.staff.examine.config.ExamineConfiguration;
import net.shortninja.staffplus.core.domain.staff.examine.config.ExamineModuleLoader;
import net.shortninja.staffplus.core.domain.staff.infractions.config.InfractionsConfiguration;
import net.shortninja.staffplus.core.domain.staff.infractions.config.InfractionsModuleLoader;
import net.shortninja.staffplus.core.domain.staff.investigate.config.InvestigationConfiguration;
import net.shortninja.staffplus.core.domain.staff.investigate.config.InvestigationModuleLoader;
import net.shortninja.staffplus.core.domain.staff.kick.config.KickConfiguration;
import net.shortninja.staffplus.core.domain.staff.kick.config.KickModuleLoader;
import net.shortninja.staffplus.core.domain.staff.mode.config.*;
import net.shortninja.staffplus.core.domain.staff.mode.item.CustomModuleConfiguration;
import net.shortninja.staffplus.core.domain.staff.mute.config.MuteConfiguration;
import net.shortninja.staffplus.core.domain.staff.mute.config.MuteModuleLoader;
import net.shortninja.staffplus.core.domain.staff.protect.config.ProtectConfiguration;
import net.shortninja.staffplus.core.domain.staff.protect.config.ProtectModuleLoader;
import net.shortninja.staffplus.core.domain.staff.reporting.config.ManageReportConfiguration;
import net.shortninja.staffplus.core.domain.staff.reporting.config.ManageReportingModuleLoader;
import net.shortninja.staffplus.core.domain.staff.reporting.config.ReportConfiguration;
import net.shortninja.staffplus.core.domain.staff.reporting.config.ReportingModuleLoader;
import net.shortninja.staffplus.core.domain.staff.staffchat.config.StaffChatConfiguration;
import net.shortninja.staffplus.core.domain.staff.staffchat.config.StaffChatModuleLoader;
import net.shortninja.staffplus.core.domain.staff.teleport.config.LocationLoader;
import net.shortninja.staffplus.core.domain.staff.tracing.config.TraceConfiguration;
import net.shortninja.staffplus.core.domain.staff.tracing.config.TraceModuleLoader;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.config.AppealConfiguration;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.config.AppealModuleLoader;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.config.ManageWarningsConfiguration;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.config.ManageWarningsModuleLoader;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.config.WarningConfiguration;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.config.WarningModuleLoader;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfiguration;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncModuleLoader;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.Map;

//TODO: replace this with something that isn't horribly coupled...
@IocBean
public class Options {

    public List<String> blockedCommands;
    public List<String> blockedModeCommands;
    public String glassTitle;

    public String serverName;
    public String mainWorld;
    public String timestampFormat;
    public int autoSave;
    public long clock;
    private List<String> soundNames;
    public boolean offlinePlayersModeEnabled;

    public Map<String, Location> locations;
    public AuthenticationConfiguration authenticationConfiguration;
    public InfractionsConfiguration infractionsConfiguration;
    public InvestigationConfiguration investigationConfiguration;
    public ReportConfiguration reportConfiguration;
    public ManageReportConfiguration manageReportConfiguration;
    public ManageWarningsConfiguration manageWarningsConfiguration;
    public WarningConfiguration warningConfiguration;
    public AppealConfiguration appealConfiguration;
    public BlackListConfiguration blackListConfiguration;
    public TraceConfiguration traceConfiguration;
    public BroadcastConfiguration broadcastConfiguration;
    public ProtectConfiguration protectConfiguration;
    public BanConfiguration banConfiguration;
    public KickConfiguration kickConfiguration;
    public MuteConfiguration muteConfiguration;
    public AltDetectConfiguration altDetectConfiguration;
    public StaffChatConfiguration staffChatConfiguration;
    public ExamineConfiguration examineConfiguration;
    public EnderchestsConfiguration enderchestsConfiguration;
    public GeneralModeConfiguration modeConfiguration;
    public ServerSyncConfiguration serverSyncConfiguration;
    public AlertsConfiguration alertsConfiguration;
    public ChatConfiguration chatConfiguration;
    public StaffItemsConfiguration staffItemsConfiguration;

    /*
     * Vanish
     */
    public boolean vanishEnabled;
    public boolean vanishTabList;
    public boolean vanishShowAway;
    public boolean vanishChatEnabled;
    public boolean vanishMessageEnabled;

    /*
     * Staff Mode
     */
    public boolean staffView;

    /*
     * Custom
     */
    public List<CustomModuleConfiguration> customModuleConfigurations;
    /*
     * Permissions
     */
    public String permissionWildcard;
    public String permissionBlock;
    public String permissionReport;
    public String permissionReportBypass;
    public String permissionReportUpdateNotifications;
    public String permissionWarn;
    public String permissionWarnBypass;
    public String permissionVanishCommand;
    public String permissionVanishTotal;
    public String permissionVanishList;
    public String permissionChatClear;
    public String permissionChatToggle;
    public String permissionChatSlow;
    public String permissionBlacklist;
    public String permissionMode;
    public String permissionModeSilentChestInteraction;
    public String permissionFreeze;
    public String permissionFreezeBypass;
    public String permissionTeleportToLocation;
    public String permissionTeleportToPlayer;
    public String permissionTeleportHere;
    public String permissionTeleportBypass;
    public String permissionTrace;
    public String permissionTraceBypass;
    public String permissionCps;
    public String permissionFollow;
    public String permissionRevive;
    public String permissionMember;
    public String ipHidePerm;
    public String permissionClearInv;
    public String permissionClearInvBypass;
    public String permissionBroadcast;
    public String permissionCounterGuiShowVanish;

    /*
     * Commands
     */
    public String commandStaffMode;
    public String commandStaffFly;
    public String commandFreeze;
    public String commandTeleportToLocation;
    public String commandTeleportBack;
    public String commandTeleportToPlayer;
    public String commandTeleportHere;
    public String commandCps;
    public String commandStaffChat;
    public String commandReport;
    public String commandReportPlayer;
    public String commandReports;
    public String commandFindReports;
    public String commandWarn;
    public String commandWarns;
    public String commandVanish;
    public String commandChat;
    public String commandFollow;
    public String commandRevive;
    public String commandStaffList;
    public String commandClearInv;
    public String commandTrace;
    public String commandBroadcast;

    public String permissionStrip;
    public String permissionStaff;
    public String commandNotes;
    public String commandLogin;
    public String commandStrip;

    /*
     * Storage
     */
    public String storageType;
    public String mySqlHost;
    public String mySqlUser;
    public String database;
    public String mySqlPassword;
    public int mySqlPort;

    private final AuthenticationConfigurationLoader authenticationConfigurationLoader;
    private final InfractionsModuleLoader infractionsModuleLoader;
    private final ReportingModuleLoader reportingModuleLoader;
    private final ManageReportingModuleLoader manageReportingModuleLoader;
    private final WarningModuleLoader warningModuleLoader;
    private final AppealModuleLoader appealModuleLoader;
    private final ManageWarningsModuleLoader manageWarningsModuleLoader;
    private final BlackListConfigurationLoader blackListConfigurationLoader;
    private final TraceModuleLoader traceModuleLoader;
    private final BroadcastConfigurationLoader broadcastConfigurationLoader;
    private final ProtectModuleLoader protectModuleLoader;
    private final BanModuleLoader banModuleLoader;
    private final KickModuleLoader kickModuleLoader;
    private final MuteModuleLoader muteModuleLoader;
    private final AltDetectModuleLoader altDetectModuleLoader;
    private final StaffChatModuleLoader staffChatModuleLoader;
    private final ExamineModuleLoader examineModuleLoader;
    private final EnderchestsModuleLoader enderchestsModuleLoader;
    private final StaffModeModuleLoader staffModeModuleLoader;
    private final ServerSyncModuleLoader serverSyncModuleLoader;
    private final AlertsModuleLoader alertsModuleLoader;
    private final ChatModuleLoader chatModuleLoader;
    private final InvestigationModuleLoader investigationModuleLoader;
    private final StaffCustomItemsLoader staffCustomItemsLoader;
    private final StaffItemsLoader staffItemsLoader;

    public Options(AuthenticationConfigurationLoader authenticationConfigurationLoader,
                   InfractionsModuleLoader infractionsModuleLoader,
                   ReportingModuleLoader reportingModuleLoader,
                   ManageReportingModuleLoader manageReportingModuleLoader,
                   WarningModuleLoader warningModuleLoader,
                   AppealModuleLoader appealModuleLoader,
                   ManageWarningsModuleLoader manageWarningsModuleLoader,
                   BlackListConfigurationLoader blackListConfigurationLoader,
                   TraceModuleLoader traceModuleLoader,
                   BroadcastConfigurationLoader broadcastConfigurationLoader,
                   ProtectModuleLoader protectModuleLoader,
                   BanModuleLoader banModuleLoader,
                   KickModuleLoader kickModuleLoader,
                   MuteModuleLoader muteModuleLoader,
                   AltDetectModuleLoader altDetectModuleLoader,
                   StaffChatModuleLoader staffChatModuleLoader,
                   ExamineModuleLoader examineModuleLoader,
                   EnderchestsModuleLoader enderchestsModuleLoader,
                   StaffModeModuleLoader staffModeModuleLoader,
                   ServerSyncModuleLoader serverSyncModuleLoader,
                   AlertsModuleLoader alertsModuleLoader,
                   ChatModuleLoader chatModuleLoader,
                   InvestigationModuleLoader investigationModuleLoader,
                   StaffCustomItemsLoader staffCustomItemsLoader,
                   StaffItemsLoader staffItemsLoader) {
        this.authenticationConfigurationLoader = authenticationConfigurationLoader;
        this.infractionsModuleLoader = infractionsModuleLoader;
        this.reportingModuleLoader = reportingModuleLoader;
        this.manageReportingModuleLoader = manageReportingModuleLoader;
        this.warningModuleLoader = warningModuleLoader;
        this.appealModuleLoader = appealModuleLoader;
        this.manageWarningsModuleLoader = manageWarningsModuleLoader;
        this.blackListConfigurationLoader = blackListConfigurationLoader;
        this.traceModuleLoader = traceModuleLoader;
        this.broadcastConfigurationLoader = broadcastConfigurationLoader;
        this.protectModuleLoader = protectModuleLoader;
        this.banModuleLoader = banModuleLoader;
        this.kickModuleLoader = kickModuleLoader;
        this.muteModuleLoader = muteModuleLoader;
        this.altDetectModuleLoader = altDetectModuleLoader;
        this.staffChatModuleLoader = staffChatModuleLoader;
        this.examineModuleLoader = examineModuleLoader;
        this.enderchestsModuleLoader = enderchestsModuleLoader;
        this.staffModeModuleLoader = staffModeModuleLoader;
        this.serverSyncModuleLoader = serverSyncModuleLoader;
        this.alertsModuleLoader = alertsModuleLoader;
        this.chatModuleLoader = chatModuleLoader;
        this.investigationModuleLoader = investigationModuleLoader;
        this.staffCustomItemsLoader = staffCustomItemsLoader;
        this.staffItemsLoader = staffItemsLoader;
        reload();
    }

    public void reload() {
        FileConfiguration defaultConfig = StaffPlus.get().getFileConfigurations().get("config");
        FileConfiguration permissionsConfig = StaffPlus.get().getFileConfigurations().get("permissions");
        FileConfiguration commandsConfig = StaffPlus.get().getFileConfigurations().get("commands");

        blockedCommands = JavaUtils.stringToList(defaultConfig.getString("blocked-commands", ""));
        blockedModeCommands = JavaUtils.stringToList(defaultConfig.getString("blocked-mode-commands", ""));
        glassTitle = defaultConfig.getString("glass-title");

        serverName = defaultConfig.getString("server-name");
        mainWorld = defaultConfig.getString("main-world");
        timestampFormat = defaultConfig.getString("timestamp-format");
        autoSave = defaultConfig.getInt("auto-save");
        clock = defaultConfig.getInt("clock") * 20;
        soundNames = JavaUtils.stringToList(defaultConfig.getString("sound-names"));
        offlinePlayersModeEnabled = defaultConfig.getBoolean("offline-players-mode");

        locations = new LocationLoader().loadConfig();
        authenticationConfiguration = this.authenticationConfigurationLoader.loadConfig();
        infractionsConfiguration = this.infractionsModuleLoader.loadConfig();
        reportConfiguration = this.reportingModuleLoader.loadConfig();
        manageReportConfiguration = this.manageReportingModuleLoader.loadConfig();
        warningConfiguration = this.warningModuleLoader.loadConfig();
        appealConfiguration = this.appealModuleLoader.loadConfig();
        manageWarningsConfiguration = this.manageWarningsModuleLoader.loadConfig();
        blackListConfiguration = this.blackListConfigurationLoader.loadConfig();
        traceConfiguration = this.traceModuleLoader.loadConfig();
        broadcastConfiguration = this.broadcastConfigurationLoader.loadConfig();
        protectConfiguration = this.protectModuleLoader.loadConfig();
        banConfiguration = this.banModuleLoader.loadConfig();
        kickConfiguration = this.kickModuleLoader.loadConfig();
        muteConfiguration = this.muteModuleLoader.loadConfig();
        altDetectConfiguration = this.altDetectModuleLoader.loadConfig();
        staffChatConfiguration = this.staffChatModuleLoader.loadConfig();
        examineConfiguration = this.examineModuleLoader.loadConfig();
        enderchestsConfiguration = this.enderchestsModuleLoader.loadConfig();
        modeConfiguration = this.staffModeModuleLoader.loadConfig();
        serverSyncConfiguration = this.serverSyncModuleLoader.loadConfig();
        alertsConfiguration = this.alertsModuleLoader.loadConfig();
        chatConfiguration = this.chatModuleLoader.loadConfig();
        investigationConfiguration = this.investigationModuleLoader.loadConfig();
        customModuleConfigurations = this.staffCustomItemsLoader.loadConfig();
        staffItemsConfiguration = this.staffItemsLoader.loadConfig();

        /*
         * Vanish
         */
        vanishEnabled = defaultConfig.getBoolean("vanish-module.enabled");
        vanishTabList = defaultConfig.getBoolean("vanish-module.tab-list");
        vanishShowAway = defaultConfig.getBoolean("vanish-module.show-away");
        vanishChatEnabled = defaultConfig.getBoolean("vanish-module.chat");
        vanishMessageEnabled = defaultConfig.getBoolean("vanish-module.vanish-message-enabled");

        /*
         * Staff Mode
         */
        staffView = defaultConfig.getBoolean("staff-mode.staff-see-staff-in-mode");

        /*
         * Permissions
         */
        permissionStaff = permissionsConfig.getString("permissions.staffplus");
        permissionStrip = permissionsConfig.getString("permissions.strip");
        permissionWildcard = permissionsConfig.getString("permissions.wild-card");
        permissionBlock = permissionsConfig.getString("permissions.block");
        permissionReport = permissionsConfig.getString("permissions.report");
        permissionReportBypass = permissionsConfig.getString("permissions.report-bypass");
        permissionReportUpdateNotifications = permissionsConfig.getString("permissions.report-update-notifications");
        permissionWarn = permissionsConfig.getString("permissions.warn");
        permissionWarnBypass = permissionsConfig.getString("permissions.warn-bypass");
        permissionVanishCommand = permissionsConfig.getString("permissions.vanish");
        permissionVanishTotal = permissionsConfig.getString("permissions.vanish-total");
        permissionVanishList = permissionsConfig.getString("permissions.vanish-list");
        permissionChatClear = permissionsConfig.getString("permissions.chat-clear");
        permissionChatToggle = permissionsConfig.getString("permissions.chat-toggle");
        permissionChatSlow = permissionsConfig.getString("permissions.chat-slow");
        permissionBlacklist = permissionsConfig.getString("permissions.blacklist");
        permissionMode = permissionsConfig.getString("permissions.mode");
        permissionModeSilentChestInteraction = permissionsConfig.getString("permissions.mode-silent-chest-interaction");
        permissionFreeze = permissionsConfig.getString("permissions.freeze");
        permissionFreezeBypass = permissionsConfig.getString("permissions.freeze-bypass");
        permissionTeleportToLocation = permissionsConfig.getString("permissions.teleport-to-location");
        permissionTeleportToPlayer = permissionsConfig.getString("permissions.teleport-to-player");
        permissionTeleportHere = permissionsConfig.getString("permissions.teleport-here");
        permissionTeleportBypass = permissionsConfig.getString("permissions.teleport-bypass");
        permissionTrace = permissionsConfig.getString("permissions.trace");
        permissionTraceBypass = permissionsConfig.getString("permissions.trace-bypass");
        permissionCps = permissionsConfig.getString("permissions.cps");
        permissionFollow = permissionsConfig.getString("permissions.follow");
        permissionRevive = permissionsConfig.getString("permissions.revive");
        permissionMember = permissionsConfig.getString("permissions.member");
        ipHidePerm = permissionsConfig.getString("permissions.ipPerm");
        permissionClearInv = permissionsConfig.getString("permissions.invClear");
        permissionClearInvBypass = permissionsConfig.getString("permissions.invClear-bypass");
        permissionBroadcast = permissionsConfig.getString("permissions.broadcast");
        permissionCounterGuiShowVanish = permissionsConfig.getString("permissions.counter-show-vanished");

        /*
         * Commands
         */
        commandStaffMode = commandsConfig.getString("commands.staff-mode");
        commandStaffFly = commandsConfig.getString("commands.staff-mode-fly");
        commandFreeze = commandsConfig.getString("commands.freeze");
        commandTeleportToLocation = commandsConfig.getString("commands.teleport-to-location");
        commandTeleportBack = commandsConfig.getString("commands.teleport-back");
        commandTeleportToPlayer = commandsConfig.getString("commands.teleport-to-player");
        commandTeleportHere = commandsConfig.getString("commands.teleport-here");
        commandCps = commandsConfig.getString("commands.cps");
        commandStaffChat = commandsConfig.getString("commands.staff-chat");
        commandReport = commandsConfig.getString("commands.report");
        commandReportPlayer = commandsConfig.getString("commands.reportPlayer");
        commandReports = commandsConfig.getString("commands.reports.manage.cli");
        commandFindReports = commandsConfig.getString("commands.reports.manage.gui-find-reports");
        commandWarn = commandsConfig.getString("commands.warn");
        commandWarns = commandsConfig.getString("commands.warns");
        commandVanish = commandsConfig.getString("commands.vanish");
        commandChat = commandsConfig.getString("commands.chat");
        commandFollow = commandsConfig.getString("commands.follow");
        commandRevive = commandsConfig.getString("commands.revive");
        commandStaffList = commandsConfig.getString("commands.staff-list");
        commandClearInv = commandsConfig.getString("commands.clearInv");
        commandTrace = commandsConfig.getString("commands.trace");
        commandBroadcast = commandsConfig.getString("commands.broadcast");
        commandNotes = commandsConfig.getString("commands.notes");
        commandLogin = commandsConfig.getString("commands.login");
        commandStrip = commandsConfig.getString("commands.strip");

        /*
         * Storage
         */
        storageType = defaultConfig.getString("storage.type");
        mySqlHost = defaultConfig.getString("storage.mysql.host");
        mySqlUser = defaultConfig.getString("storage.mysql.user");
        database = defaultConfig.getString("storage.mysql.database");
        mySqlPassword = defaultConfig.getString("storage.mysql.password");
        mySqlPort = defaultConfig.getInt("storage.mysql.port");
    }


    public static String getMaterial(String current) {
        switch (current) {
            case "HEAD":
                return Materials.valueOf("HEAD").getName();
            case "SPAWNER":
                return Materials.valueOf("SPAWNER").getName();
            case "ENDEREYE":
                return Materials.valueOf("ENDEREYE").getName();
            case "CLOCK":
                return Materials.valueOf("CLOCK").getName();
            case "LEAD":
                return Materials.valueOf("LEAD").getName();
            case "INK":
                return Materials.valueOf("INK").getName();
            default:
                return current;

        }

    }

    public static Material stringToMaterial(String string) {
        Material sound = Material.STONE;

        boolean isValid = JavaUtils.isValidEnum(Material.class, getMaterial(string));
        if (!isValid) {
            Bukkit.getLogger().severe("Invalid material type '" + string + "'!");
        } else
            sound = Material.valueOf(getMaterial(string));

        return sound;
    }
}