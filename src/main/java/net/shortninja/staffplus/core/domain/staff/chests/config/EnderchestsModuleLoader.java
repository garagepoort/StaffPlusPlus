package net.shortninja.staffplus.core.domain.staff.chests.config;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.config.AbstractConfigLoader;

@IocBean
public class EnderchestsModuleLoader extends AbstractConfigLoader<EnderchestsConfiguration> {

    @Override
    protected EnderchestsConfiguration load() {
        boolean enabled = defaultConfig.getBoolean("enderchest-module.enabled");
        String commandOpenEnderChests = defaultConfig.getString("commands.echest_view");
        String permissionViewOnline = defaultConfig.getString("permissions.enderchests.view.online");
        String permissionViewOffline = defaultConfig.getString("permissions.enderchests.view.offline");
        String permissionInteract = defaultConfig.getString("permissions.enderchests.interact");

        return new EnderchestsConfiguration(enabled, commandOpenEnderChests, permissionViewOnline, permissionViewOffline, permissionInteract);
    }
}
