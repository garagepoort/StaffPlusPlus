package net.shortninja.staffplus.core.domain.staff.chests.config;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.config.AbstractConfigLoader;

@IocBean
public class EnderchestsModuleLoader extends AbstractConfigLoader<EnderchestsConfiguration> {

    @Override
    protected EnderchestsConfiguration load() {
        boolean enabled = defaultConfig.getBoolean("enderchest-module.enabled");
        String commandOpenEnderChests = commandsConfig.getString("commands.echest_view");
        String permissionViewOnline = permissionsConfig.getString("permissions.enderchests.view.online");
        String permissionViewOffline = permissionsConfig.getString("permissions.enderchests.view.offline");
        String permissionInteract = permissionsConfig.getString("permissions.enderchests.interact");

        return new EnderchestsConfiguration(enabled, commandOpenEnderChests, permissionViewOnline, permissionViewOffline, permissionInteract);
    }
}
