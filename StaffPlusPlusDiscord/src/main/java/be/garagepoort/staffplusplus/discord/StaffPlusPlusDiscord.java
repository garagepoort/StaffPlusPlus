package be.garagepoort.staffplusplus.discord;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class StaffPlusPlusDiscord extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("StaffPlusPlusDiscord plugin enabled");
        saveDefaultConfig();
        FileConfiguration config = getConfig();

        if(config.getString("StaffPlusPlusDiscord.webhookUrl") == null || config.getString("StaffPlusPlusDiscord.webhookUrl").isEmpty()) {
            throw new RuntimeException("Cannot enable StaffPlusPlusDiscord. No webhookUrl provided in the configuration");
        }
        getServer().getPluginManager().registerEvents(new ReportListener(config), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("StaffPlusPlusDiscord plugin disabled");
    }
}
