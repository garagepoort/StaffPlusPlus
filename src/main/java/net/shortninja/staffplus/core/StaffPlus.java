package net.shortninja.staffplus.core;

import be.garagepoort.mcioc.TubingPlugin;
import net.shortninja.staffplus.core.application.bootstrap.PluginDisable;
import net.shortninja.staffplus.core.common.config.AutoUpdater;
import net.shortninja.staffplus.core.common.config.AutoUpdaterLanguageFiles;
import net.shortninja.staffplusplus.IStaffPlus;
import net.shortninja.staffplusplus.session.SessionManager;
import net.shortninja.staffplusplus.staffmode.chat.StaffChatService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

public class StaffPlus extends TubingPlugin implements IStaffPlus {

    @Override
    protected void enable() {
        saveDefaultConfig();
        if (!loadConfig()) {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        Bukkit.getServicesManager().register(IStaffPlus.class, this, this, ServicePriority.Normal);

        getLogger().info("Staff++ has been enabled!");
        getLogger().info("Plugin created by Shortninja continued by Qball - Revisited by Garagepoort");
    }

    @Override
    protected void disable() {
        getLogger().info("Staff++ is now disabling!");
        getIocContainer().getList(PluginDisable.class).forEach(b -> b.disable(this));
        getLogger().info("Staff++ disabled!");
    }

    @Override
    protected void beforeReload() {
        getIocContainer().getList(PluginDisable.class).forEach(b -> b.disable(this));
    }

    private boolean loadConfig() {
        if (!AutoUpdater.updateConfig(this) || !AutoUpdaterLanguageFiles.updateConfig(this)) {
            Bukkit.getPluginManager().disablePlugin(this);
            return false;
        }
        return true;
    }

    @Override
    public StaffChatService getStaffChatService() {
        return StaffPlus.get().getIocContainer().get(StaffChatService.class);
    }

    @Override
    public SessionManager getSessionManager() {
        return StaffPlus.get().getIocContainer().get(SessionManager.class);
    }

}