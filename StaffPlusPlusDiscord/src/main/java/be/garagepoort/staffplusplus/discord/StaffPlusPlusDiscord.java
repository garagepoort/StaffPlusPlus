package be.garagepoort.staffplusplus.discord;

import be.garagepoort.staffplusplus.discord.reports.ReportListener;
import be.garagepoort.staffplusplus.discord.warnings.WarningListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class StaffPlusPlusDiscord extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("StaffPlusPlusDiscord plugin enabled");
        saveDefaultConfig();
        ConfigUpdater.updateConfig(this);
        FileConfiguration config = getConfig();

        if(config.getString("StaffPlusPlusDiscord.webhookUrl") == null || config.getString("StaffPlusPlusDiscord.webhookUrl").isEmpty()) {
            throw new RuntimeException("Cannot enable StaffPlusPlusDiscord. No webhookUrl provided in the configuration");
        }
        getServer().getPluginManager().registerEvents(new ReportListener(config), this);
        getServer().getPluginManager().registerEvents(new WarningListener(config), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("StaffPlusPlusDiscord plugin disabled");
    }
}
