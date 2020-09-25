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

        ReportListener reportListener = new ReportListener(config);
        WarningListener warningListener = new WarningListener(config);
//        TraceListener traceListener = new TraceListener(config);

        if(reportListener.isEnabled() &&
                (config.getString("StaffPlusPlusDiscord.webhookUrl") == null || config.getString("StaffPlusPlusDiscord.webhookUrl").isEmpty())) {
            throw new RuntimeException("Cannot enable StaffPlusPlusDiscord. No report webhookUrl provided in the configuration.");
        }

        if(warningListener.isEnabled() &&
                (config.getString("StaffPlusPlusDiscord.warnings.webhookUrl") == null || config.getString("StaffPlusPlusDiscord.warnings.webhookUrl").isEmpty())) {
            throw new RuntimeException("Cannot enable StaffPlusPlusDiscord. No warning webhookUrl provided in the configuration.");
        }
//        if(traceListener.isEnabled() &&
//                (config.getString("StaffPlusPlusDiscord.trace.webhookUrl") == null || config.getString("StaffPlusPlusDiscord.trace.webhookUrl").isEmpty())) {
//            throw new RuntimeException("Cannot enable StaffPlusPlusDiscord. No trace webhookUrl provided in the configuration.");
//        }

        getServer().getPluginManager().registerEvents(reportListener, this);
        getServer().getPluginManager().registerEvents(warningListener, this);
//        getServer().getPluginManager().registerEvents(traceListener, this);
    }

    @Override
    public void onDisable() {
        getLogger().info("StaffPlusPlusDiscord plugin disabled");
    }
}
