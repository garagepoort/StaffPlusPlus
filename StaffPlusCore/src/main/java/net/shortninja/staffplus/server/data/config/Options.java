package net.shortninja.staffplus.server.data.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import net.shortninja.staffplus.player.attribute.mode.item.ModuleConfiguration;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.lib.Sounds;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.attribute.mode.handler.VanishHandler.VanishType;
import net.shortninja.staffplus.util.lib.JavaUtils;
import net.shortninja.staffplus.util.lib.hex.Items;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

//TODO: replace this with something that isn't horribly coupled...
public class Options implements IOptions
{
	private static FileConfiguration config = StaffPlus.get().getConfig();
	private static final int CURRENT_VERSION = 6194;
	private MessageCoordinator message = StaffPlus.get().message;
	private int configVersion = config.getInt("config-version");
	
	public Options()
	{
		/*
		 * Configuration updating support added, but too buggy to release.
		 */
		if(configVersion < CURRENT_VERSION)
		{
			 //updateConfig();
		}
		
		loadCustomModules();
	}
	
	/*
	 * General
	 */
	public String language = config.getString("lang");
	public List<String> blockedCommands = JavaUtils.stringToList(config.getString("blocked-commands"));
	public List<String> blockedModeCommands = JavaUtils.stringToList(config.getString("blocked-mode-commands"));
	public short glassColor = (short) config.getInt("glass-color");
	public String glassTitle = config.getString("glass-title");
	
	/*
	 * Advanced
	 */
	public int autoSave = config.getInt("auto-save");
	public long clock = config.getInt("clock") * 20;
	public boolean disablePackets = configVersion >= 3.19 ? config.getBoolean("disable-packets") : false;
	public List<String> animationPackets = JavaUtils.stringToList(config.getString("animation-packets"));
	public List<String> soundNames = JavaUtils.stringToList(config.getString("sound-names"));
	
	/*
	 * Reports
	 */
	public boolean reportsEnabled = config.getBoolean("reports-module.enabled");
	public Sounds reportsSound = stringToSound(sanitize(config.getString("reports-module.sound")));
	public int reportsCooldown = config.getInt("reports-module.cooldown");
	public boolean reportsShowReporter = config.getBoolean("reports-module.show-reporter");
	
	/*
	 * Warnings
	 */
	public boolean warningsEnabled = config.getBoolean("warnings-module.enabled");
	public Sounds warningsSound = stringToSound(sanitize(config.getString("warnings-module.sound")));
	public int warningsMaximum = config.getInt("warnings-module.maximum");
	public String warningsBanCommand = config.getString("warnings-module.ban-command");
	public long warningsClear = config.getLong("warnings-module.clear");
	public boolean warningsShowIssuer = config.getBoolean("warnings-module.show-issuer");
	
	/*
	 * Staff Chat
	 */
	public boolean staffChatEnabled = config.getBoolean("staff-chat-module.enabled");
	public String staffChatHandle = config.getString("staff-chat-module.handle");
	
	/*
	 * Vanish
	 */
	public boolean vanishEnabled = config.getBoolean("vanish-module.enabled");
	public boolean vanishTabList = config.getBoolean("vanish-module.tab-list");
	public boolean vanishShowAway = config.getBoolean("vanish-module.show-away");
	
	/*
	 * Chat
	 */
	public boolean chatEnabled = config.getBoolean("chat-module.enabled");
	public int chatLines = config.getInt("chat-module.lines");
	public int chatSlow = config.getInt("chat-module.slow");
	public boolean chatBlacklistEnabled = config.getBoolean("chat-module.blacklist-module.enabled");
	public boolean chatBlacklistHoverable = config.getBoolean("chat-module.blacklist-module.hoverable");
	public String chatBlacklistCharacter = config.getString("chat-module.blacklist-module.character");
	public boolean chatBlacklistMerging = config.getBoolean("chat-module.blacklist-module.merging");
	public List<String> chatBlacklistWords = JavaUtils.stringToList(config.getString("chat-module.blacklist-module.words"));
	public List<String> chatBlacklistCharacters = JavaUtils.stringToList(config.getString("chat-module.blacklist-module.characters"));
	public List<String> chatBlacklistDomains = JavaUtils.stringToList(config.getString("chat-module.blacklist-module.domains"));
	public List<String> chatBlacklistPeriods = JavaUtils.stringToList(config.getString("chat-module.blacklist-module.periods"));
	public List<String> chatBlacklistAllowed = JavaUtils.stringToList(config.getString("chat-module.blacklist-module.allowed"));
	
