package net.shortninja.staffplus.server.data.config;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.authentication.AuthenticationConfiguration;
import net.shortninja.staffplus.authentication.AuthenticationConfigurationLoader;
import net.shortninja.staffplus.server.chat.blacklist.BlackListConfiguration;
import net.shortninja.staffplus.server.chat.blacklist.BlackListConfigurationLoader;
import net.shortninja.staffplus.staff.alerts.xray.XrayBlockConfig;
import net.shortninja.staffplus.staff.altaccountdetect.config.AltDetectConfiguration;
import net.shortninja.staffplus.staff.altaccountdetect.config.AltDetectModuleLoader;
import net.shortninja.staffplus.staff.ban.config.BanConfiguration;
import net.shortninja.staffplus.staff.ban.config.BanModuleLoader;
import net.shortninja.staffplus.staff.broadcast.config.BroadcastConfiguration;
import net.shortninja.staffplus.staff.broadcast.config.BroadcastConfigurationLoader;
import net.shortninja.staffplus.staff.chests.config.EnderchestsConfiguration;
import net.shortninja.staffplus.staff.chests.config.EnderchestsModuleLoader;
import net.shortninja.staffplus.staff.examine.config.ExamineConfiguration;
import net.shortninja.staffplus.staff.examine.config.ExamineModuleLoader;
import net.shortninja.staffplus.staff.infractions.config.InfractionsConfiguration;
import net.shortninja.staffplus.staff.infractions.config.InfractionsModuleLoader;
import net.shortninja.staffplus.staff.kick.config.KickConfiguration;
import net.shortninja.staffplus.staff.kick.config.KickModuleLoader;
import net.shortninja.staffplus.staff.mode.item.ModuleConfiguration;
import net.shortninja.staffplus.staff.mute.config.MuteConfiguration;
import net.shortninja.staffplus.staff.mute.config.MuteModuleLoader;
import net.shortninja.staffplus.staff.protect.config.ProtectConfiguration;
import net.shortninja.staffplus.staff.protect.config.ProtectModuleLoader;
import net.shortninja.staffplus.staff.reporting.config.ManageReportConfiguration;
import net.shortninja.staffplus.staff.reporting.config.ManageReportingModuleLoader;
import net.shortninja.staffplus.staff.reporting.config.ReportConfiguration;
import net.shortninja.staffplus.staff.reporting.config.ReportingModuleLoader;
import net.shortninja.staffplus.staff.staffchat.config.StaffChatConfiguration;
import net.shortninja.staffplus.staff.staffchat.config.StaffChatModuleLoader;
import net.shortninja.staffplus.staff.teleport.config.LocationLoader;
import net.shortninja.staffplus.staff.tracing.config.TraceConfiguration;
import net.shortninja.staffplus.staff.tracing.config.TraceModuleLoader;
import net.shortninja.staffplus.staff.warn.config.WarningConfiguration;
import net.shortninja.staffplus.staff.warn.config.WarningModuleLoader;
import net.shortninja.staffplus.unordered.VanishType;
import net.shortninja.staffplus.unordered.altdetect.AltDetectTrustLevel;
import net.shortninja.staffplus.util.Materials;
import net.shortninja.staffplus.util.lib.JavaUtils;
import net.shortninja.staffplus.util.lib.Sounds;
import net.shortninja.staffplus.util.lib.hex.Items;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

//TODO: replace this with something that isn't horribly coupled...
public class Options implements IOptions {

    public List<String> blockedCommands;
    public List<String> blockedModeCommands;
    public String glassTitle;

    public String mainWorld;
    public int autoSave;
    public long clock;
    private List<String> soundNames;
    public boolean offlinePlayersModeEnabled;

    public Map<String, Location> locations;
    public AuthenticationConfiguration authenticationConfiguration;
    public InfractionsConfiguration infractionsConfiguration;
    public ReportConfiguration reportConfiguration;
    public ManageReportConfiguration manageReportConfiguration;
    public WarningConfiguration warningConfiguration;
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

