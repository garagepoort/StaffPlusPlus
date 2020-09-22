package net.shortninja.staffplus;

import net.shortninja.staffplus.nms.Protocol_v1_12;
import net.shortninja.staffplus.player.NodeUser;
import net.shortninja.staffplus.player.OfflinePlayerProvider;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.player.attribute.TicketHandler;
import net.shortninja.staffplus.player.attribute.mode.ModeCoordinator;
import net.shortninja.staffplus.player.attribute.mode.handler.CpsHandler;
import net.shortninja.staffplus.player.attribute.mode.handler.GadgetHandler;
import net.shortninja.staffplus.player.attribute.mode.handler.InventoryHandler;
import net.shortninja.staffplus.player.attribute.mode.handler.ReviveHandler;
import net.shortninja.staffplus.player.ext.bukkit.BukkitOfflinePlayerProvider;
import net.shortninja.staffplus.server.AlertCoordinator;
import net.shortninja.staffplus.server.PacketModifier;
import net.shortninja.staffplus.server.command.CmdHandler;
import net.shortninja.staffplus.server.compatibility.IProtocol;
import net.shortninja.staffplus.server.data.Load;
import net.shortninja.staffplus.server.data.Save;
import net.shortninja.staffplus.server.data.config.AutoUpdater;
import net.shortninja.staffplus.server.data.config.IOptions;
import net.shortninja.staffplus.server.data.file.ChangelogFile;
import net.shortninja.staffplus.server.data.file.DataFile;
import net.shortninja.staffplus.server.data.file.LanguageFile;
import net.shortninja.staffplus.server.hook.HookHandler;
import net.shortninja.staffplus.server.hook.SuperVanishHook;
import net.shortninja.staffplus.server.listener.*;
import net.shortninja.staffplus.server.listener.entity.EntityChangeBlock;
import net.shortninja.staffplus.server.listener.entity.EntityDamage;
import net.shortninja.staffplus.server.listener.entity.EntityDamageByEntity;
import net.shortninja.staffplus.server.listener.entity.EntityTarget;
import net.shortninja.staffplus.server.listener.player.*;
import net.shortninja.staffplus.staff.staffchat.BungeeStaffChatListener;
import net.shortninja.staffplus.unordered.IUser;
import net.shortninja.staffplus.util.Metrics;
import net.shortninja.staffplus.util.PermissionHandler;
import net.shortninja.staffplus.util.database.DatabaseInitializer;
import net.shortninja.staffplus.util.updates.UpdateNotifier;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.logging.Filter;
import java.util.logging.LogRecord;

import static org.bukkit.Bukkit.getScheduler;

// TODO Add command to check e chests and offline player inventories

public class StaffPlus extends JavaPlugin implements IStaffPlus {
    private static StaffPlus plugin;

    public IProtocol versionProtocol;
    public DataFile dataFile;
    public LanguageFile languageFile;

    public HookHandler hookHandler;
    public CpsHandler cpsHandler;
    public GadgetHandler gadgetHandler;
    public ReviveHandler reviveHandler;
    public OfflinePlayerProvider offlinePlayerProvider;
    public TicketHandler ticketHandler;
    public CmdHandler cmdHandler;
    public ModeCoordinator modeCoordinator;
    public AlertCoordinator alertCoordinator;
    public UUID consoleUUID = UUID.fromString("9c417515-22bc-46b8-be4d-538482992f8f");
    public Tasks tasks;
    public Map<UUID, IUser> users;
    public List<Inventory> viewedChest = new ArrayList<>();
    public InventoryHandler inventoryHandler;
    public boolean usesPlaceholderAPI;
    private DatabaseInitializer databaseInitializer = new DatabaseInitializer();

    public static StaffPlus get() {
        return plugin;
    }

    @Override
    public void onLoad() {
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeStaffChatListener());

        Bukkit.getLogger().setFilter(new PasswordFilter()); // FIXME