	/*
	 * Tickets
	 */
	public boolean ticketsEnabled = config.getBoolean("tickets-module.enabled");
	public boolean ticketsGlobal = config.getBoolean("tickets-module.global");
	public boolean ticketsKeepOpen = config.getBoolean("tickets-module.keep-open");
	
	/*
	 * Alerts
	 */
	public boolean alertsNameNotify = config.getBoolean("alerts-module.name-notify");
	public boolean alertsMentionNotify = config.getBoolean("alerts-module.mention-notify");
	public Sounds alertsSound = stringToSound(sanitize(config.getString("alerts-module.sound")));
	public boolean alertsXrayEnabled = config.getBoolean("alerts-module.xray-alerts.enabled");
	public List<Material> alertsXrayBlocks = stringToMaterialList(config.getString("alerts-module.xray-alerts.blocks"));
	
	/*
	 * Security
	 */
	public boolean loginEnabled = configVersion >= 3.2 ? config.getBoolean("login.enabled") : false;
	public String loginKick = configVersion >= 3.2 ? config.getString("login.kick-message") : "&cInvalid login password!";
	
	/*
	 * Staff Mode
	 */
	public boolean modeBlockManipulation = config.getBoolean("staff-mode.block-manipulation");
	public boolean modeInventoryInteraction = config.getBoolean("staff-mode.inventory-interaction");
	public boolean modeItemChange = configVersion >= 3.1 ? config.getBoolean("staff-mode.item-change") : true;
	public VanishType modeVanish = stringToVanishType(config.getString("staff-mode.vanish-type"));
	public boolean modeVanished = config.getBoolean("staff-mode.vanish");
	public boolean modeInvincible = config.getBoolean("staff-mode.invincible");
	public boolean modeDamage = configVersion >= 6194 ? config.getBoolean("staff-mode.damage") : false;
	public boolean modeHungerLoss = configVersion >= 3.17 ? config.getBoolean("staff-mode.hunger-loss") : true;
	public boolean modeFlight = config.getBoolean("staff-mode.flight");
	public boolean modeCreative = config.getBoolean("staff-mode.creative");
	public boolean modeOriginalLocation = config.getBoolean("staff-mode.original-location");
	public boolean modeEnableOnLogin = config.getBoolean("staff-mode.enable-on-login");
	public List<String> modeEnableCommands = configVersion >= 3.16 ? JavaUtils.stringToList(config.getString("staff-mode.enable-commands")) : new ArrayList<String>();
	public List<String> modeDisableCommands = configVersion >= 3.16 ? JavaUtils.stringToList(config.getString("staff-mode.disable-commands")) : new ArrayList<String>();
	
	/*
	 * Compass
	 */
	public boolean modeCompassEnabled = config.getBoolean("staff-mode.compass-module.enabled");
	public int modeCompassSlot = config.getInt("staff-mode.compass-module.slot") - 1;
	public int modeCompassVelocity = config.getInt("staff-mode.compass-module.velocity");
	private Material modeCompassType = stringToMaterial(sanitize(config.getString("staff-mode.compass-module.item")));
	private short modeCompassData = getMaterialData(config.getString("staff-mode.compass-module.item"));
	private String modeCompassName = config.getString("staff-mode.compass-module.name");
	private List<String> modeCompassLore = JavaUtils.stringToList(config.getString("staff-mode.compass-module.lore"));
	public ItemStack modeCompassItem = Items.builder().setMaterial(modeCompassType).setData(modeCompassData).setName(modeCompassName).setLore(modeCompassLore).build();
	
	/*
	 * Random Teleport
	 */
	public boolean modeRandomTeleportEnabled = config.getBoolean("staff-mode.random-teleport-module.enabled");
	public int modeRandomTeleportSlot = config.getInt("staff-mode.random-teleport-module.slot") - 1;
	private Material modeRandomTeleportType = stringToMaterial(sanitize(config.getString("staff-mode.random-teleport-module.item")));
	private short modeRandomTeleportData = getMaterialData(config.getString("staff-mode.random-teleport-module.item"));
	private String modeRandomTeleportName = config.getString("staff-mode.random-teleport-module.name");
	private List<String> modeRandomTeleportLore = JavaUtils.stringToList(config.getString("staff-mode.random-teleport-module.lore"));
	public ItemStack modeRandomTeleportItem = Items.builder().setMaterial(modeRandomTeleportType).setData(modeRandomTeleportData).setName(modeRandomTeleportName).setLore(modeRandomTeleportLore).build();
	public boolean modeRandomTeleportRandom = config.getBoolean("staff-mode.random-teleport-module.random");
	