    /*
     * Vanish
     */
    public boolean vanishEnabled;
    public boolean vanishTabList;
    public boolean vanishShowAway;
    public boolean vanishChatEnabled;
    public boolean vanishMessageEnabled;
    /*
     * Chat
     */
    public boolean chatEnabled;
    public int chatLines;
    public int chatSlow;
    public boolean chatBlacklistEnabled;
    public boolean chatBlacklistHoverable;
    public String chatBlacklistCharacter;
    public List<String> chatBlacklistWords;
    public List<String> chatBlacklistCharacters;
    public List<String> chatBlacklistDomains;
    public List<String> chatBlacklistPeriods;
    public List<String> chatBlacklistAllowed;
    /*
     * Alerts
     */
    public boolean alertsNameNotify;
    public boolean alertsMentionNotify;
    public boolean alertsXrayEnabled;
    public boolean alertsAltDetectEnabled;
    public List<AltDetectTrustLevel> alertsAltDetectTrustLevels;
    /*
     * Staff Mode
     */
    public boolean modeBlockManipulation;
    public boolean modeInventoryInteraction;
    public boolean modeSilentChestInteraction;
    public boolean modeInvincible;
    public boolean modeFlight;
    public boolean modeCreative;
    public boolean modeOriginalLocation;
    public boolean modeEnableOnLogin;
    public boolean staffView;
    /*
     * Compass
     */
    public boolean modeCompassEnabled;
    public int modeCompassSlot;
    public int modeCompassVelocity;
    /*
     * Random Teleport
     */
    public boolean modeRandomTeleportEnabled;
    public int modeRandomTeleportSlot;
    public boolean modeRandomTeleportRandom;
    /*
     * Vanish
     */
    public boolean modeVanishEnabled;
    public int modeVanishSlot;

    /*
     * GUI Hub
     */
    public boolean modeGuiEnabled;
    public int modeGuiSlot;
    public boolean modeGuiMiner;
    public String modeGuiMinerTitle;
    public String modeGuiMinerName;
    public String modeGuiMinerLore;
    public int modeGuiMinerLevel;
    /*
     * Counter
     */
    public boolean modeCounterEnabled;
    public int modeCounterSlot;
    public String modeCounterTitle;
    public boolean modeCounterShowStaffMode;
    /*
     * Freeze
     */
    public boolean modeFreezeEnabled;
    public int modeFreezeSlot;
    public boolean modeFreezeChat;
    public boolean modeFreezeDamage;
    /*
     * CPS
     */
    public boolean modeCpsEnabled;
    public int modeCpsSlot;
    public long modeCpsTime;
    public int modeCpsMax;
    /*
     * Examine
     */

    public boolean modeExamineEnabled;
    public int modeExamineSlot;
    public String modeExamineTitle;
    public int modeExamineFood;
    public int modeExamineIp;
    public int modeExamineGamemode;
    public int modeExamineInfractions;
    public int modeExamineLocation;
    public int modeExamineNotes;
    public int modeExamineFreeze;
    public int modeExamineWarn;
    /*
     * Follow
     */
    public boolean modeFollowEnabled;
    public int modeFollowSlot;
    /*
     * Custom
     */
    public Map<String, ModuleConfiguration> moduleConfigurations;
    /*
     * Permissions
     */
    public String permissionAlerts;
    ;
    public String permissionWildcard;
    public String permissionBlock;
    public String permissionReport;
    public String permissionReportBypass;
    public String permissionReportUpdateNotifications;
    public String permissionWarn;
    public String permissionWarnBypass;
    public String permissionVanishTotal;
    public String permissionVanishList;
    public String permissionChatClear;
    public String permissionChatToggle;
    public String permissionChatSlow;
    public String permissionBlacklist;
    public String permissionMention;
    public String permissionAlertsAltDetect;
    public String permissionNameChange;
    public String permissionXray;
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
    public String commandWarn;
    public String commandWarns;
    public String commandVanish;
    public String commandChat;
    public String commandAlerts;
    public String commandFollow;
    public String commandRevive;
    public String commandStaffList;
    public String commandClearInv;
    public String commandTrace;
    public String commandBroadcast;

    public Sounds alertsSound;
    public List<XrayBlockConfig> alertsXrayBlocks;
    public VanishType modeVanish;

    public boolean modeItemChange;
    public boolean modeDamage;
    public boolean modeHungerLoss;
    public List<String> modeEnableCommands;
    public List<String> modeDisableCommands;
    public boolean worldChange;
    public int modeFreezeTimer;
    public Sounds modeFreezeSound;
    public boolean modeFreezePrompt;
    public String modeFreezePromptTitle;
    public List<String> logoutCommands;
    public String permissionStrip;
    public String permissionStaff;
    public String commandNotes;
    public String commandLogin;
    public String commandStrip;

