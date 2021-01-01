package net.shortninja.staffplus.staff.kick.config;

import net.shortninja.staffplus.common.config.ConfigLoader;
import org.bukkit.configuration.file.FileConfiguration;

public class KickModuleLoader extends ConfigLoader<KickConfiguration> {

    @Override
    protected KickConfiguration load(FileConfiguration config) {
        boolean kickEnabled = config.getBoolean("kick-module.enabled");

        String commandKickPlayer = config.getString("commands.kick");
        String permissionKickPlayer = config.getString("permissions.kick");
        String permissionKickByPass = config.getString("permissions.kick-bypass");

        return new KickConfiguration(kickEnabled, commandKickPlayer, permissionKickPlayer, permissionKickByPass);
    }
}
