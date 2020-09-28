package net.shortninja.staffplus.server.data.config;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.authentication.AuthenticationConfiguration;
import net.shortninja.staffplus.authentication.AuthenticationConfigurationLoader;
import net.shortninja.staffplus.staff.broadcast.config.BroadcastConfiguration;
import net.shortninja.staffplus.staff.broadcast.config.BroadcastConfigurationLoader;
import net.shortninja.staffplus.staff.mode.item.ModuleConfiguration;
import net.shortninja.staffplus.server.chat.blacklist.BlackListConfiguration;
import net.shortninja.staffplus.server.chat.blacklist.BlackListConfigurationLoader;
import net.shortninja.staffplus.staff.reporting.config.ReportConfiguration;
import net.shortninja.staffplus.staff.reporting.config.ReportingModuleLoader;
import net.shortninja.staffplus.staff.teleport.config.LocationLoader;
import net.shortninja.staffplus.staff.warn.config.WarningConfiguration;
import net.shortninja.staffplus.staff.warn.config.WarningModuleLoader;
import net.shortninja.staffplus.staff.tracing.config.TraceConfiguration;
import net.shortninja.staffplus.staff.tracing.config.TraceModuleLoader;
import net.shortninja.staffplus.unordered.VanishType;
import net.shortninja.staffplus.util.Materials;
import net.shortninja.staffplus.util.lib.JavaUtils;
import net.shortninja.staffplus.util.lib.Sounds;
import net.shortninja.staffplus.util.lib.hex.Items;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

//TODO: replace this with something that isn't horribly coupled...
public class Options implements IOptions {
    private static final FileConfiguration config = StaffPlus.get().getConfig();
    private final InputStream stream = StaffPlus.get().getResource("config.yml");

    YamlConfiguration configuration = YamlConfiguration.loadConfiguration(new InputStreamReader(stream, StandardCharsets.UTF_8));

    /*
     * General
     */
    public final String language = config.getString("lang");

    public final List<String> blockedCommands = JavaUtils.stringToList(config.getString("blocked-commands", ""));
    public final List<String> blockedModeCommands = JavaUtils.stringToList(config.getString("blocked-mode-commands", ""));
    public final short glassColor = (short) config.getInt("glass-color");
    public final String glassTitle = config.getString("glass-title");
    /*
     * Advanced
     */
    private final int configVersion = configuration.getInt("config-version");

    public final int autoSave = config.getInt("auto-save");
    public final long clock = config.getInt("clock") * 20;
    public final List<String> animationPackets = JavaUtils.stringToList(config.getString("animation-packets"));
    public final List<String> soundNames = JavaUtils.stringToList(config.getString("sound-names"));
    public final boolean offlinePlayersModeEnabled = config.getBoolean("offline-players-mode");

    public Map<String, Location> locations = new LocationLoader().load();
    public final AuthenticationConfiguration authenticationConfiguration = new AuthenticationConfigurationLoader().load();
    public final ReportConfiguration reportConfiguration = new ReportingModuleLoader().load();
    public final WarningConfiguration warningConfiguration = new WarningModuleLoader().load();
    public final BlackListConfiguration blackListConfiguration = new BlackListConfigurationLoader().load();
    public final TraceConfiguration traceConfiguration = new TraceModuleLoader().load();
    public final BroadcastConfiguration broadcastConfiguration = new BroadcastConfigurationLoader().load();

