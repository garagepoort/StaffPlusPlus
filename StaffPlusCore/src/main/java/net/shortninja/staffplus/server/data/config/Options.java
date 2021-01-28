package net.shortninja.staffplus.server.data.config;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.authentication.AuthenticationConfiguration;
import net.shortninja.staffplus.authentication.AuthenticationConfigurationLoader;
import net.shortninja.staffplus.server.chat.blacklist.BlackListConfiguration;
import net.shortninja.staffplus.server.chat.blacklist.BlackListConfigurationLoader;
import net.shortninja.staffplus.server.synchronization.ServerSyncConfiguration;
import net.shortninja.staffplus.server.synchronization.ServerSyncModuleLoader;
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
import net.shortninja.staffplus.staff.mode.config.GeneralModeConfiguration;
import net.shortninja.staffplus.staff.mode.config.StaffModeModuleLoader;
import net.shortninja.staffplus.staff.mode.item.CustomModuleConfiguration;
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

    public String serverName;
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
    public GeneralModeConfiguration modeConfiguration;
    public ServerSyncConfiguration serverSyncConfiguration;

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
    public boolean staffView;

    /*
     * Custom
     */
    public List<CustomModuleConfiguration> customModuleConfigurations;
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
    public String permissionVanishCommand;
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

    public Options() {
        reload();
    }

    public void reload() {

        StaffPlus.get().reloadConfig();
        FileConfiguration config = StaffPlus.get().getConfig();
        blockedCommands = JavaUtils.stringToList(config.getString("blocked-commands", ""));
        blockedModeCommands = JavaUtils.stringToList(config.getString("blocked-mode-commands", ""));
        glassTitle = config.getString("glass-title");

        serverName = config.getString("server-name");
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
        modeConfiguration = new StaffModeModuleLoader().loadConfig();
        serverSyncConfiguration = new ServerSyncModuleLoader().loadConfig();

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
        staffView = config.getBoolean("staff-mode.staff-see-staff-in-mode");

        /*
         * Custom
         */
        customModuleConfigurations = new ArrayList<>();
        /*
         * Permissions
         */
        permissionStaff = config.getString("permissions.staffplus");
        permissionAlerts = config.getString("permissions.alerts");
        permissionStrip = config.getString("permissions.strip");
        permissionWildcard = config.getString("permissions.wild-card");
        permissionBlock = config.getString("permissions.block");
        permissionReport = config.getString("permissions.report");
        permissionReportBypass = config.getString("permissions.report-bypass");
        permissionReportUpdateNotifications = config.getString("permissions.report-update-notifications");
        permissionWarn = config.getString("permissions.warn");
        permissionWarnBypass = config.getString("permissions.warn-bypass");
        permissionVanishCommand = config.getString("permissions.vanish");
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
        permissionModeSilentChestInteraction = config.getString("permissions.mode-silent-chest-interaction");
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
        commandNotes = config.getString("commands.notes");
        commandLogin = config.getString("commands.login");
        commandStrip = config.getString("commands.strip");

        alertsSound = stringToSound(sanitize(config.getString("alerts-module.sound")));
        alertsXrayBlocks = Arrays.stream(config.getString("alerts-module.xray-alerts.blocks").split("\\s*,\\s*"))
            .map(XrayBlockConfig::new)
            .collect(Collectors.toList());

        /*
         * Storage
         */
        storageType = config.getString("storage.type");
        mySqlHost = config.getString("storage.mysql.host");
        mySqlUser = config.getString("storage.mysql.user");
        database = config.getString("storage.mysql.database");
        mySqlPassword = config.getString("storage.mysql.password");
        mySqlPort = config.getInt("storage.mysql.port");

        loadCustomModules(config);
    }

    private void loadCustomModules(FileConfiguration config) {
        if(config.getConfigurationSection("staff-mode.custom-modules") == null) {
            StaffPlus.get().getLogger().info("No custom staff mode modules to load");
            return;
        }

        for (String identifier : config.getConfigurationSection("staff-mode.custom-modules").getKeys(false)) {
            boolean enabled = config.getBoolean("staff-mode.custom-modules." + identifier + ".enabled");
            if (!enabled) {
                continue;
            }

            CustomModuleConfiguration.ModuleType moduleType = stringToModuleType(sanitize(config.getString("staff-mode.custom-modules." + identifier + ".type")));
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


            if (moduleType != CustomModuleConfiguration.ModuleType.ITEM) {
                action = config.getString("staff-mode.custom-modules." + identifier + ".command");
            }

            customModuleConfigurations.add(new CustomModuleConfiguration(true, identifier, moduleType, slot, item, action));
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

    public static Material stringToMaterial(String string) {
        Material sound = Material.STONE;

        boolean isValid = JavaUtils.isValidEnum(Material.class, getMaterial(string));
        if (!isValid) {
            Bukkit.getLogger().severe("Invalid material type '" + string + "'!");
        } else
            sound = Material.valueOf(getMaterial(string));

        return sound;
    }

    private CustomModuleConfiguration.ModuleType stringToModuleType(String string) {
        CustomModuleConfiguration.ModuleType moduleType = CustomModuleConfiguration.ModuleType.ITEM;
        boolean isValid = JavaUtils.isValidEnum(CustomModuleConfiguration.ModuleType.class, string);

        if (!isValid) {
            Bukkit.getLogger().severe("Invalid module type '" + string + "'!");
        } else moduleType = CustomModuleConfiguration.ModuleType.valueOf(string);

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