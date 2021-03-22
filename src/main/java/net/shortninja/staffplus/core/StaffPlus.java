package net.shortninja.staffplus.core;

import be.garagepoort.staffplusplus.craftbukkit.api.ProtocolFactory;
import be.garagepoort.staffplusplus.craftbukkit.common.IProtocol;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.context.ContextCalculator;
import net.luckperms.api.context.ContextManager;
import net.shortninja.staffplus.core.application.IocContainer;
import net.shortninja.staffplus.core.application.data.DataFile;
import net.shortninja.staffplus.core.application.data.LanguageFile;
import net.shortninja.staffplus.core.application.metrics.MetricsUtil;
import net.shortninja.staffplus.core.application.updates.UpdateNotifier;
import net.shortninja.staffplus.core.common.UpdatableGui;
import net.shortninja.staffplus.core.common.bungee.BungeeUtil;
import net.shortninja.staffplus.core.common.cmd.CmdHandler;
import net.shortninja.staffplus.core.common.config.AutoUpdater;
import net.shortninja.staffplus.core.common.config.AutoUpdaterLanguageFiles;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.mode.StaffModeLuckPermsContextCalculator;
import net.shortninja.staffplus.core.domain.staff.mode.StaffModeService;
import net.shortninja.staffplus.core.domain.staff.mode.handler.CpsHandler;
import net.shortninja.staffplus.core.domain.staff.mode.handler.GadgetHandler;
import net.shortninja.staffplus.core.domain.staff.mute.MuteSessionTask;
import net.shortninja.staffplus.core.domain.staff.revive.ReviveHandler;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.WarningExpireTask;
import net.shortninja.staffplus.core.session.PlayerSession;
import net.shortninja.staffplus.core.session.SessionLoader;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import net.shortninja.staffplusplus.IStaffPlus;
import net.shortninja.staffplusplus.session.SessionManager;
import net.shortninja.staffplusplus.staffmode.chat.StaffChatService;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
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
    private BukkitTask guiUpdateTask;

    private ContextManager contextManager;
    private final List<ContextCalculator<Player>> registeredCalculators = new ArrayList<>();
    private boolean luckPermsEnabled;
    private FileConfiguration langFile;

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
            getLogger().severe("This version of Minecraft is not supported! If you have just updated to a brand new server version, check the Spigot plugin page.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // Create all config files
        saveDefaultConfig();
        langFile = new LanguageFile().get();
        DataFile.init();
        if (!loadConfig()) {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        IocContainer.registerBean(versionProtocol);
        IocContainer.init(this);

        cpsHandler = new CpsHandler();
        gadgetHandler = new GadgetHandler();
        reviveHandler = new ReviveHandler();
        cmdHandler = new CmdHandler();
        new Tasks();
        new MuteSessionTask();
        new WarningExpireTask();

        getScheduler().runTaskAsynchronously(this, () -> new UpdateNotifier().checkUpdate());


        Bukkit.getOnlinePlayers().forEach(player -> IocContainer.get(SessionManagerImpl.class).initialize(player));
        BukkitUtils.initListeners();
        BungeeUtil.initListeners(this);
        MetricsUtil.initializeMetrics(this, IocContainer.get(Options.class));

        guiUpdateTask = getScheduler().runTaskTimer(this, () -> {
            for (PlayerSession playerSession : IocContainer.get(SessionManagerImpl.class).getAll()) {
                if (playerSession.getCurrentGui().isPresent() && playerSession.getCurrentGui().get() instanceof UpdatableGui) {
                    ((UpdatableGui) playerSession.getCurrentGui().get()).update();
                }
            }
        }, 0, 10);

        enableLuckPermHooks();
        Bukkit.getServicesManager().register(IStaffPlus.class, this, this, ServicePriority.Normal);

        getLogger().info("Staff++ has been enabled! Initialization took " + (System.currentTimeMillis() - System.currentTimeMillis()) + "ms.");
        getLogger().info("Plugin created by Shortninja continued by Qball - Revisited by Garagepoort");
    }

    private boolean loadConfig() {
        if (!AutoUpdater.updateConfig(this) || !AutoUpdaterLanguageFiles.updateConfig(this)) {
            Bukkit.getPluginManager().disablePlugin(this);
            return false;
        }
        return true;
    }

    public FileConfiguration getLangFile() {
        return langFile;
    }

    @Override
    public void onDisable() {
        guiUpdateTask.cancel();
        getLogger().info("Staff++ is now disabling!");
        this.disableLuckPermHooks();
        stop();
    }

    public void saveUsers() {
        for (PlayerSession session : IocContainer.get(SessionManagerImpl.class).getAll()) {
            IocContainer.get(SessionLoader.class).saveSessionSynchronous(session);
        }
    }

    private boolean setupVersionProtocol() {
        versionProtocol = ProtocolFactory.getProtocol();
        return versionProtocol != null;
    }


    /*
     * Nullifying all of the instances is sort of an experimental thing to deal
     * with memory leaks that could occur on reloads (where instances could be
     * handled incorrectly)
     */


    private void stop() {
        saveUsers();
        if (IocContainer.get(Options.class).modeConfiguration.isModeDisableOnLogout()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                IocContainer.get(StaffModeService.class).removeMode(player);
            }
        }

        versionProtocol = null;
        cpsHandler = null;
        gadgetHandler = null;
        reviveHandler = null;
        cmdHandler = null;
        plugin = null;
    }

    public PermissionHandler getPermissions() {
        return IocContainer.get(PermissionHandler.class);
    }

    @Override
    public StaffChatService getStaffChatService() {
        return IocContainer.get(StaffChatService.class);
    }

    @Override
    public SessionManager getSessionManager() {
        return IocContainer.get(SessionManager.class);
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


    public void reloadLangFile() {
        this.langFile = new LanguageFile().get();
    }
}