    /*
     * Staff Chat
     */
    public final boolean staffChatEnabled = config.getBoolean("staff-chat-module.enabled");
    public final String staffChatHandle = config.getString("staff-chat-module.handle");
    /*
     * Vanish
     */
    public final boolean vanishEnabled = config.getBoolean("vanish-module.enabled");
    public final boolean vanishTabList = config.getBoolean("vanish-module.tab-list");
    public final boolean vanishShowAway = config.getBoolean("vanish-module.show-away");
    public final boolean vanishSuggestionsEnabled = config.getBoolean("vanish-module.suggestions");
    public final boolean vanishChatEnabled = config.getBoolean("vanish-module.chat");
    /*
     * Chat
     */
    public final boolean chatEnabled = config.getBoolean("chat-module.enabled");
    public final int chatLines = config.getInt("chat-module.lines");
    public final int chatSlow = config.getInt("chat-module.slow");
    public final boolean chatBlacklistEnabled = config.getBoolean("chat-module.blacklist-module.enabled");
    public final boolean chatBlacklistHoverable = config.getBoolean("chat-module.blacklist-module.hoverable");
    public final String chatBlacklistCharacter = config.getString("chat-module.blacklist-module.character");
    public final List<String> chatBlacklistWords = JavaUtils.stringToList(config.getString("chat-module.blacklist-module.words"));
    public final List<String> chatBlacklistCharacters = JavaUtils.stringToList(config.getString("chat-module.blacklist-module.characters"));
    public final List<String> chatBlacklistDomains = JavaUtils.stringToList(config.getString("chat-module.blacklist-module.domains"));
    public final List<String> chatBlacklistPeriods = JavaUtils.stringToList(config.getString("chat-module.blacklist-module.periods"));
    public final List<String> chatBlacklistAllowed = JavaUtils.stringToList(config.getString("chat-module.blacklist-module.allowed"));
    /*
     * Alerts
     */
    public final boolean alertsNameNotify = config.getBoolean("alerts-module.name-notify");
    public final boolean alertsMentionNotify = config.getBoolean("alerts-module.mention-notify");
    public final boolean alertsXrayEnabled = config.getBoolean("alerts-module.xray-alerts.enabled");
    /*
     * Staff Mode
     */
    public final boolean modeBlockManipulation = config.getBoolean("staff-mode.block-manipulation");
    public final boolean modeInventoryInteraction = config.getBoolean("staff-mode.inventory-interaction");
    public final boolean modeInvincible = config.getBoolean("staff-mode.invincible");
    public final boolean modeFlight = config.getBoolean("staff-mode.flight");
    public final boolean modeCreative = config.getBoolean("staff-mode.creative");
    public final boolean modeOriginalLocation = config.getBoolean("staff-mode.original-location");
    public final boolean modeEnableOnLogin = config.getBoolean("staff-mode.enable-on-login");
    public final boolean staffView = config.getBoolean("staff-mode.staff-see-staff-in-mode");
    /*
     * Compass
     */
    public final boolean modeCompassEnabled = config.getBoolean("staff-mode.compass-module.enabled");
    public final int modeCompassSlot = config.getInt("staff-mode.compass-module.slot") - 1;
    public final int modeCompassVelocity = config.getInt("staff-mode.compass-module.velocity");
    /*
     * Random Teleport
     */
    public final boolean modeRandomTeleportEnabled = config.getBoolean("staff-mode.random-teleport-module.enabled");
    public final int modeRandomTeleportSlot = config.getInt("staff-mode.random-teleport-module.slot") - 1;
    public final boolean modeRandomTeleportRandom = config.getBoolean("staff-mode.random-teleport-module.random");
    /*
     * Vanish
     */
    public final boolean modeVanishEnabled = config.getBoolean("staff-mode.vanish-module.enabled");
    public final int modeVanishSlot = config.getInt("staff-mode.vanish-module.slot") - 1;

