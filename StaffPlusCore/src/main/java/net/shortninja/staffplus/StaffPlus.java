package net.shortninja.staffplus;

import be.garagepoort.staffplusplus.craftbukkit.api.ProtocolFactory;
import be.garagepoort.staffplusplus.craftbukkit.common.IProtocol;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.context.ContextCalculator;
import net.luckperms.api.context.ContextManager;
import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.application.data.DataFile;
import net.shortninja.staffplus.application.database.DatabaseInitializer;
import net.shortninja.staffplus.application.metrics.MetricsUtil;
import net.shortninja.staffplus.application.updates.UpdateNotifier;
import net.shortninja.staffplus.common.UpdatableGui;
import net.shortninja.staffplus.common.bungee.BungeeUtil;
import net.shortninja.staffplus.common.cmd.CmdHandler;
import net.shortninja.staffplus.common.config.AutoUpdater;
import net.shortninja.staffplus.common.config.AutoUpdaterLanguageFiles;
import net.shortninja.staffplus.common.utils.BukkitUtils;
import net.shortninja.staffplus.common.utils.PermissionHandler;
import net.shortninja.staffplus.domain.staff.mode.StaffModeLuckPermsContextCalculator;
import net.shortninja.staffplus.domain.staff.mode.handler.CpsHandler;
import net.shortninja.staffplus.domain.staff.mode.handler.GadgetHandler;
import net.shortninja.staffplus.domain.staff.mute.MuteSessionTask;
import net.shortninja.staffplus.domain.staff.revive.ReviveHandler;
import net.shortninja.staffplus.domain.staff.warn.warnings.WarningExpireTask;
import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplusplus.IStaffPlus;
import net.shortninja.staffplusplus.session.SessionManager;
import net.shortninja.staffplusplus.staffmode.chat.StaffChatService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import static org.bukkit.Bukkit.getScheduler;

public class StaffPlus extends JavaPlugin implements IStaffPlus {
    private static StaffPlus plugin;

    public IProtocol versionProtocol;

    public CpsHandler cpsHandler;
    public GadgetHandler gadgetHandler;
    public ReviveHandler reviveHandler;
    public CmdHandler cmdHandler;
    public UUID consoleUUID = UUID.fromString("9c417515-22bc-46b8-be4d-538482992f8f");
    public boolean usesPlaceholderAPI;
    private final DatabaseInitializer databaseInitializer = new DatabaseInitializer();
    private BukkitTask guiUpdateTask;

    private Tasks tasks;
    private MuteSessionTask muteSessionTask;
    private WarningExpireTask warningExpireTask;

    private ContextManager contextManager;
    private final List<ContextCalculator<Player>> registeredCalculators = new ArrayList<>();
    private boolean luckPermsEnabled;

    public static StaffPlus get() {
        return plugin;
    }

    @Override
    public void onLoad() {
        Plugin placeholderPlugin;
        if ((placeholderPlugin = Bukkit.getPluginManager().getPlugin("PlaceholderAPI")) != null) {
            usesPlaceholderAPI = true;
            Bukkit.getLogger().info("Hooked into PlaceholderAPI " + placeholderPlugin.getDescription().getVersion());
        }

    }

