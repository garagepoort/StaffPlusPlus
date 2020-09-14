package be.garagepoort.staffplusplus.discord;

import org.bukkit.plugin.java.JavaPlugin;

public class StaffPlusPlusDiscord extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("StaffPlusPlusDiscord plugin enabled");

        getServer().getPluginManager().registerEvents(new ReportListener(), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("StaffPlusPlusDiscord plugin disabled");
    }
}
