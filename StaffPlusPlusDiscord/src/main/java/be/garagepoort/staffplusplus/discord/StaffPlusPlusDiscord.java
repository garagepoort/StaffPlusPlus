package be.garagepoort.staffplusplus.discord;

import be.garagepoort.staffplusplus.discord.altdetect.AltDetectionListener;
import be.garagepoort.staffplusplus.discord.ban.BanListener;
import be.garagepoort.staffplusplus.discord.kick.KickListener;
import be.garagepoort.staffplusplus.discord.mute.MuteListener;
import be.garagepoort.staffplusplus.discord.reports.ReportListener;
import be.garagepoort.staffplusplus.discord.warnings.AppealListener;
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

        saveResource("discordtemplates/reports/report-created.json", false);
        saveResource("discordtemplates/reports/report-accepted.json", false);
        saveResource("discordtemplates/reports/report-resolved.json", false);
        saveResource("discordtemplates/reports/report-rejected.json", false);
        saveResource("discordtemplates/reports/report-reopened.json", false);

        saveResource("discordtemplates/warnings/warning-created.json", false);
        saveResource("discordtemplates/warnings/threshold-reached.json", false);
        saveResource("discordtemplates/warnings/appeals/appeal-created.json", false);
        saveResource("discordtemplates/warnings/appeals/appeal-approved.json", false);
        saveResource("discordtemplates/warnings/appeals/appeal-rejected.json", false);

        saveResource("discordtemplates/bans/banned.json", false);
        saveResource("discordtemplates/bans/unbanned.json", false);

        saveResource("discordtemplates/mutes/muted.json", false);
        saveResource("discordtemplates/mutes/unmuted.json", false);

        saveResource("discordtemplates/kicks/kicked.json", false);
        saveResource("discordtemplates/altdetects/detected.json", false);

        ConfigUpdater.updateConfig(this);
        FileConfiguration config = getConfig();

        initListener(new ReportListener(config), "Cannot enable StaffPlusPlusDiscord. No report webhookUrl provided in the configuration.");
        initListener(new WarningListener(config), "Cannot enable StaffPlusPlusDiscord. No warning webhookUrl provided in the configuration.");
        initListener(new BanListener(config), "Cannot enable StaffPlusPlusDiscord. No bans webhookUrl provided in the configuration.");
        initListener(new KickListener(config), "Cannot enable StaffPlusPlusDiscord. No kicks webhookUrl provided in the configuration.");
        initListener(new MuteListener(config), "Cannot enable StaffPlusPlusDiscord. No mutes webhookUrl provided in the configuration.");
        initListener(new AppealListener(config), "Cannot enable StaffPlusPlusDiscord. No warning appeals webhookUrl provided in the configuration.");
        initListener(new AltDetectionListener(config), "Cannot enable StaffPlusPlusDiscord. No altDetect webhookUrl provided in the configuration.");

    }

    private void initListener(StaffPlusPlusListener listener, String errorMessage) {
        if (listener.isEnabled()) {
            if (!listener.isValid()) {
                showError(errorMessage);
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }
            listener.init();
            getServer().getPluginManager().registerEvents(listener, this);
        }
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