    /*
     * GUI Hub
     */
    public final boolean modeGuiEnabled = config.getBoolean("staff-mode.gui-module.enabled");
    public final int modeGuiSlot = config.getInt("staff-mode.gui-module.slot") - 1;
    public final boolean modeGuiReports = config.getBoolean("staff-mode.gui-module.reports-gui");
    public final String modeGuiReportsTitle = config.getString("staff-mode.gui-module.reports-title");
    public final String modeGuiMyReportsTitle = config.getString("staff-mode.gui-module.my-reports-title");
    public final String modeGuiClosedReportsTitle = config.getString("staff-mode.gui-module.closed-reports-title");
    public final String modeGuiReportsName = config.getString("staff-mode.gui-module.reports-name");
    public final String modeGuiReportsLore = config.getString("staff-mode.gui-module.reports-lore");
    public final String modeGuiMyReportsLore = config.getString("staff-mode.gui-module.my-reports-lore");
    public final String modeGuiClosedReportsLore = config.getString("staff-mode.gui-module.closed-reports-lore");
    public final boolean modeGuiMiner = config.getBoolean("staff-mode.gui-module.miner-gui");
    public final String modeGuiMinerTitle = config.getString("staff-mode.gui-module.miner-title");
    public final String modeGuiMinerName = config.getString("staff-mode.gui-module.miner-name");
    public final String modeGuiMinerLore = config.getString("staff-mode.gui-module.miner-lore");
    public final int modeGuiMinerLevel = config.getInt("staff-mode.gui-module.xray-level");
    /*
     * Counter
     */
    public final boolean modeCounterEnabled = config.getBoolean("staff-mode.counter-module.enabled");
    public final int modeCounterSlot = config.getInt("staff-mode.counter-module.slot") - 1;
    public final String modeCounterTitle = config.getString("staff-mode.counter-module.title");
    public final boolean modeCounterShowStaffMode = config.getBoolean("staff-mode.counter-module.show-staff-mode");
    /*
     * Freeze
     */
    public final boolean modeFreezeEnabled = config.getBoolean("staff-mode.freeze-module.enabled");
    public final int modeFreezeSlot = config.getInt("staff-mode.freeze-module.slot") - 1;
    public final boolean modeFreezeChat = config.getBoolean("staff-mode.freeze-module.chat");
    public final boolean modeFreezeDamage = config.getBoolean("staff-mode.freeze-module.damage");
    /*
     * CPS
     */
    public final boolean modeCpsEnabled = config.getBoolean("staff-mode.cps-module.enabled");
    public final int modeCpsSlot = config.getInt("staff-mode.cps-module.slot") - 1;
    public final long modeCpsTime = config.getInt("staff-mode.cps-module.time") * 20;
    public final int modeCpsMax = config.getInt("staff-mode.cps-module.max");
    /*
     * Examine
     */

    public final boolean enderChestEnabled = configVersion < 6204 || config.getBoolean("staff-mode.enderchest-module.enabled");
    public final boolean enderOfflineChestEnabled = configVersion < 6204 || config.getBoolean("staff-mode.enderchest-module.offline-viewing");
    public final boolean modeExamineEnabled = config.getBoolean("staff-mode.examine-module.enabled");
    public final int modeExamineSlot = config.getInt("staff-mode.examine-module.slot") - 1;
    public final String modeExamineTitle = config.getString("staff-mode.examine-module.title");
    public final int modeExamineFood = config.getInt("staff-mode.examine-module.info-line.food") <= 0 ? -1 : config.getInt("staff-mode.examine-module.info-line.food");
    public final int modeExamineIp = config.getInt("staff-mode.examine-module.info-line.ip-address") <= 0 ? -1 : config.getInt("staff-mode.examine-module.info-line.ip-address");
    public final int modeExamineGamemode = config.getInt("staff-mode.examine-module.info-line.gamemode") <= 0 ? -1 : config.getInt("staff-mode.examine-module.info-line.gamemode");
    public final int modeExamineInfractions = config.getInt("staff-mode.examine-module.info-line.infractions") <= 0 ? -1 : config.getInt("staff-mode.examine-module.info-line.infractions");
    public final int modeExamineLocation = config.getInt("staff-mode.examine-module.info-line.location") <= 0 ? -1 : config.getInt("staff-mode.examine-module.info-line.location");
    public final int modeExamineNotes = config.getInt("staff-mode.examine-module.info-line.notes") <= 0 ? -1 : config.getInt("staff-mode.examine-module.info-line.notes");
    public final int modeExamineFreeze = config.getInt("staff-mode.examine-module.info-line.freeze") <= 0 ? -1 : config.getInt("staff-mode.examine-module.info-line.freeze");
    public final int modeExamineWarn = config.getInt("staff-mode.examine-module.info-line.warn") <= 0 ? -1 : config.getInt("staff-mode.examine-module.info-line.warn");
    /*
     * Follow
     */
    public final boolean modeFollowEnabled = config.getBoolean("staff-mode.follow-module.enabled");
    public final int modeFollowSlot = config.getInt("staff-mode.follow-module.slot") - 1;
    /*
     * Custom
     */
    public Map<String, ModuleConfiguration> moduleConfigurations = new HashMap<>();
    /*
     * Permissions
     */
    public final String permissionAlerts = config.getString("permissions.alerts");;
    public final String permissionWildcard = config.getString("permissions.wild-card");
    public final String permissionBlock = config.getString("permissions.block");
    public final String permissionReport = config.getString("permissions.report");
    public final String permissionReportBypass = config.getString("permissions.report-bypass");
    public final String permissionWarn = config.getString("permissions.warn");
    public final String permissionWarnBypass = config.getString("permissions.warn-bypass");
    public final String permissionStaffChat = config.getString("permissions.staff-chat");
    public final String permissionVanishTotal = config.getString("permissions.vanish-total");
    public final String permissionVanishList = config.getString("permissions.vanish-list");
    public final String permissionChatClear = config.getString("permissions.chat-clear");
    public final String permissionChatToggle = config.getString("permissions.chat-toggle");
    public final String permissionChatSlow = config.getString("permissions.chat-slow");
    public final String permissionBlacklist = config.getString("permissions.blacklist");
    public final String permissionMention = config.getString("permissions.mention");
    public final String permissionNameChange = config.getString("permissions.name-change");
    public final String permissionXray = config.getString("permissions.xray");
    public final String permissionMode = config.getString("permissions.mode");
    public final String permissionFreeze = config.getString("permissions.freeze");
    public final String permissionFreezeBypass = config.getString("permissions.freeze-bypass");
    public final String permissionTeleport = config.getString("permissions.teleport");
    public final String permissionTeleportToPlayer = config.getString("permissions.teleport-to-player");
    public final String permissionTeleportBypass = config.getString("permissions.teleport-bypass");
    public final String permissionTrace = config.getString("permissions.trace");
    public final String permissionTraceBypass = config.getString("permissions.trace-bypass");
    public final String permissionCps = config.getString("permissions.cps");
    public final String permissionExamine = config.getString("permissions.examine");
    public final String permissionFollow = config.getString("permissions.follow");
    public final String permissionRevive = config.getString("permissions.revive");
    public final String permissionMember = config.getString("permissions.member");
    public final String ipHidePerm = config.getString("permissions.ipPerm");
    public final String permissionClearInv = config.getString("permissions.invClear");
    public final String permissionClearInvBypass = config.getString("permissions.invClear-bypass");
    public final String permissionBroadcast = config.getString("permission.broadcast");