	/*
	 * Vanish
	 */
	public boolean modeVanishEnabled = config.getBoolean("staff-mode.vanish-module.enabled");
	public int modeVanishSlot = config.getInt("staff-mode.vanish-module.slot") - 1;
	private Material modeVanishType = stringToMaterial(sanitize(config.getString("staff-mode.vanish-module.item")));
	private short modeVanishData = getMaterialData(config.getString("staff-mode.vanish-module.item"));
	private String modeVanishName = config.getString("staff-mode.vanish-module.name");
	private List<String> modeVanishLore = JavaUtils.stringToList(config.getString("staff-mode.vanish-module.lore"));
	public ItemStack modeVanishItem = Items.builder().setMaterial(modeVanishType).setData(modeVanishData).setName(modeVanishName).setLore(modeVanishLore).build();
	private Material modeVanishTypeOff = stringToMaterial(sanitize(config.getString("staff-mode.vanish-module.item-off")));
	private short modeVanishDataOff = getMaterialData(config.getString("staff-mode.vanish-module.item-off"));
	public ItemStack modeVanishItemOff = Items.builder().setMaterial(modeVanishTypeOff).setData(modeVanishDataOff).setName(modeVanishName).setLore(modeVanishLore).build();
	
	/*
	 * GUI Hub
	 */
	public boolean modeGuiEnabled = config.getBoolean("staff-mode.gui-module.enabled");
	public int modeGuiSlot = config.getInt("staff-mode.gui-module.slot") - 1;
	private Material modeGuiType = stringToMaterial(sanitize(config.getString("staff-mode.gui-module.item")));
	private short modeGuiData = getMaterialData("staff-mode.gui-module.item");
	private String modeGuiName = config.getString("staff-mode.gui-module.name");
	private List<String> modeGuiLore = JavaUtils.stringToList(config.getString("staff-mode.gui-module.lore"));
	public ItemStack modeGuiItem = Items.builder().setMaterial(modeGuiType).setData(modeGuiData).setName(modeGuiName).setLore(modeGuiLore).build();
	public boolean modeGuiReports = config.getBoolean("staff-mode.gui-module.reports-gui");
	public String modeGuiReportsTitle = config.getString("staff-mode.gui-module.reports-title");
	public boolean modeGuiMiner = config.getBoolean("staff-mode.gui-module.miner-gui");
	public String modeGuiMinerTitle = config.getString("staff-mode.gui-module.miner-title");
	public int modeGuiMinerLevel = config.getInt("staff-mode.gui-module.xray-level");
	
	/*
	 * Counter
	 */
	public boolean modeCounterEnabled = config.getBoolean("staff-mode.counter-module.enabled");
	public int modeCounterSlot = config.getInt("staff-mode.counter-module.slot") - 1;
	private Material modeCounterType = stringToMaterial(sanitize(config.getString("staff-mode.counter-module.item")));
	private short modeCounterData = getMaterialData(config.getString("staff-mode.counter-module.item"));
	private String modeCounterName = config.getString("staff-mode.counter-module.name");
	private List<String> modeCounterLore = JavaUtils.stringToList(config.getString("staff-mode.counter-module.lore"));
	public ItemStack modeCounterItem = Items.builder().setMaterial(modeCounterType).setData(modeCounterData).setName(modeCounterName).setLore(modeCounterLore).build();
	public String modeCounterTitle = config.getString("staff-mode.counter-module.title");
	public boolean modeCounterShowStaffMode = config.getBoolean("staff-mode.counter-module.show-staff-mode");
	
