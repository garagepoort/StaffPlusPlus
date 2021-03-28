package net.shortninja.staffplus.core;

import be.garagepoort.mcioc.IocContainer;
import be.garagepoort.staffplusplus.craftbukkit.api.ProtocolFactory;
import be.garagepoort.staffplusplus.craftbukkit.common.IProtocol;
import net.shortninja.staffplus.core.application.bootstrap.PluginDisable;
import net.shortninja.staffplus.core.common.config.AutoUpdater;
import net.shortninja.staffplus.core.common.config.AutoUpdaterLanguageFiles;
import net.shortninja.staffplusplus.IStaffPlus;
import net.shortninja.staffplusplus.session.SessionManager;
import net.shortninja.staffplusplus.staffmode.chat.StaffChatService;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class StaffPlus extends JavaPlugin implements IStaffPlus {
    private static StaffPlus plugin;

    public IProtocol versionProtocol;
    public boolean usesPlaceholderAPI;
    public IocContainer iocContainer;
    public UUID consoleUUID = UUID.fromString("9c417515-22bc-46b8-be4d-538482992f8f");

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
        versionProtocol = ProtocolFactory.getProtocol();

        saveDefaultConfig();
        if (!loadConfig()) {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        initIocContainer();
        Bukkit.getServicesManager().register(IStaffPlus.class, this, this, ServicePriority.Normal);

        getLogger().info("Staff++ has been enabled!");
        getLogger().info("Plugin created by Shortninja continued by Qball - Revisited by Garagepoort");
    }

    @Override
    public void onDisable() {
        getLogger().info("Staff++ is now disabling!");
        iocContainer.getList(PluginDisable.class).forEach(b -> b.disable(this));
        getLogger().info("Staff++ disabled!");
    }

    private boolean loadConfig() {
        if (!AutoUpdater.updateConfig(this) || !AutoUpdaterLanguageFiles.updateConfig(this)) {
            Bukkit.getPluginManager().disablePlugin(this);
            return false;
        }
        return true;
    }


    public void reload() {
        reloadConfig();
        HandlerList.unregisterAll(this);
        iocContainer.getList(PluginDisable.class).forEach(b -> b.disable(this));
        initIocContainer();
    }

    private void initIocContainer() {
        iocContainer = new IocContainer();
        iocContainer.registerBean(versionProtocol);
        iocContainer.init("net.shortninja.staffplus.core", getConfig());
    }

    @Override
    public StaffChatService getStaffChatService() {
        return StaffPlus.get().iocContainer.get(StaffChatService.class);
    }

    @Override
    public SessionManager getSessionManager() {
        return StaffPlus.get().iocContainer.get(SessionManager.class);
    }

}