        Plugin placeholderPlugin;
        if ((placeholderPlugin = Bukkit.getPluginManager().getPlugin("PlaceholderAPI")) != null) {
            usesPlaceholderAPI = true;
            Bukkit.getLogger().info("Hooked into PlaceholderAPI " + placeholderPlugin.getDescription().getVersion());
        }
    }

    @Override
    public void onEnable() {
        plugin = this;
        IocContainer.init(this);
        saveDefaultConfig();
        AutoUpdater.updateConfig(this);

        start(System.currentTimeMillis());
        loadPlayerProvider();

        if (getConfig().getBoolean("metrics"))
            new Metrics(this);

        hookHandler.addHook(new SuperVanishHook(this));
        hookHandler.enableAll();
    }

    @Override
    public UserManager getUserManager() {
        return IocContainer.getUserManager();
    }

    @Override
    public void onDisable() {
        IocContainer.getMessage().sendConsoleMessage("Staff++ is now disabling!", true);
        stop();
    }

    public void saveUsers() {
        for (IUser user : IocContainer.getUserManager().getAll()) {
            new Save(new NodeUser(user));
        }

        dataFile.save();
    }

    protected void start(long start) {
        users = new HashMap<>();
        if (!setupVersionProtocol()) {
            IocContainer.getMessage().sendConsoleMessage("This version of Minecraft is not supported! If you have just updated to a brand new server version, check the Spigot plugin page.", true);
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        getScheduler().runTaskAsynchronously(this, () -> {
            new UpdateNotifier().checkUpdate();
        });

        dataFile = new DataFile("data.yml");
        languageFile = new LanguageFile();
        hookHandler = new HookHandler();
        cpsHandler = new CpsHandler();
        gadgetHandler = new GadgetHandler();
        reviveHandler = new ReviveHandler();
        ticketHandler = new TicketHandler();
        cmdHandler = new CmdHandler();
        modeCoordinator = new ModeCoordinator();
        alertCoordinator = new AlertCoordinator();
        tasks = new Tasks();
        inventoryHandler = new InventoryHandler();
        this.databaseInitializer.initialize();

        for (Player player : Bukkit.getOnlinePlayers()) {
            new Load().load(player);
        }
        registerListeners();
        new ChangelogFile();

        if (!IocContainer.getOptions().disablePackets || !IocContainer.getOptions().animationPackets.isEmpty() || !IocContainer.getOptions().soundNames.isEmpty()) {
            new PacketModifier();
        }

        IocContainer.getMessage().sendConsoleMessage("Staff++ has been enabled! Initialization took " + (System.currentTimeMillis() - start) + "ms.", false);
        IocContainer.getMessage().sendConsoleMessage("Plugin created by Shortninja continued by Qball - Revisited by Garagepoort", false);
    }

    private boolean setupVersionProtocol() {
        final String version = Bukkit.getServer().getClass().getPackage().getName();
        final String formattedVersion = version.substring(version.lastIndexOf('.') + 1);
        versionProtocol = new Protocol_v1_12(this);
        IocContainer.getMessage().sendConsoleMessage("Version protocol set to '" + formattedVersion + "'.", false);
        return versionProtocol != null;
    }

    private void registerListeners() {
        new EntityDamage();
        new EntityDamageByEntity();
        new EntityTarget();
        new AsyncPlayerChat();
        new PlayerCommandPreprocess();
        new PlayerDeath();
        new PlayerDropItem();
        new PlayerInteract();
        new PlayerLogin();
        new PlayerJoin();
        new PlayerPickupItem();
        new PlayerQuit();
        new BlockBreak();
        new BlockPlace();
        new FoodLevelChange();
        new InventoryClick();
        new InventoryClose();
        new InventoryOpen();
        new PlayerWorldChange();
        new EntityChangeBlock();
    }


    /*
     * Nullifying all of the instances is sort of an experimental thing to deal
     * with memory leaks that could occur on reloads (where instances could be
     * handled incorrectly)
     */


    private void stop() {
        hookHandler.disableAll();

        saveUsers();
        tasks.cancel();

        for (Player player : Bukkit.getOnlinePlayers()) {
            modeCoordinator.removeMode(player);
            IocContainer.getVanishHandler().removeVanish(player);
        }

        versionProtocol = null;
        languageFile = null;
        cpsHandler = null;
        gadgetHandler = null;
        reviveHandler = null;
        ticketHandler = null;
        cmdHandler = null;
        modeCoordinator = null;
        alertCoordinator = null;
        tasks = null;
        plugin = null;

    }

    @Override
    public IOptions getOptions() {
        return IocContainer.getOptions();
    }

    public PermissionHandler getPermissions() {
        return IocContainer.getPermissionHandler();
    }


    private static final class PasswordFilter implements Filter {

        @Override
        public boolean isLoggable(LogRecord record) {
            return !record.getMessage().toLowerCase().contains("/register") && !record.getMessage().toLowerCase().contains("/login");
        }
    }

    private void loadPlayerProvider() {
        if (IocContainer.getOptions().offlinePlayersModeEnabled) {
            offlinePlayerProvider = new BukkitOfflinePlayerProvider();
        }
    }

}