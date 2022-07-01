package net.shortninja.staffplus.core;

import be.garagepoort.mcioc.tubingbukkit.TubingBukkitPlugin;
import net.shortninja.staffplus.core.application.bootstrap.PluginDisable;
import net.shortninja.staffplusplus.IStaffPlus;
import net.shortninja.staffplusplus.ban.BanService;
import net.shortninja.staffplusplus.mute.MuteService;
import net.shortninja.staffplusplus.reports.ReportService;
import net.shortninja.staffplusplus.session.SessionManager;
import net.shortninja.staffplusplus.staffmode.chat.StaffChatService;
import net.shortninja.staffplusplus.warnings.WarningService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

public class StaffPlus extends TubingBukkitPlugin implements IStaffPlus {

    private static StaffPlus plugin;

    public static StaffPlus get() {
        return plugin;
    }

    @Override
    protected void beforeEnable() {
        plugin = this;
    }

    @Override
    protected void enable() {
        try {
            Bukkit.getServicesManager().register(IStaffPlus.class, this, this, ServicePriority.Normal);

            getLogger().info("Staff++ has been enabled!");
            getLogger().info("Plugin created by Shortninja continued by Qball - Revisited by Garagepoort");
        } catch (Exception e) {
            getLogger().severe("Unable to load plugin: " + e.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    protected void disable() {
        getLogger().info("Staff++ is now disabling!");
        if (this.getIocContainer() != null && getIocContainer().getList(PluginDisable.class) != null) {
            getIocContainer().getList(PluginDisable.class).forEach(b -> b.disable(this));
        }
        getLogger().info("Staff++ disabled!");
    }

    @Override
    public StaffChatService getStaffChatService() {
        return StaffPlus.get().getIocContainer().get(StaffChatService.class);
    }

    @Override
    public SessionManager getSessionManager() {
        return StaffPlus.get().getIocContainer().get(SessionManager.class);
    }

    @Override
    public BanService getBanService() {
        return StaffPlus.get().getIocContainer().get(BanService.class);
    }

    @Override
    public MuteService getMuteService() {
        return StaffPlus.get().getIocContainer().get(MuteService.class);
    }

    @Override
    public ReportService getReportService() {
        return StaffPlus.get().getIocContainer().get(ReportService.class);
    }

    @Override
    public WarningService getWarningService() {
        return StaffPlus.get().getIocContainer().get(WarningService.class);
    }
}