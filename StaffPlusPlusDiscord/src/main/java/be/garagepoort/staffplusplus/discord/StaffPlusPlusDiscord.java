package be.garagepoort.staffplusplus.discord;

import be.garagepoort.staffplusplus.discord.altdetect.AltDetectionListener;
import be.garagepoort.staffplusplus.discord.ban.BanListener;
import be.garagepoort.staffplusplus.discord.reports.ReportListener;
import be.garagepoort.staffplusplus.discord.warnings.WarningListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class StaffPlusPlusDiscord extends JavaPlugin {

    private static StaffPlusPlusDiscord plugin;


    public static StaffPlusPlusDiscord get() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        getLogger().info("StaffPlusPlusDiscord plugin enabled");
        saveDefaultConfig();
        ConfigUpdater.updateConfig(this);
        FileConfiguration config = getConfig();

        ReportListener reportListener = new ReportListener(config);
        WarningListener warningListener = new WarningListener(config);
        BanListener banListener = new BanListener(config);
        AltDetectionListener altDetectionListener = new AltDetectionListener(config);

        if (reportListener.isEnabled() &&
            (config.getString("StaffPlusPlusDiscord.webhookUrl") == null || config.getString("StaffPlusPlusDiscord.webhookUrl").isEmpty())) {
            showError("Cannot enable StaffPlusPlusDiscord. No report webhookUrl provided in the configuration.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if (warningListener.isEnabled() &&
            (config.getString("StaffPlusPlusDiscord.warnings.webhookUrl") == null || config.getString("StaffPlusPlusDiscord.warnings.webhookUrl").isEmpty())) {
            showError("Cannot enable StaffPlusPlusDiscord. No warning webhookUrl provided in the configuration.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if (banListener.isEnabled() &&
            (config.getString("StaffPlusPlusDiscord.bans.webhookUrl") == null || config.getString("StaffPlusPlusDiscord.bans.webhookUrl").isEmpty())) {
            showError("Cannot enable StaffPlusPlusDiscord. No bans webhookUrl provided in the configuration.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if (altDetectionListener.isEnabled() &&
            (config.getString("StaffPlusPlusDiscord.altDetect.webhookUrl") == null || config.getString("StaffPlusPlusDiscord.altDetect.webhookUrl").isEmpty())) {
            showError("Cannot enable StaffPlusPlusDiscord. No altDetect webhookUrl provided in the configuration.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        reportListener.init();
        warningListener.init();
        banListener.init();
        altDetectionListener.init();

        getServer().getPluginManager().registerEvents(reportListener, this);
        getServer().getPluginManager().registerEvents(warningListener, this);
        getServer().getPluginManager().registerEvents(banListener, this);
        getServer().getPluginManager().registerEvents(altDetectionListener, this);
    }

    private void showError(String errorMessage) {
        getLogger().severe("=============================================================================================");
        getLogger().severe("!!!  " + errorMessage);
        getLogger().severe("=============================================================================================");
    }

    @Override
    public void onDisable() {
        getLogger().info("StaffPlusPlusDiscord plugin disabled");
    }
}
