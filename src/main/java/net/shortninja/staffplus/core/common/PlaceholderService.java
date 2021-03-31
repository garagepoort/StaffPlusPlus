package net.shortninja.staffplus.core.common;

import be.garagepoort.mcioc.IocBean;
import me.clip.placeholderapi.PlaceholderAPI;
import net.shortninja.staffplus.core.StaffPlus;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

@IocBean
public class PlaceholderService {

    private boolean usesPlaceholderAPI = false;

    public PlaceholderService() {
        Plugin placeholderPlugin;
        if ((placeholderPlugin = Bukkit.getPluginManager().getPlugin("PlaceholderAPI")) != null) {
            usesPlaceholderAPI = true;
            StaffPlus.get().getLogger().info("Hooked into PlaceholderAPI " + placeholderPlugin.getDescription().getVersion());
        }
    }

    public String setPlaceholders(CommandSender sender, String message) {
        if (!usesPlaceholderAPI || !(sender instanceof Player)) {
            return message;
        }
        return PlaceholderAPI.setPlaceholders((OfflinePlayer) sender, message);
    }
}