    public ItemStack modeCompassItem;
    public ItemStack modeRandomTeleportItem;
    public ItemStack modeVanishItem;
    public ItemStack modeVanishItemOff;
    public ItemStack modeGuiItem;
    public ItemStack modeCounterItem;
    public ItemStack modeFreezeItem;
    public ItemStack modeCpsItem;
    public ItemStack modeExamineItem;

    /*
     * Storage
     */
    public String storageType;
    public String mySqlHost;
    public String mySqlUser;
    public String database;
    public String mySqlPassword;
    public int mySqlPort;

    public ItemStack modeFollowItem;


    public Options() {
        reload();
    }

    public void reload() {

        StaffPlus.get().reloadConfig();
        FileConfiguration config = StaffPlus.get().getConfig();
        blockedCommands = JavaUtils.stringToList(config.getString("blocked-commands", ""));
        blockedModeCommands = JavaUtils.stringToList(config.getString("blocked-mode-commands", ""));
        glassTitle = config.getString("glass-title");

        mainWorld = config.getString("main-world");
        autoSave = config.getInt("auto-save");
        clock = config.getInt("clock") * 20;
        soundNames = JavaUtils.stringToList(config.getString("sound-names"));
        offlinePlayersModeEnabled = config.getBoolean("offline-players-mode");

        locations = new LocationLoader().loadConfig();
        authenticationConfiguration = new AuthenticationConfigurationLoader().loadConfig();
        infractionsConfiguration = new InfractionsModuleLoader().loadConfig();
        reportConfiguration = new ReportingModuleLoader().loadConfig();
        manageReportConfiguration = new ManageReportingModuleLoader().loadConfig();
        warningConfiguration = new WarningModuleLoader().loadConfig();
        blackListConfiguration = new BlackListConfigurationLoader().loadConfig();
        traceConfiguration = new TraceModuleLoader().loadConfig();
        broadcastConfiguration = new BroadcastConfigurationLoader().loadConfig();
        protectConfiguration = new ProtectModuleLoader().loadConfig();
        banConfiguration = new BanModuleLoader().loadConfig();
        kickConfiguration = new KickModuleLoader().loadConfig();
        muteConfiguration = new MuteModuleLoader().loadConfig();
        altDetectConfiguration = new AltDetectModuleLoader().loadConfig();
        staffChatConfiguration = new StaffChatModuleLoader().loadConfig();
        examineConfiguration = new ExamineModuleLoader().loadConfig();
        enderchestsConfiguration = new EnderchestsModuleLoader().loadConfig();

        /*
         * Vanish
         */
        vanishEnabled = config.getBoolean("vanish-module.enabled");
        vanishTabList = config.getBoolean("vanish-module.tab-list");
        vanishShowAway = config.getBoolean("vanish-module.show-away");
        vanishChatEnabled = config.getBoolean("vanish-module.chat");
        vanishMessageEnabled = config.getBoolean("vanish-module.vanish-message-enabled");
        /*
         * Chat
         */
        chatEnabled = config.getBoolean("chat-module.enabled");
        chatLines = config.getInt("chat-module.lines");
        chatSlow = config.getInt("chat-module.slow");
        chatBlacklistEnabled = config.getBoolean("chat-module.blacklist-module.enabled");
        chatBlacklistHoverable = config.getBoolean("chat-module.blacklist-module.hoverable");
        chatBlacklistCharacter = config.getString("chat-module.blacklist-module.character");
        chatBlacklistWords = JavaUtils.stringToList(config.getString("chat-module.blacklist-module.words"));
        chatBlacklistCharacters = JavaUtils.stringToList(config.getString("chat-module.blacklist-module.characters"));
        chatBlacklistDomains = JavaUtils.stringToList(config.getString("chat-module.blacklist-module.domains"));
        chatBlacklistPeriods = JavaUtils.stringToList(config.getString("chat-module.blacklist-module.periods"));
        chatBlacklistAllowed = JavaUtils.stringToList(config.getString("chat-module.blacklist-module.allowed"));
        /*
         * Alerts
         */
        alertsNameNotify = config.getBoolean("alerts-module.name-notify");
        alertsMentionNotify = config.getBoolean("alerts-module.mention-notify");
        alertsXrayEnabled = config.getBoolean("alerts-module.xray-alerts.enabled");
        alertsAltDetectEnabled = config.getBoolean("alerts-module.alt-detect-notify.enabled");
        alertsAltDetectTrustLevels = Arrays.stream(config.getString("alerts-module.alt-detect-notify.trust-levels", "").split(";"))
            .map(AltDetectTrustLevel::valueOf)
            .collect(Collectors.toList());
        /*
         * Staff Mode
         */
        modeBlockManipulation = config.getBoolean("staff-mode.block-manipulation");
        modeInventoryInteraction = config.getBoolean("staff-mode.inventory-interaction");
        modeSilentChestInteraction = config.getBoolean("staff-mode.silent-chest-interaction");
        modeInvincible = config.getBoolean("staff-mode.invincible");
        modeFlight = config.getBoolean("staff-mode.flight");
        modeCreative = config.getBoolean("staff-mode.creative");
        modeOriginalLocation = config.getBoolean("staff-mode.original-location");
        modeEnableOnLogin = config.getBoolean("staff-mode.enable-on-login");
        staffView = config.getBoolean("staff-mode.staff-see-staff-in-mode");
        /*
         * Compass
         */
        modeCompassEnabled = config.getBoolean("staff-mode.compass-module.enabled");
        modeCompassSlot = config.getInt("staff-mode.compass-module.slot") - 1;
        modeCompassVelocity = config.getInt("staff-mode.compass-module.velocity");
        /*
         * Random Teleport
         */
        modeRandomTeleportEnabled = config.getBoolean("staff-mode.random-teleport-module.enabled");
        modeRandomTeleportSlot = config.getInt("staff-mode.random-teleport-module.slot") - 1;
        modeRandomTeleportRandom = config.getBoolean("staff-mode.random-teleport-module.random");
        /*
         * Vanish
         */
        modeVanishEnabled = config.getBoolean("staff-mode.vanish-module.enabled");
        modeVanishSlot = config.getInt("staff-mode.vanish-module.slot") - 1;

        /*
         * GUI Hub
         */
        modeGuiEnabled = config.getBoolean("staff-mode.gui-module.enabled");
        modeGuiSlot = config.getInt("staff-mode.gui-module.slot") - 1;
        modeGuiMiner = config.getBoolean("staff-mode.gui-module.miner-gui");
        modeGuiMinerTitle = config.getString("staff-mode.gui-module.miner-title");
        modeGuiMinerName = config.getString("staff-mode.gui-module.miner-name");
        modeGuiMinerLore = config.getString("staff-mode.gui-module.miner-lore");
        modeGuiMinerLevel = config.getInt("staff-mode.gui-module.xray-level");
        /*
         * Counter
         */
        modeCounterEnabled = config.getBoolean("staff-mode.counter-module.enabled");
        modeCounterSlot = config.getInt("staff-mode.counter-module.slot") - 1;
        modeCounterTitle = config.getString("staff-mode.counter-module.title");
        modeCounterShowStaffMode = config.getBoolean("staff-mode.counter-module.show-staff-mode");
        /*
         * Freeze
         */
        modeFreezeEnabled = config.getBoolean("staff-mode.freeze-module.enabled");
        modeFreezeSlot = config.getInt("staff-mode.freeze-module.slot") - 1;
        modeFreezeChat = config.getBoolean("staff-mode.freeze-module.chat");
        modeFreezeDamage = config.getBoolean("staff-mode.freeze-module.damage");
        /*
         * CPS
         */
        modeCpsEnabled = config.getBoolean("staff-mode.cps-module.enabled");
        modeCpsSlot = config.getInt("staff-mode.cps-module.slot") - 1;
        modeCpsTime = config.getInt("staff-mode.cps-module.time") * 20;
        modeCpsMax = config.getInt("staff-mode.cps-module.max");

        /*
         * Examine
         */
        modeExamineEnabled = config.getBoolean("staff-mode.examine-module.enabled");
        modeExamineSlot = config.getInt("staff-mode.examine-module.slot") - 1;
        modeExamineTitle = config.getString("staff-mode.examine-module.title");
        modeExamineFood = config.getInt("staff-mode.examine-module.info-line.food") <= 0 ? -1 : config.getInt("staff-mode.examine-module.info-line.food");
        modeExamineIp = config.getInt("staff-mode.examine-module.info-line.ip-address") <= 0 ? -1 : config.getInt("staff-mode.examine-module.info-line.ip-address");
        modeExamineGamemode = config.getInt("staff-mode.examine-module.info-line.gamemode") <= 0 ? -1 : config.getInt("staff-mode.examine-module.info-line.gamemode");
        modeExamineInfractions = config.getInt("staff-mode.examine-module.info-line.infractions") <= 0 ? -1 : config.getInt("staff-mode.examine-module.info-line.infractions");
        modeExamineLocation = config.getInt("staff-mode.examine-module.info-line.location") <= 0 ? -1 : config.getInt("staff-mode.examine-module.info-line.location");
        modeExamineNotes = config.getInt("staff-mode.examine-module.info-line.notes") <= 0 ? -1 : config.getInt("staff-mode.examine-module.info-line.notes");
        modeExamineFreeze = config.getInt("staff-mode.examine-module.info-line.freeze") <= 0 ? -1 : config.getInt("staff-mode.examine-module.info-line.freeze");
        modeExamineWarn = config.getInt("staff-mode.examine-module.info-line.warn") <= 0 ? -1 : config.getInt("staff-mode.examine-module.info-line.warn");
        /*
         * Follow
         */
        modeFollowEnabled = config.getBoolean("staff-mode.follow-module.enabled");
        modeFollowSlot = config.getInt("staff-mode.follow-module.slot") - 1;
        /*
         * Custom
         */
        moduleConfigurations = new HashMap<>();
        /*
         * Permissions
         */
        permissionAlerts = config.getString("permissions.alerts");
        ;
        permissionWildcard = config.getString("permissions.wild-card");
        permissionBlock = config.getString("permissions.block");
        permissionReport = config.getString("permissions.report");
        permissionReportBypass = config.getString("permissions.report-bypass");
        permissionReportUpdateNotifications = config.getString("permissions.report-update-notifications");
        permissionWarn = config.getString("permissions.warn");
        permissionWarnBypass = config.getString("permissions.warn-bypass");
        permissionVanishTotal = config.getString("permissions.vanish-total");
        permissionVanishList = config.getString("permissions.vanish-list");
        permissionChatClear = config.getString("permissions.chat-clear");
        permissionChatToggle = config.getString("permissions.chat-toggle");
        permissionChatSlow = config.getString("permissions.chat-slow");
        permissionBlacklist = config.getString("permissions.blacklist");
        permissionMention = config.getString("permissions.mention");
        permissionAlertsAltDetect = config.getString("permissions.alerts-alt-detect");
        permissionNameChange = config.getString("permissions.name-change");
        permissionXray = config.getString("permissions.xray");
        permissionMode = config.getString("permissions.mode");
        permissionModeSilentChestInteraction = config.getString("permissions.mode.silent-chest-interaction");
        permissionFreeze = config.getString("permissions.freeze");
        permissionFreezeBypass = config.getString("permissions.freeze-bypass");
        permissionTeleportToLocation = config.getString("permissions.teleport-to-location");
        permissionTeleportToPlayer = config.getString("permissions.teleport-to-player");
        permissionTeleportHere = config.getString("permissions.teleport-here");
        permissionTeleportBypass = config.getString("permissions.teleport-bypass");
        permissionTrace = config.getString("permissions.trace");
        permissionTraceBypass = config.getString("permissions.trace-bypass");
        permissionCps = config.getString("permissions.cps");
        permissionFollow = config.getString("permissions.follow");
        permissionRevive = config.getString("permissions.revive");
        permissionMember = config.getString("permissions.member");
        ipHidePerm = config.getString("permissions.ipPerm");
        permissionClearInv = config.getString("permissions.invClear");
        permissionClearInvBypass = config.getString("permissions.invClear-bypass");
        permissionBroadcast = config.getString("permissions.broadcast");
        permissionCounterGuiShowVanish = config.getString("permissions.counter-show-vanished");

        /*
         * Commands
         */
        commandStaffMode = config.getString("commands.staff-mode");
        commandFreeze = config.getString("commands.freeze");
        commandTeleportToLocation = config.getString("commands.teleport-to-location");
        commandTeleportBack = config.getString("commands.teleport-back");
        commandTeleportToPlayer = config.getString("commands.teleport-to-player");
        commandTeleportHere = config.getString("commands.teleport-here");
        commandCps = config.getString("commands.cps");
        commandStaffChat = config.getString("commands.staff-chat");
        commandReport = config.getString("commands.report");
        commandReportPlayer = config.getString("commands.reportPlayer");
        commandReports = config.getString("commands.reports.manage.cli");
        commandWarn = config.getString("commands.warn");
        commandWarns = config.getString("commands.warns");
        commandVanish = config.getString("commands.vanish");
        commandChat = config.getString("commands.chat");
        commandAlerts = config.getString("commands.alerts");
        commandFollow = config.getString("commands.follow");
        commandRevive = config.getString("commands.revive");
        commandStaffList = config.getString("commands.staff-list");
        commandClearInv = config.getString("commands.clearInv");
        commandTrace = config.getString("commands.trace");
        commandBroadcast = config.getString("commands.broadcast");

        alertsSound = stringToSound(sanitize(config.getString("alerts-module.sound")));
        alertsXrayBlocks = Arrays.stream(config.getString("alerts-module.xray-alerts.blocks").split("\\s*,\\s*"))
            .map(XrayBlockConfig::new)
            .collect(Collectors.toList());

        modeVanish = stringToVanishType(config.getString("staff-mode.vanish-type"));

        modeItemChange = config.getBoolean("staff-mode.item-change");
        modeDamage = config.getBoolean("staff-mode.damage");
        modeHungerLoss = config.getBoolean("staff-mode.hunger-loss");
        modeEnableCommands = JavaUtils.stringToList(config.getString("staff-mode.enable-commands"));
        modeDisableCommands = JavaUtils.stringToList(config.getString("staff-mode.disable-commands"));
        worldChange = config.getBoolean("staff-mode.disable-on-world-change");
        modeFreezeTimer = config.getInt("staff-mode.freeze-module.timer");
        modeFreezeSound = stringToSound(sanitize(config.getString("staff-mode.freeze-module.sound")));
        modeFreezePrompt = config.getBoolean("staff-mode.freeze-module.prompt");
        modeFreezePromptTitle = config.getString("staff-mode.freeze-module.prompt-title");
        logoutCommands = JavaUtils.stringToList(config.getString("staff-mode.freeze-module.logout-commands"));
        permissionStrip = config.getString("permissions.strip");
        permissionStaff = config.getString("permissions.staffplus");
        commandNotes = config.getString("commands.notes");
        commandLogin = config.getString("commands.login");
        commandStrip = config.getString("commands.strip");

        Material modeCompassType = stringToMaterial(sanitize(config.getString("staff-mode.compass-module.item")));
        short modeCompassData = getMaterialData(config.getString("staff-mode.compass-module.item"));
        String modeCompassName = config.getString("staff-mode.compass-module.name");
        List<String> modeCompassLore = JavaUtils.stringToList(config.getString("staff-mode.compass-module.lore"));
        modeCompassItem = Items.builder().setMaterial(modeCompassType).setData(modeCompassData).setName(modeCompassName).setLore(modeCompassLore).build();

        Material modeRandomTeleportType = stringToMaterial(sanitize(config.getString("staff-mode.random-teleport-module.item")));
        short modeRandomTeleportData = getMaterialData(config.getString("staff-mode.random-teleport-module.item"));
        String modeRandomTeleportName = config.getString("staff-mode.random-teleport-module.name");
        List<String> modeRandomTeleportLore = JavaUtils.stringToList(config.getString("staff-mode.random-teleport-module.lore"));
        modeRandomTeleportItem = Items.builder().setMaterial(modeRandomTeleportType).setData(modeRandomTeleportData).setName(modeRandomTeleportName).setLore(modeRandomTeleportLore).build();

        Material modeVanishType = stringToMaterial(sanitize(config.getString("staff-mode.vanish-module.item")));
        short modeVanishData = getMaterialData(config.getString("staff-mode.vanish-module.item"));
        String modeVanishName = config.getString("staff-mode.vanish-module.name");
        List<String> modeVanishLore = JavaUtils.stringToList(config.getString("staff-mode.vanish-module.lore"));
        modeVanishItem = Items.builder().setMaterial(modeVanishType).setData(modeVanishData).setName(modeVanishName).setLore(modeVanishLore).build();

        Material modeVanishTypeOff = stringToMaterial(sanitize(config.getString("staff-mode.vanish-module.item-off")));
        short modeVanishDataOff = getMaterialData(config.getString("staff-mode.vanish-module.item-off"));
        modeVanishItemOff = Items.builder().setMaterial(modeVanishTypeOff).setData(modeVanishDataOff).setName(modeVanishName).setLore(modeVanishLore).build();

        Material modeGuiType = stringToMaterial(sanitize(config.getString("staff-mode.gui-module.item")));
        short modeGuiData = getMaterialData("staff-mode.gui-module.item");
        String modeGuiName = config.getString("staff-mode.gui-module.name");
        List<String> modeGuiLore = JavaUtils.stringToList(config.getString("staff-mode.gui-module.lore"));
        modeGuiItem = Items.builder().setMaterial(modeGuiType).setData(modeGuiData).setName(modeGuiName).setLore(modeGuiLore).build();

        Material modeCounterType = stringToMaterial(sanitize(config.getString("staff-mode.counter-module.item")));
        short modeCounterData = getMaterialData(config.getString("staff-mode.counter-module.item"));
        String modeCounterName = config.getString("staff-mode.counter-module.name");
        List<String> modeCounterLore = JavaUtils.stringToList(config.getString("staff-mode.counter-module.lore"));
        modeCounterItem = Items.builder().setMaterial(modeCounterType).setData(modeCounterData).setName(modeCounterName).setLore(modeCounterLore).build();

        Material modeFreezeType = stringToMaterial(sanitize(config.getString("staff-mode.freeze-module.item")));
        short modeFreezeData = getMaterialData(config.getString("staff-mode.freeze-module.item"));
        String modeFreezeName = config.getString("staff-mode.freeze-module.name");
        List<String> modeFreezeLore = JavaUtils.stringToList(config.getString("staff-mode.freeze-module.lore"));
        modeFreezeItem = Items.builder().setMaterial(modeFreezeType).setData(modeFreezeData).setName(modeFreezeName).setLore(modeFreezeLore).build();

        Material modeCpsType = stringToMaterial(sanitize(config.getString("staff-mode.cps-module.item")));
        short modeCpsData = getMaterialData(config.getString("staff-mode.cps-module.item"));
        String modeCpsName = config.getString("staff-mode.cps-module.name");
        List<String> modeCpsLore = JavaUtils.stringToList(config.getString("staff-mode.cps-module.lore"));
        modeCpsItem = Items.builder().setMaterial(modeCpsType).setData(modeCpsData).setName(modeCpsName).setLore(modeCpsLore).build();

        Material modeExamineType = stringToMaterial(sanitize(config.getString("staff-mode.examine-module.item")));
        short modeExamineData = getMaterialData(config.getString("staff-mode.examine-module.item"));
        String modeExamineName = config.getString("staff-mode.examine-module.name");
        List<String> modeExamineLore = JavaUtils.stringToList(config.getString("staff-mode.examine-module.lore"));
        modeExamineItem = Items.builder().setMaterial(modeExamineType).setData(modeExamineData).setName(modeExamineName).setLore(modeExamineLore).build();


        /*
         * Storage
         */
        storageType = config.getString("storage.type");
        mySqlHost = config.getString("storage.mysql.host");
        mySqlUser = config.getString("storage.mysql.user");
        database = config.getString("storage.mysql.database");
        mySqlPassword = config.getString("storage.mysql.password");
        mySqlPort = config.getInt("storage.mysql.port");

        Material modeFollowType = stringToMaterial(sanitize(config.getString("staff-mode.follow-module.item")));
        short modeFollowData = getMaterialData(config.getString("staff-mode.follow-module.item"));
        String modeFollowName = config.getString("staff-mode.follow-module.name");
        List<String> modeFollowLore = JavaUtils.stringToList(config.getString("staff-mode.follow-module.lore"));
        modeFollowItem = Items.builder().setMaterial(modeFollowType).setData(modeFollowData).setName(modeFollowName).setLore(modeFollowLore).build();


        loadCustomModules(config);
    }

