package net.shortninja.staffplus;

import net.shortninja.staffplus.command.CmdHandler;
import net.shortninja.staffplus.data.Load;
import net.shortninja.staffplus.data.Save;
import net.shortninja.staffplus.data.config.Messages;
import net.shortninja.staffplus.data.config.Options;
import net.shortninja.staffplus.data.file.ChangelogFile;
import net.shortninja.staffplus.data.file.DataFile;
import net.shortninja.staffplus.data.file.LanguageFile;
import net.shortninja.staffplus.listener.BlockBreak;
import net.shortninja.staffplus.listener.BlockPlace;
import net.shortninja.staffplus.listener.InventoryClick;
import net.shortninja.staffplus.listener.InventoryClose;
import net.shortninja.staffplus.listener.entity.EntityDamage;
import net.shortninja.staffplus.listener.entity.EntityDamageByEntity;
import net.shortninja.staffplus.listener.entity.EntityTarget;
import net.shortninja.staffplus.listener.player.AsyncPlayerChat;
import net.shortninja.staffplus.listener.player.PlayerCommandPreprocess;
import net.shortninja.staffplus.listener.player.PlayerDeath;
import net.shortninja.staffplus.listener.player.PlayerDropItem;
import net.shortninja.staffplus.listener.player.PlayerInteract;
import net.shortninja.staffplus.listener.player.PlayerJoin;
import net.shortninja.staffplus.listener.player.PlayerQuit;
import net.shortninja.staffplus.player.NodeUser;
import net.shortninja.staffplus.player.User;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.player.attribute.TicketHandler;
import net.shortninja.staffplus.player.attribute.infraction.InfractionCoordinator;
import net.shortninja.staffplus.player.attribute.mode.ModeCoordinator;
import net.shortninja.staffplus.player.attribute.mode.handler.CpsHandler;
import net.shortninja.staffplus.player.attribute.mode.handler.FreezeHandler;
import net.shortninja.staffplus.player.attribute.mode.handler.GadgetHandler;
import net.shortninja.staffplus.player.attribute.mode.handler.ReviveHandler;
import net.shortninja.staffplus.player.attribute.mode.handler.VanishHandler;
import net.shortninja.staffplus.server.AlertCoordinator;
import net.shortninja.staffplus.server.PacketModifier;
import net.shortninja.staffplus.server.chat.ChatHandler;
import net.shortninja.staffplus.server.compatibility.IProtocol;
import net.shortninja.staffplus.server.compatibility.v1_10.Protocol_v1_10_R1;
import net.shortninja.staffplus.server.compatibility.v1_7.Protocol_v1_7_R1;
import net.shortninja.staffplus.server.compatibility.v1_7.Protocol_v1_7_R2;
import net.shortninja.staffplus.server.compatibility.v1_7.Protocol_v1_7_R3;
import net.shortninja.staffplus.server.compatibility.v1_7.Protocol_v1_7_R4;
import net.shortninja.staffplus.server.compatibility.v1_8.Protocol_v1_8_R1;
import net.shortninja.staffplus.server.compatibility.v1_8.Protocol_v1_8_R3;
import net.shortninja.staffplus.server.compatibility.v1_9.Protocol_v1_9_R1;
import net.shortninja.staffplus.server.compatibility.v1_9.Protocol_v1_9_R2;
import net.shortninja.staffplus.util.Message;
import net.shortninja.staffplus.util.Permission;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class StaffPlus extends JavaPlugin
{
	private static StaffPlus plugin;
	
	public IProtocol versionProtocol;
	public Permission permission;
	public Message message;
	public Options options;
	public DataFile dataFile;
    public LanguageFile languageFile;
    public Messages messages;
	public UserManager userManager;
	public CpsHandler cpsHandler;
	public FreezeHandler freezeHandler;
	public GadgetHandler gadgetHandler;
	public ReviveHandler reviveHandler;
	public VanishHandler vanishHandler;
	public ChatHandler chatHandler;
	public TicketHandler ticketHandler;
	public CmdHandler cmdHandler;
	public ModeCoordinator modeCoordinator;
	public InfractionCoordinator infractionCoordinator;
	public AlertCoordinator alertCoordinator;
	public Tasks tasks;
	
	@Override
	public void onEnable()
	{
		plugin = this;
		saveDefaultConfig();
		permission = new Permission();
		message = new Message();
		options = new Options();
		start(System.currentTimeMillis());
	}
	
	@Override
	public void onDisable()
	{
		message.sendConsoleMessage("Staff+ is now disabling!", true);
		stop();
		plugin = null;
	}
	
	public static StaffPlus get()
	{
		return plugin;
	}
	
	public void saveUsers()
	{
		for(User user : userManager.getUsers())
		{
			new Save(new NodeUser(user));
		}
		
		dataFile.save();
	}
	
	private void start(long start)
	{
		if(!setupVersionProtocol())
		{
			message.sendConsoleMessage("This version of Minecraft is not supported! If you have just updated to a brand new server version, check the Spigot plugin page.", true);
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		
		dataFile = new DataFile("data.yml");
		languageFile = new LanguageFile();
		messages = new Messages(languageFile);
		userManager = new UserManager();
		cpsHandler = new CpsHandler();
		freezeHandler = new FreezeHandler();
		gadgetHandler = new GadgetHandler();
		reviveHandler = new ReviveHandler();
		vanishHandler = new VanishHandler();
		chatHandler = new ChatHandler();
		ticketHandler = new TicketHandler();
		cmdHandler = new CmdHandler();
		modeCoordinator = new ModeCoordinator();
		infractionCoordinator = new InfractionCoordinator();
		alertCoordinator = new AlertCoordinator();
		tasks = new Tasks();
		
		for(Player player : Bukkit.getOnlinePlayers())
		{
			new Load(player);
		}
		
		registerListeners();
		new ChangelogFile();
		new PacketModifier();
		message.sendConsoleMessage("Staff+ has been enabled! Initialization took " + (System.currentTimeMillis() - start) + "ms.", false);
		message.sendConsoleMessage("Plugin created by Shortninja.", false);
	}
	
	private boolean setupVersionProtocol()
	{
		String version = Bukkit.getServer().getClass().getPackage().getName();
		String formattedVersion = version.substring(version.lastIndexOf('.') + 1);
		
		switch(formattedVersion)
		{
			case "v1_7_R1":
				versionProtocol = new Protocol_v1_7_R1();
				break;
			case "v1_7_R2":
				versionProtocol = new Protocol_v1_7_R2();
				break;
			case "v1_7_R3":
				versionProtocol = new Protocol_v1_7_R3();
				break;
			case "v1_7_R4":
				versionProtocol = new Protocol_v1_7_R4();
				break;
			case "v1_8_R1":
				versionProtocol = new Protocol_v1_8_R1();
				break;
			case "v1_8_R2":
				versionProtocol = new Protocol_v1_8_R3();
				break;
			case "v1_8_R3":
				versionProtocol = new Protocol_v1_8_R3();
				break;
			case "v1_9_R1":
				versionProtocol = new Protocol_v1_9_R1();
				break;
			case "v1_9_R2":
				versionProtocol = new Protocol_v1_9_R2();
				break;
			case "v1_10_R1":
				versionProtocol = new Protocol_v1_10_R1();
				break;
		}
		
		if(versionProtocol != null)
		{
			message.sendConsoleMessage("Version protocol set to '" + formattedVersion + "'.", false);
		}
		
		return versionProtocol != null;
	}
	
	private void registerListeners()
	{
		new EntityDamage();
		new EntityDamageByEntity();
		new EntityTarget();
		new AsyncPlayerChat();
		new PlayerCommandPreprocess();
		new PlayerDeath();
		new PlayerDropItem();
		new PlayerInteract();
		new PlayerJoin();
		new PlayerQuit();
		new BlockBreak();
		new BlockPlace();
		new InventoryClick();
		new InventoryClose();
	}
	
	/*
	 * Nullifying all of the instances is sort of an experimental thing to deal
	 * with memory leaks that could occur on reloads (where instances could be
	 * handled incorrectly)
	 */
	private void stop()
	{
		saveUsers();
		tasks.cancel();
		
		for(Player player : Bukkit.getOnlinePlayers())
		{
			modeCoordinator.removeMode(player);
			vanishHandler.removeVanish(player);
		}
		
		versionProtocol = null;
		permission = null;
		message = null;
		options = null;
		languageFile = null;
		userManager = null;
		cpsHandler = null;
		freezeHandler = null;
		gadgetHandler = null;
		reviveHandler = null;
		vanishHandler = null;
		chatHandler = null;
		ticketHandler = null;
		cmdHandler = null;
		modeCoordinator = null;
		infractionCoordinator = null;
		alertCoordinator = null;
		tasks = null;
	}
}