	/*
	 * Freeze
	 */
	public boolean modeFreezeEnabled = config.getBoolean("staff-mode.freeze-module.enabled");
	public int modeFreezeSlot = config.getInt("staff-mode.freeze-module.slot") - 1;
	private Material modeFreezeType = stringToMaterial(sanitize(config.getString("staff-mode.freeze-module.item")));
	private short modeFreezeData = getMaterialData(config.getString("staff-mode.freeze-module.item"));
	private String modeFreezeName = config.getString("staff-mode.freeze-module.name");
	private List<String> modeFreezeLore = JavaUtils.stringToList(config.getString("staff-mode.freeze-module.lore"));
	public ItemStack modeFreezeItem = Items.builder().setMaterial(modeFreezeType).setData(modeFreezeData).setName(modeFreezeName).setLore(modeFreezeLore).build();
	public boolean modeFreezeChat = config.getBoolean("staff-mode.freeze-module.chat");
	public boolean modeFreezeDamage = config.getBoolean("staff-mode.freeze-module.damage");
	public int modeFreezeTimer = configVersion >= 3.17 ? config.getInt("staff-mode.freeze-module.timer") : 0;
	public Sounds modeFreezeSound = configVersion >= 3.17 ? stringToSound(sanitize(config.getString("staff-mode.freeze-module.sound"))) : Sounds.ORB_PICKUP;
	public boolean modeFreezePrompt = configVersion >= 3.1 ? config.getBoolean("staff-mode.freeze-module.prompt") : false;
	public String modeFreezePromptTitle = configVersion >= 3.1 ? config.getString("staff-mode.freeze-module.prompt-title") : "&bFrozen";
	
	/*
	 * CPS
	 */
	public boolean modeCpsEnabled = config.getBoolean("staff-mode.cps-module.enabled");
	public int modeCpsSlot = config.getInt("staff-mode.cps-module.slot") - 1;
	private Material modeCpsType = stringToMaterial(sanitize(config.getString("staff-mode.cps-module.item")));
	private short modeCpsData = getMaterialData(config.getString("staff-mode.cps-module.item"));
	private String modeCpsName = config.getString("staff-mode.cps-module.name");
	private List<String> modeCpsLore = JavaUtils.stringToList(config.getString("staff-mode.cps-module.lore"));
	public ItemStack modeCpsItem = Items.builder().setMaterial(modeCpsType).setData(modeCpsData).setName(modeCpsName).setLore(modeCpsLore).build();
	public long modeCpsTime = config.getInt("staff-mode.cps-module.time") * 20;
	public int modeCpsMax = config.getInt("staff-mode.cps-module.max");
	
	/*
	 * Examine
	 */
	public boolean modeExamineEnabled = config.getBoolean("staff-mode.examine-module.enabled");
	public int modeExamineSlot = config.getInt("staff-mode.examine-module.slot") - 1;
	private Material modeExamineType = stringToMaterial(sanitize(config.getString("staff-mode.examine-module.item")));
	private short modeExamineData = getMaterialData(config.getString("staff-mode.examine-module.item"));
	private String modeExamineName = config.getString("staff-mode.examine-module.name");
	private List<String> modeExamineLore = JavaUtils.stringToList(config.getString("staff-mode.examine-module.lore"));
	public ItemStack modeExamineItem = Items.builder().setMaterial(modeExamineType).setData(modeExamineData).setName(modeExamineName).setLore(modeExamineLore).build();
	public String modeExamineTitle = config.getString("staff-mode.examine-module.title");
	public int modeExamineFood = config.getInt("staff-mode.examine-module.info-line.food") <= 0 ? -1 : config.getInt("staff-mode.examine-module.info-line.food") + 44;
	public int modeExamineIp = config.getInt("staff-mode.examine-module.info-line.ip-address") <= 0 ? -1 : config.getInt("staff-mode.examine-module.info-line.ip-address") + 44;
	public int modeExamineGamemode = config.getInt("staff-mode.examine-module.info-line.gamemode") <= 0 ? -1 : config.getInt("staff-mode.examine-module.info-line.gamemode") + 44;
	public int modeExamineInfractions = config.getInt("staff-mode.examine-module.info-line.infractions") <= 0 ? -1 : config.getInt("staff-mode.examine-module.info-line.infractions") + 44;
	public int modeExamineLocation = config.getInt("staff-mode.examine-module.info-line.location") <= 0 ? -1 : config.getInt("staff-mode.examine-module.info-line.location") + 44;
	public int modeExamineNotes = config.getInt("staff-mode.examine-module.info-line.notes") <= 0 ? -1 : config.getInt("staff-mode.examine-module.info-line.notes") + 44;
	public int modeExamineFreeze = config.getInt("staff-mode.examine-module.info-line.freeze") <= 0 ? -1 : config.getInt("staff-mode.examine-module.info-line.freeze") + 44;
	public int modeExamineWarn = config.getInt("staff-mode.examine-module.info-line.warn") <= 0 ? -1 : config.getInt("staff-mode.examine-module.info-line.warn") + 44;
	