    private void loadCustomModules(FileConfiguration config) {
        for (String identifier : config.getConfigurationSection("staff-mode.custom-modules").getKeys(false)) {
            if (!config.getBoolean("staff-mode.custom-modules." + identifier + ".enabled")) {
                continue;
            }

            ModuleConfiguration.ModuleType moduleType = stringToModuleType(sanitize(config.getString("staff-mode.custom-modules." + identifier + ".type")));
            int slot = config.getInt("staff-mode.custom-modules." + identifier + ".slot") - 1;
            Material type = stringToMaterial(sanitize(config.getString("staff-mode.custom-modules." + identifier + ".item")));
            short data = getMaterialData(config.getString("staff-mode.custom-modules." + identifier + ".item"));
            String name = config.getString("staff-mode.custom-modules." + identifier + ".name");
            List<String> lore = JavaUtils.stringToList(config.getString("staff-mode.custom-modules." + identifier + ".lore"));
            ItemStack item = Items.builder().setMaterial(type).setData(data).setName(name).setLore(lore).build();
            String action = "";


            if (!config.getString("staff-mode.custom-modules." + identifier + ".enchantment", "").equalsIgnoreCase("")) {
                String enchantInfo = config.getString("staff-mode.custom-modules." + identifier + ".enchantment");
                String[] enchantInfoParts = enchantInfo.split(":");
                Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchantInfoParts[0]));
                if (enchantment == null) {
                    enchantment = Enchantment.DURABILITY;
                }
                int level = Integer.parseInt(enchantInfoParts[1]);
                item = Items.builder().setMaterial(type).setData(data).setName(name).setLore(lore)
                    .addEnchantment(enchantment, level).build();
            } else
                item = Items.builder().setMaterial(type).setData(data).setName(name).setLore(lore).build();


