package net.shortninja.staffplus.core;

import be.garagepoort.mcioc.TubingPlugin;
import net.shortninja.staffplus.core.application.bootstrap.PluginDisable;
import net.shortninja.staffplus.core.application.config.AutoUpdater;
import net.shortninja.staffplus.core.application.config.ConfigurationFile;
import net.shortninja.staffplusplus.IStaffPlus;
import net.shortninja.staffplusplus.ban.BanService;
import net.shortninja.staffplusplus.mute.MuteService;
import net.shortninja.staffplusplus.reports.ReportService;
import net.shortninja.staffplusplus.session.SessionManager;
import net.shortninja.staffplusplus.staffmode.chat.StaffChatService;
import net.shortninja.staffplusplus.warnings.WarningService;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.ServicePriority;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StaffPlus extends TubingPlugin implements IStaffPlus {

    private static StaffPlus plugin;
    private List<ConfigurationFile> configurationFiles;

    public static StaffPlus get() {
        return plugin;
    }

    @Override
    protected void enable() {
        try {
            plugin = this;
            if (!loadConfig()) {
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }

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
    protected void beforeReload() {
        getIocContainer().getList(PluginDisable.class).forEach(b -> b.disable(this));
        loadConfig();
    }

    @Override
    public Map<String, FileConfiguration> getFileConfigurations() {
        return configurationFiles.stream()
            .collect(Collectors.toMap(ConfigurationFile::getIdentifier, ConfigurationFile::getFileConfiguration, (a, b) -> a));
    }

    private boolean loadConfig() {
        saveDefaultConfig();
        configurationFiles = Arrays.asList(
            new ConfigurationFile("config.yml"),
            new ConfigurationFile("configuration/permissions.yml"),
            new ConfigurationFile("configuration/commands.yml"),
            new ConfigurationFile("configuration/staffmode/modules.yml"),
            new ConfigurationFile("configuration/staffmode/custom-modules.yml"),
            new ConfigurationFile("configuration/staffmode/modes.yml"),
            new ConfigurationFile("lang/lang_de.yml"),
            new ConfigurationFile("lang/lang_en.yml"),
            new ConfigurationFile("lang/lang_es.yml"),
            new ConfigurationFile("lang/lang_fr.yml"),
            new ConfigurationFile("lang/lang_hr.yml"),
            new ConfigurationFile("lang/lang_hu.yml"),
            new ConfigurationFile("lang/lang_it.yml"),
            new ConfigurationFile("lang/lang_nl.yml"),
            new ConfigurationFile("lang/lang_no.yml"),
            new ConfigurationFile("lang/lang_pt.yml"),
            new ConfigurationFile("lang/lang_sv.yml"),
            new ConfigurationFile("lang/lang_zh.yml"),
            new ConfigurationFile("lang/lang_id.yml")
        );

        AutoUpdater.runMigrations(configurationFiles);
        for (ConfigurationFile c : configurationFiles) {
            if (!AutoUpdater.updateConfig(c)) {
                Bukkit.getPluginManager().disablePlugin(this);
                return false;
            }
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