    @Override
    public void onEnable() {
        plugin = this;
        if (!setupVersionProtocol()) {
            IocContainer.getMessage().sendConsoleMessage("This version of Minecraft is not supported! If you have just updated to a brand new server version, check the Spigot plugin page.", true);
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if (!loadConfig()) {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        this.databaseInitializer.initialize();
        DataFile.init();

        cpsHandler = new CpsHandler();
        gadgetHandler = new GadgetHandler();
        reviveHandler = new ReviveHandler();
        cmdHandler = new CmdHandler();
        tasks = new Tasks();
        muteSessionTask = new MuteSessionTask();
        warningExpireTask = new WarningExpireTask();

        getScheduler().runTaskAsynchronously(this, () -> new UpdateNotifier().checkUpdate());


        Bukkit.getOnlinePlayers().forEach(player -> IocContainer.getSessionManager().initialize(player));
        BukkitUtils.initListeners();
        BungeeUtil.initListeners(this);
        MetricsUtil.initializeMetrics(this, IocContainer.getOptions());

        guiUpdateTask = getScheduler().runTaskTimer(this, () -> {
            for (PlayerSession playerSession : IocContainer.getSessionManager().getAll()) {
                if (playerSession.getCurrentGui().isPresent() && playerSession.getCurrentGui().get() instanceof UpdatableGui) {
                    ((UpdatableGui) playerSession.getCurrentGui().get()).update();
                }
            }
        }, 0, 10);

        enableLuckPermHooks();
        Bukkit.getServicesManager().register(IStaffPlus.class, this, this, ServicePriority.Normal);

        IocContainer.getMessage().sendConsoleMessage("Staff++ has been enabled! Initialization took " + (System.currentTimeMillis() - System.currentTimeMillis()) + "ms.", false);
        IocContainer.getMessage().sendConsoleMessage("Plugin created by Shortninja continued by Qball - Revisited by Garagepoort", false);
    }

    private boolean loadConfig() {
        IocContainer.init(this);
        saveDefaultConfig();
        // TODO need to refactor the creation of lang files
        IocContainer.getMessages();

        if(!AutoUpdater.updateConfig(this) || !AutoUpdaterLanguageFiles.updateConfig(this)) {
            Bukkit.getPluginManager().disablePlugin(this);
            return false;
        }
        return true;
    }

    @Override
    public void onDisable() {
        guiUpdateTask.cancel();
        IocContainer.getMessage().sendConsoleMessage("Staff++ is now disabling!", true);
        this.disableLuckPermHooks();
        stop();
    }

    public void saveUsers() {
        getScheduler().runTaskAsynchronously(StaffPlus.get(), () -> {
            for (PlayerSession session : IocContainer.getSessionManager().getAll()) {
                IocContainer.getSessionLoader().saveSessionSynchronous(session);
            }
        });
    }

    private boolean setupVersionProtocol() {
        final String version = Bukkit.getServer().getClass().getPackage().getName();
        final String formattedVersion = version.substring(version.lastIndexOf('.') + 1);
        versionProtocol = ProtocolFactory.getProtocol();
        IocContainer.getMessage().sendConsoleMessage("Version protocol set to '" + formattedVersion + "'.", false);
        return versionProtocol != null;
    }


    /*
     * Nullifying all of the instances is sort of an experimental thing to deal
     * with memory leaks that could occur on reloads (where instances could be
     * handled incorrectly)
     */


    private void stop() {
        saveUsers();
        if (IocContainer.getOptions().modeConfiguration.isModeDisableOnLogout()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                IocContainer.getModeCoordinator().removeMode(player);
            }
        }

        versionProtocol = null;
        cpsHandler = null;
        gadgetHandler = null;
        reviveHandler = null;
        cmdHandler = null;
        tasks = null;
        plugin = null;
    }

    public PermissionHandler getPermissions() {
        return IocContainer.getPermissionHandler();
    }

    @Override
    public StaffChatService getStaffChatService() {
        return IocContainer.getStaffChatService();
    }

    @Override
    public SessionManager getSessionManager() {
        return IocContainer.getSessionManager();
    }

    private void enableLuckPermHooks() {
        luckPermsEnabled = Bukkit.getPluginManager().getPlugin("LuckPerms") != null;
        if (luckPermsEnabled) {
            getLogger().info("Luckperms enabled");
            LuckPerms luckPerms = getServer().getServicesManager().load(LuckPerms.class);
            if (luckPerms == null) {
                throw new IllegalStateException("LuckPerms API not loaded.");
            }
            this.contextManager = luckPerms.getContextManager();
            this.register("StaffMode", StaffModeLuckPermsContextCalculator::new);
        }
    }

    private void disableLuckPermHooks() {
        if (luckPermsEnabled) {
            this.registeredCalculators.forEach(c -> this.contextManager.unregisterCalculator(c));
            this.registeredCalculators.clear();
        }
    }

    private void register(String option, Supplier<ContextCalculator<Player>> calculatorSupplier) {
        getLogger().info("Registering '" + option + "' calculator.");
        ContextCalculator<Player> calculator = calculatorSupplier.get();
        this.contextManager.registerCalculator(calculator);
        this.registeredCalculators.add(calculator);
    }


}