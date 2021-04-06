package net.shortninja.staffplus.core.domain.staff.kick.config;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.config.AbstractConfigLoader;

@IocBean
public class KickModuleLoader extends AbstractConfigLoader<KickConfiguration> {

    @Override
    protected KickConfiguration load() {
        boolean kickEnabled = defaultConfig.getBoolean("kick-module.enabled");

        String commandKickPlayer = commandsConfig.getString("commands.kick");
        String permissionKickPlayer = permissionsConfig.getString("permissions.kick");
        String permissionKickByPass = permissionsConfig.getString("permissions.kick-bypass");

        return new KickConfiguration(kickEnabled, commandKickPlayer, permissionKickPlayer, permissionKickByPass);
    }
}
