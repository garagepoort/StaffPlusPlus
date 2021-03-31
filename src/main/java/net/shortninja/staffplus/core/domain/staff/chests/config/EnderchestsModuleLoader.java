package net.shortninja.staffplus.core.domain.staff.chests.config;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.config.AbstractConfigLoader;

import org.bukkit.configuration.file.FileConfiguration;

@IocBean
public class EnderchestsModuleLoader extends AbstractConfigLoader<EnderchestsConfiguration> {

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
