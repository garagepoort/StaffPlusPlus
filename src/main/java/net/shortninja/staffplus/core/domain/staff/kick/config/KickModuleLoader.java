package net.shortninja.staffplus.core.domain.staff.kick.config;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.config.AbstractConfigLoader;

import org.bukkit.configuration.file.FileConfiguration;

@IocBean
public class KickModuleLoader extends AbstractConfigLoader<KickConfiguration> {

    @Override
    protected KickConfiguration load(FileConfiguration config) {
        boolean kickEnabled = config.getBoolean("kick-module.enabled");

        String commandKickPlayer = config.getString("commands.kick");
        String permissionKickPlayer = config.getString("permissions.kick");
        String permissionKickByPass = config.getString("permissions.kick-bypass");

        return new KickConfiguration(kickEnabled, commandKickPlayer, permissionKickPlayer, permissionKickByPass);
    }
}
