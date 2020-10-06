package be.garagepoort.staffplusplus.trello;

import be.garagepoort.staffplusplus.trello.reports.ReportListener;
import be.garagepoort.staffplusplus.trello.repository.database.DatabaseInitializer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class StaffPlusPlusTrello extends JavaPlugin {

    private static StaffPlusPlusTrello plugin;

    public static Plugin get() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        getLogger().info("StaffPlusPlusTrello plugin enabled");
        saveDefaultConfig();
        ConfigUpdater.updateConfig(this);
        FileConfiguration config = getConfig();

        new DatabaseInitializer().initialize();

        ReportListener reportListener = new ReportListener(config);
        reportListener.init();

        getServer().getPluginManager().registerEvents(reportListener, this);
    }

    @Override
    public void onDisable() {
        getLogger().info("StaffPlusPlusTrello plugin disabled");
    }
}