	/*
	 * Follow
	 */
	public boolean modeFollowEnabled = config.getBoolean("staff-mode.follow-module.enabled");
	public int modeFollowSlot = config.getInt("staff-mode.follow-module.slot") - 1;
	private Material modeFollowType = stringToMaterial(sanitize(config.getString("staff-mode.follow-module.item")));
	private short modeFollowData = getMaterialData(config.getString("staff-mode.follow-module.item"));
	private String modeFollowName = config.getString("staff-mode.follow-module.name");
	private List<String> modeFollowLore = JavaUtils.stringToList(config.getString("staff-mode.follow-module.lore"));
	public ItemStack modeFollowItem = Items.builder().setMaterial(modeFollowType).setData(modeFollowData).setName(modeFollowName).setLore(modeFollowLore).build();
	public boolean modeUseMount = config.getBoolean("staff-mode.follow-module.use-mount");
	
	/*
	 * Custom
	 */
	public Map<String, ModuleConfiguration> moduleConfigurations = new HashMap<String, ModuleConfiguration>();
	
	/*
	 * Permissions
	 */
	public String permissionWildcard = config.getString("permissions.wild-card");
	public String permissionBlock = config.getString("permissions.block");
	public String permissionReport = config.getString("permissions.report");
	public String permissionReportBypass = config.getString("permissions.report-bypass");
	public String permissionWarn = config.getString("permissions.warn");
	public String permissionWarnBypass = config.getString("permissions.warn-bypass");
	public String permissionStaffChat = config.getString("permissions.staff-chat");
	public String permissionVanishTotal = config.getString("permissions.vanish-total");
	public String permissionVanishList = config.getString("permissions.vanish-list");
	public String permissionChatClear = config.getString("permissions.chat-clear");
	public String permissionChatToggle = config.getString("permissions.chat-toggle");
	public String permissionChatSlow = config.getString("permissions.chat-slow");
	public String permissionBlacklist = config.getString("permissions.blacklist");
	public String permissionTickets = config.getString("permissions.tickets");
	public String permissionMention = config.getString("permissions.mention");
	public String permissionNameChange = config.getString("permissions.name-change");
	public String permissionXray = config.getString("permissions.xray");
	public String permissionMode = config.getString("permissions.mode");
	public String permissionCompass = config.getString("permissions.compass");
	public String permissionRandomTeleport = config.getString("permissions.random-teleport");
	public String permissionGui = config.getString("permissions.gui");
	public String permissionCounter = config.getString("permissions.counter");
	public String permissionFreeze = config.getString("permissions.freeze");
	public String permissionFreezeBypass = config.getString("permissions.freeze-bypass");
	public String permissionCps = config.getString("permissions.cps");
	public String permissionExamine = config.getString("permissions.examine");
	public String permissionExamineModify = config.getString("permissions.examine-modify");
	public String permissionFollow = config.getString("permissions.follow");
	public String permissionLockdown = config.getString("permissions.lockdown");
	public String permissionRevive = config.getString("permissions.revive");
	public String permissionMember = config.getString("permissions.member");
	public String permissionStrip = configVersion >= 6194 ? config.getString("permissions.strip") : "staff.strip";
	
	/*
	 * Commands
	 */
	public String commandStaffMode = config.getString("commands.staff-mode");
	public String commandFreeze = config.getString("commands.freeze");
	public String commandExamine = config.getString("commands.examine");
	public String commandNotes = configVersion >= 3.1 ? config.getString("commands.notes") : "notes";
	public String commandCps = config.getString("commands.cps");
	public String commandStaffChat = config.getString("commands.staff-chat");
	public String commandReport = config.getString("commands.report");
	public String commandWarn = config.getString("commands.warn");
	public String commandVanish = config.getString("commands.vanish");
	public String commandChat = config.getString("commands.chat");
	public String commandTicket = config.getString("commands.ticket");
	public String commandAlerts = config.getString("commands.alerts");
	public String commandFollow = config.getString("commands.follow");
	public String commandRevive = config.getString("commands.revive");
	public String commandStaffList = config.getString("commands.staff-list");
	public String commandLogin = configVersion >= 3.2 ? config.getString("commands.login") : "login";
	public String commandRegister = configVersion >= 3.2 ? config.getString("commands.register") : "register";
	public String commandStrip = configVersion >= 6194 ? config.getString("commands.strip") : "strip";
	
