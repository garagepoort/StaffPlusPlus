package net.shortninja.staffplus.staff.chests.config;

import net.shortninja.staffplus.common.config.ConfigLoader;
import org.bukkit.configuration.file.FileConfiguration;

public class EnderchestsModuleLoader extends ConfigLoader<EnderchestsConfiguration> {

    @Override
    protected EnderchestsConfiguration load(FileConfiguration config) {
        boolean enabled = config.getBoolean("enderchest-module.enabled");
        String commandOpenEnderChests = config.getString("commands.echest_view");
        String permissionViewOnline = config.getString("permissions.enderchests.view.online");
        String permissionViewOffline = config.getString("permissions.enderchests.view.offline");
        String permissionInteract = config.getString("permissions.enderchests.interact");

        return new EnderchestsConfiguration(enabled, commandOpenEnderChests, permissionViewOnline, permissionViewOffline, permissionInteract);
    }
}