    /*
     * Commands
     */
    public final String commandStaffMode = config.getString("commands.staff-mode");
    public final String commandFreeze = config.getString("commands.freeze");
    public final String commandTeleport = config.getString("commands.teleport");
    public final String commandTeleportToPlayer = config.getString("commands.teleport-to-player");
    public final String commandExamine = config.getString("commands.examine");
    public final String commandCps = config.getString("commands.cps");
    public final String commandStaffChat = config.getString("commands.staff-chat");
    public final String commandReport = config.getString("commands.report");
    public final String commandReportPlayer = config.getString("commands.reportPlayer");
    public final String commandReports = config.getString("commands.reports");
    public final String commandWarn = config.getString("commands.warn");
    public final String commandWarns = config.getString("commands.warns");
    public final String commandVanish = config.getString("commands.vanish");
    public final String commandChat = config.getString("commands.chat");
    public final String commandAlerts = config.getString("commands.alerts");
    public final String commandFollow = config.getString("commands.follow");
    public final String commandRevive = config.getString("commands.revive");
    public final String commandStaffList = config.getString("commands.staff-list");
    public final String commandClearInv = config.getString("commands.clearInv");
    public final String commandTrace = config.getString("commands.trace");
    public final String commandBroadcast = config.getString("commands.broadcast");
    public final String commandEChestView = configVersion >= 6203 ? config.getString("commands.echest_view") : "eview";

    public final Sounds alertsSound = stringToSound(sanitize(config.getString("alerts-module.sound")));
    public final List<Material> alertsXrayBlocks = stringToMaterialList(config.getString("alerts-module.xray-alerts.blocks"));
    public final VanishType modeVanish = stringToVanishType(config.getString("staff-mode.vanish-type"));
    public final boolean disablePackets = configVersion >= 3.19 && config.getBoolean("disable-packets");

