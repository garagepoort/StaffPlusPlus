package net.shortninja.staffplus.core.domain.staff.vanish;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.AbstractConfigLoader;

@IocBean
public class VanishModuleLoader extends AbstractConfigLoader<VanishConfiguration> {
    @Override
    protected VanishConfiguration load() {
        boolean vanishEnabled = defaultConfig.getBoolean("vanish-module.enabled");
        boolean vanishTabList = defaultConfig.getBoolean("vanish-module.tab-list");
        boolean vanishShowAway = defaultConfig.getBoolean("vanish-module.show-away");
        boolean vanishChatEnabled = defaultConfig.getBoolean("vanish-module.chat");
        boolean vanishMessageEnabled = defaultConfig.getBoolean("vanish-module.vanish-message-enabled");

        String permissionSeeVanished = permissionsConfig.getString("permissions.see-vanished");
        String permissionVanishCommand = permissionsConfig.getString("permissions.vanish");
        String permissionVanishTotal = permissionsConfig.getString("permissions.vanish-total");
        String permissionVanishList = permissionsConfig.getString("permissions.vanish-list");
        String permissionVanishPlayer = permissionsConfig.getString("permissions.vanish-player");

        String commandVanish = commandsConfig.getString("commands.vanish");
        return new VanishConfiguration(vanishEnabled, vanishTabList, vanishShowAway, vanishChatEnabled, vanishMessageEnabled, permissionVanishCommand, permissionSeeVanished, permissionVanishTotal, permissionVanishList, permissionVanishPlayer, commandVanish);
    }
}