    private void updateConfig()
    {
		File dataFolder = StaffPlus.get().getDataFolder();
		File configFile = new File(dataFolder, "config.yml");
		YamlConfiguration oldConfig = YamlConfiguration.loadConfiguration(configFile);
		String backup = "backup-#" + CURRENT_VERSION + ".yml";
		String currentKey = "";

		configFile.renameTo(new File(dataFolder, backup));
		StaffPlus.get().saveDefaultConfig();
		config = StaffPlus.get().getConfig();
		
		for(String key : oldConfig.getConfigurationSection("").getKeys(true))
		{
			if(key.equalsIgnoreCase("config-version"))
			{
				config.set(key, CURRENT_VERSION);
			}else config.set(key, oldConfig.get(key));
		}
		
		for(String key : config.getConfigurationSection("").getKeys(true))
		{
			System.out.println(key);
			
			if(!key.contains("."))
			{
				currentKey = key;
			}
			
			if(!oldConfig.contains(key))
			{
				config.set(currentKey + key, config.get(key));
			}
		}
		
		config.options().header(" Staff+ | Made with love by Shortninja - ♥\n "
				+ "Your configuration file has been automatically updated to file version #" + CURRENT_VERSION + "!\n "
				+ "Unfortunately, all information comments reset when an update occurs, so you will\n "
				+ "have to completely regenerate your config by deleting it to get comments back.\n "
				+ "Though your settings should have been copied, your old config file was saved as\n "
				+ "'backup.yml' in the plugin folder, so your old settings can be reviewed.");
		config.options().copyHeader(true);
		StaffPlus.get().saveConfig();
		message.sendConsoleMessage("Your config has been updated to #" + CURRENT_VERSION + "! All configured values should be the same, but just in case your old configuration file is stored as a backup.", false);
    }
	
	private void loadCustomModules()
	{
		for(String identifier : config.getConfigurationSection("staff-mode.custom-modules").getKeys(false))
		{
			if(!config.getBoolean("staff-mode.custom-modules." + identifier + ".enabled"))
			{
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
			
			if(moduleType != ModuleConfiguration.ModuleType.ITEM)
			{
				action = config.getString("staff-mode.custom-modules." + identifier + ".command");
			}
			
			moduleConfigurations.put(identifier, new ModuleConfiguration(identifier, moduleType, slot, item, action));
		}
	}
	
	private List<Material> stringToMaterialList(String commas)
	{
		List<Material> list = new ArrayList<Material>();
		ListIterator<String> iterator = Arrays.asList(commas.split("\\s*,\\s*")).listIterator();
		
		while(iterator.hasNext())
		{
			list.add(stringToMaterial(sanitize(iterator.next())));
		}
		
		return list;
	}
	
	private short getMaterialData(String string)
	{
		short data = 0;
		
		if(string.contains(":"))
		{
			String dataString = string.substring(string.lastIndexOf(':') + 1);
			
			if(JavaUtils.isInteger(dataString))
			{
				data = (short) Integer.parseInt(dataString);
			}else message.sendConsoleMessage("Invalid material data '" + dataString + "' from '" + string + "'!", true);
		}
		
		return data;
	}
	
	private Sounds stringToSound(String string)
	{
		Sounds sound = Sounds.ORB_PICKUP;
		boolean isValid = JavaUtils.isValidEnum(Sounds.class, string);
		
		if(!isValid)
		{
			message.sendConsoleMessage("Invalid sound name '" + string + "'!", true);
		}else sound = Sounds.valueOf(string);
		
		return sound;
	}
	
	private VanishType stringToVanishType(String string)
	{
		VanishType vanishType = VanishType.NONE;
		boolean isValid = JavaUtils.isValidEnum(VanishType.class, string);
		
		if(!isValid)
		{
			message.sendConsoleMessage("Invalid vanish type '" + string + "'!", true);
		}else vanishType = VanishType.valueOf(string);
		
		return vanishType;
	}
	
	private Material stringToMaterial(String string)
	{
		Material sound = Material.STONE;
		boolean isValid = JavaUtils.isValidEnum(Material.class, string);
		
		if(!isValid)
		{
			message.sendConsoleMessage("Invalid material type '" + string + "'!", true);
		}else sound = Material.valueOf(string);
		
		return sound;
	}
	
	public ModuleConfiguration.ModuleType stringToModuleType(String string)
	{
		ModuleConfiguration.ModuleType moduleType = ModuleConfiguration.ModuleType.ITEM;
		boolean isValid = JavaUtils.isValidEnum(ModuleConfiguration.ModuleType.class, string);
		
		if(!isValid)
		{
			message.sendConsoleMessage("Invalid module type '" + string + "'!", true);
		}else moduleType = ModuleConfiguration.ModuleType.valueOf(string);
		
		return moduleType;
	}
	
	private String sanitize(String string)
	{
		if(string.contains(":"))
		{
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