    public final boolean modeItemChange = !(configVersion >= 3.1) || config.getBoolean("staff-mode.item-change");
    public final boolean modeDamage = configVersion >= 6194 && config.getBoolean("staff-mode.damage");
    public final boolean modeHungerLoss = !(configVersion >= 3.17) || config.getBoolean("staff-mode.hunger-loss");
    public final List<String> modeEnableCommands = configVersion >= 3.16 ? JavaUtils.stringToList(config.getString("staff-mode.enable-commands")) : new ArrayList<String>();
    public final List<String> modeDisableCommands = configVersion >= 3.16 ? JavaUtils.stringToList(config.getString("staff-mode.disable-commands")) : new ArrayList<String>();
    public final boolean worldChange = configVersion >= 6199 && config.getBoolean("staff-mode.disable-on-world-change");
    public final int modeFreezeTimer = configVersion >= 3.17 ? config.getInt("staff-mode.freeze-module.timer") : 0;
    public final Sounds modeFreezeSound = configVersion >= 3.17 ? stringToSound(sanitize(config.getString("staff-mode.freeze-module.sound"))) : Sounds.ORB_PICKUP;
    public final boolean modeFreezePrompt = configVersion >= 3.1 && config.getBoolean("staff-mode.freeze-module.prompt");
    public final String modeFreezePromptTitle = configVersion >= 3.1 ? config.getString("staff-mode.freeze-module.prompt-title") : "&bFrozen";
    public final List<String> logoutCommands = (configVersion >= 6195) ? JavaUtils.stringToList(config.getString("staff-mode.freeze-module.logout-commands")) : new ArrayList<>();
    public final String permissionStrip = configVersion >= 6194 ? config.getString("permissions.strip") : "staff.strip";
    public final String permissionStaff = configVersion >= 6196 ? config.getString("permissions.staffplus") : "staff.staffplus";
    public final String commandNotes = configVersion >= 3.1 ? config.getString("commands.notes") : "notes";
    public final String commandLogin = configVersion >= 3.2 ? config.getString("commands.login") : "login";
    public final String commandStrip = configVersion >= 6194 ? config.getString("commands.strip") : "strip";

    private final Material modeCompassType = stringToMaterial(sanitize(config.getString("staff-mode.compass-module.item")));
    private final short modeCompassData = getMaterialData(config.getString("staff-mode.compass-module.item"));
    private final String modeCompassName = config.getString("staff-mode.compass-module.name");
    private final List<String> modeCompassLore = JavaUtils.stringToList(config.getString("staff-mode.compass-module.lore"));
    public ItemStack modeCompassItem = Items.builder().setMaterial(modeCompassType).setData(modeCompassData).setName(modeCompassName).setLore(modeCompassLore).build();

    private final Material modeRandomTeleportType = stringToMaterial(sanitize(config.getString("staff-mode.random-teleport-module.item")));
    private final short modeRandomTeleportData = getMaterialData(config.getString("staff-mode.random-teleport-module.item"));
    private final String modeRandomTeleportName = config.getString("staff-mode.random-teleport-module.name");
    private final List<String> modeRandomTeleportLore = JavaUtils.stringToList(config.getString("staff-mode.random-teleport-module.lore"));
    public ItemStack modeRandomTeleportItem = Items.builder().setMaterial(modeRandomTeleportType).setData(modeRandomTeleportData).setName(modeRandomTeleportName).setLore(modeRandomTeleportLore).build();

    private final Material modeVanishType = stringToMaterial(sanitize(config.getString("staff-mode.vanish-module.item")));
    private final short modeVanishData = getMaterialData(config.getString("staff-mode.vanish-module.item"));
    private final String modeVanishName = config.getString("staff-mode.vanish-module.name");
    private final List<String> modeVanishLore = JavaUtils.stringToList(config.getString("staff-mode.vanish-module.lore"));
    public ItemStack modeVanishItem = Items.builder().setMaterial(modeVanishType).setData(modeVanishData).setName(modeVanishName).setLore(modeVanishLore).build();

    private final Material modeVanishTypeOff = stringToMaterial(sanitize(config.getString("staff-mode.vanish-module.item-off")));
    private final short modeVanishDataOff = getMaterialData(config.getString("staff-mode.vanish-module.item-off"));
    public ItemStack modeVanishItemOff = Items.builder().setMaterial(modeVanishTypeOff).setData(modeVanishDataOff).setName(modeVanishName).setLore(modeVanishLore).build();

    private final Material modeGuiType = stringToMaterial(sanitize(config.getString("staff-mode.gui-module.item")));
    private final short modeGuiData = getMaterialData("staff-mode.gui-module.item");
    private final String modeGuiName = config.getString("staff-mode.gui-module.name");
    private final List<String> modeGuiLore = JavaUtils.stringToList(config.getString("staff-mode.gui-module.lore"));
    public ItemStack modeGuiItem = Items.builder().setMaterial(modeGuiType).setData(modeGuiData).setName(modeGuiName).setLore(modeGuiLore).build();