            if (moduleType != ModuleConfiguration.ModuleType.ITEM) {
                action = config.getString("staff-mode.custom-modules." + identifier + ".command");
            }

            moduleConfigurations.put(identifier, new ModuleConfiguration(identifier, moduleType, slot, item, action));
        }
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

    private List<Material> stringToMaterialList(String commas) {
        List<Material> list = new ArrayList<>();

        for (String s : commas.split("\\s*,\\s*")) {
            list.add(stringToMaterial(sanitize(s)));
        }

        return list;
    }

    private short getMaterialData(String string) {
        short data = 0;

        if (string.contains(":")) {
            String dataString = string.substring(string.lastIndexOf(':') + 1);

            if (JavaUtils.isInteger(dataString)) {
                data = (short) Integer.parseInt(dataString);
            } else
                Bukkit.getLogger().severe("Invalid material data '" + dataString + "' from '" + string + "'!");
        }

        return data;
    }

    private Sounds stringToSound(String string) {
        Sounds sound = Sounds.ORB_PICKUP;
        boolean isValid = JavaUtils.isValidEnum(Sounds.class, string);

        if (!isValid) {

            Bukkit.getLogger().severe("Invalid sound name '" + string + "'!");
        } else sound = Sounds.valueOf(string);

        return sound;
    }

    private VanishType stringToVanishType(String string) {
        VanishType vanishType = VanishType.NONE;
        boolean isValid = JavaUtils.isValidEnum(VanishType.class, string);

        if (!isValid) {
            Bukkit.getLogger().severe("Invalid vanish type '" + string + "'!");
        } else vanishType = VanishType.valueOf(string);

        return vanishType;
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

    private ModuleConfiguration.ModuleType stringToModuleType(String string) {
        ModuleConfiguration.ModuleType moduleType = ModuleConfiguration.ModuleType.ITEM;
        boolean isValid = JavaUtils.isValidEnum(ModuleConfiguration.ModuleType.class, string);

        if (!isValid) {
            Bukkit.getLogger().severe("Invalid module type '" + string + "'!");
        } else moduleType = ModuleConfiguration.ModuleType.valueOf(string);

        return moduleType;
    }

    private String sanitize(String string) {
        if (string.contains(":")) {
            string = string.replace(string.substring(string.lastIndexOf(':')), "");
        }

        return string.toUpperCase();
    }

    // TODO: Remove these when replaced with better solution.....
    @Override
    public List<String> getSoundNames() {
        return soundNames;
    }
}