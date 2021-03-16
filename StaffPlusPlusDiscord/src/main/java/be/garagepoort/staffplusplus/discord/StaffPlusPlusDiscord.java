package be.garagepoort.staffplusplus.discord;

import be.garagepoort.staffplusplus.discord.altdetect.AltDetectionListener;
import be.garagepoort.staffplusplus.discord.ban.BanListener;
import be.garagepoort.staffplusplus.discord.chat.ChatListener;
import be.garagepoort.staffplusplus.discord.common.TemplateRepository;
import be.garagepoort.staffplusplus.discord.kick.KickListener;
import be.garagepoort.staffplusplus.discord.mute.MuteListener;
import be.garagepoort.staffplusplus.discord.reports.ReportListener;
import be.garagepoort.staffplusplus.discord.staffchat.StaffChatListener;
import be.garagepoort.staffplusplus.discord.staffmode.StaffModeListener;
import be.garagepoort.staffplusplus.discord.warnings.AppealListener;
import be.garagepoort.staffplusplus.discord.warnings.WarningListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

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
        FileConfiguration config = getConfig();
        ConfigUpdater.updateConfig(this);
        if (!ConfigUpdater.updateConfig(this)) {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        TemplateRepository templateRepository = new TemplateRepository(config);
        initializeListeners(templateRepository, config);
        Objects.requireNonNull(this.getCommand("staffplusplusdiscord")).setExecutor(new StaffPlusPlusDiscordCmd());
    }

    public void reload() {
        reloadConfig();
        TemplateRepository templateRepository = new TemplateRepository(getConfig());
        HandlerList.unregisterAll(this);
        initializeListeners(templateRepository, getConfig());
    }

    private void initializeListeners(TemplateRepository templateRepository, FileConfiguration config) {
        initListener(new ReportListener(config, templateRepository), "Cannot enable StaffPlusPlusDiscord. No report webhookUrl provided in the configuration.");
        initListener(new WarningListener(config, templateRepository), "Cannot enable StaffPlusPlusDiscord. No warning webhookUrl provided in the configuration.");
        initListener(new BanListener(config, templateRepository), "Cannot enable StaffPlusPlusDiscord. No bans webhookUrl provided in the configuration.");
        initListener(new KickListener(config, templateRepository), "Cannot enable StaffPlusPlusDiscord. No kicks webhookUrl provided in the configuration.");
        initListener(new MuteListener(config, templateRepository), "Cannot enable StaffPlusPlusDiscord. No mutes webhookUrl provided in the configuration.");
        initListener(new AppealListener(config, templateRepository), "Cannot enable StaffPlusPlusDiscord. No warning appeals webhookUrl provided in the configuration.");
        initListener(new AltDetectionListener(config, templateRepository), "Cannot enable StaffPlusPlusDiscord. No altDetect webhookUrl provided in the configuration.");
        initListener(new StaffModeListener(config, templateRepository), "Cannot enable StaffPlusPlusDiscord. No staffmode webhookUrl provided in the configuration.");
        initListener(new ChatListener(config, templateRepository), "Cannot enable StaffPlusPlusDiscord. No chat webhookUrl provided in the configuration.");
        initListener(new StaffChatListener(config, plugin), "Cannot enable StaffPlusPlusDiscord. DiscordSRV plugin not enabled! Disable Staffchat sync or enable the DiscordSRV plugin.");
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