    private final Material modeCounterType = stringToMaterial(sanitize(config.getString("staff-mode.counter-module.item")));
    private final short modeCounterData = getMaterialData(config.getString("staff-mode.counter-module.item"));
    private final String modeCounterName = config.getString("staff-mode.counter-module.name");
    private final List<String> modeCounterLore = JavaUtils.stringToList(config.getString("staff-mode.counter-module.lore"));
    public ItemStack modeCounterItem = Items.builder().setMaterial(modeCounterType).setData(modeCounterData).setName(modeCounterName).setLore(modeCounterLore).build();

    private final Material modeFreezeType = stringToMaterial(sanitize(config.getString("staff-mode.freeze-module.item")));
    private final short modeFreezeData = getMaterialData(config.getString("staff-mode.freeze-module.item"));
    private final String modeFreezeName = config.getString("staff-mode.freeze-module.name");
    private final List<String> modeFreezeLore = JavaUtils.stringToList(config.getString("staff-mode.freeze-module.lore"));
    public ItemStack modeFreezeItem = Items.builder().setMaterial(modeFreezeType).setData(modeFreezeData).setName(modeFreezeName).setLore(modeFreezeLore).build();

    private final Material modeCpsType = stringToMaterial(sanitize(config.getString("staff-mode.cps-module.item")));
    private final short modeCpsData = getMaterialData(config.getString("staff-mode.cps-module.item"));
    private final String modeCpsName = config.getString("staff-mode.cps-module.name");
    private final List<String> modeCpsLore = JavaUtils.stringToList(config.getString("staff-mode.cps-module.lore"));
    public ItemStack modeCpsItem = Items.builder().setMaterial(modeCpsType).setData(modeCpsData).setName(modeCpsName).setLore(modeCpsLore).build();

    private final Material modeExamineType = stringToMaterial(sanitize(config.getString("staff-mode.examine-module.item")));
    private final short modeExamineData = getMaterialData(config.getString("staff-mode.examine-module.item"));
    private final String modeExamineName = config.getString("staff-mode.examine-module.name");
    private final List<String> modeExamineLore = JavaUtils.stringToList(config.getString("staff-mode.examine-module.lore"));
    public ItemStack modeExamineItem = Items.builder().setMaterial(modeExamineType).setData(modeExamineData).setName(modeExamineName).setLore(modeExamineLore).build();


    /*
     * Storage
     */
    public final String storageType = configVersion >= 6200 ? config.getString("storage.type") : "sqlite";
    public final String mySqlHost = configVersion >= 6200 ? config.getString("storage.mysql.host") : "localhost";
    public final String mySqlUser = configVersion >= 6200 ? config.getString("storage.mysql.user") : "root";
    public final String database = configVersion >= 6200 ? config.getString("storage.mysql.database") : "root";
    public final String mySqlPassword = configVersion >= 6200 ? config.getString("storage.mysql.password") : "mypass";
    public final int mySqlPort = configVersion >= 6200 ? config.getInt("storage.mysql.port") : 3306;

    private final Material modeFollowType = stringToMaterial(sanitize(config.getString("staff-mode.follow-module.item")));
    private final short modeFollowData = getMaterialData(config.getString("staff-mode.follow-module.item"));
    private final String modeFollowName = config.getString("staff-mode.follow-module.name");
    private final List<String> modeFollowLore = JavaUtils.stringToList(config.getString("staff-mode.follow-module.lore"));
    public final ItemStack modeFollowItem = Items.builder().setMaterial(modeFollowType).setData(modeFollowData).setName(modeFollowName).setLore(modeFollowLore).build();

    public Options() {
        loadCustomModules();
    }

    private void loadCustomModules() {
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
                Enchantment enchantment = Enchantment.getByName(enchantInfoParts[0]);
                if(enchantment == null){
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

    private String getMaterial(String current) {
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
        List<Material> list = new ArrayList<Material>();

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

    private Material stringToMaterial(String string) {
        Material sound = Material.STONE;

        boolean isValid = JavaUtils.isValidEnum(Material.class, getMaterial(string));
        if (!isValid) {
            Bukkit.getLogger().severe("Invalid material type '" + string + "'!");
        } else
            sound = Material.valueOf(getMaterial(string));

        return sound;
    }

    public ModuleConfiguration.ModuleType stringToModuleType(String string) {
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