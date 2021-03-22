package net.shortninja.staffplus.core.domain.staff.altaccountdetect.config;

import net.shortninja.staffplus.core.common.config.ConfigLoader;
import org.bukkit.configuration.file.FileConfiguration;

public class AltDetectModuleLoader extends ConfigLoader<AltDetectConfiguration> {

    @Override
    protected AltDetectConfiguration load(FileConfiguration config) {
        boolean banEnabled = config.getBoolean("alt-detect-module.enabled");
        String bypassPermission = config.getString("permissions.alt-detect-bypass");
        String whitelistPermission = config.getString("permissions.alt-detect-whitelist");
        String commandWhitelist = config.getString("commands.alt-detect-whitelist");

        return new AltDetectConfiguration(banEnabled, bypassPermission, whitelistPermission, commandWhitelist);
    }
}
