package net.shortninja.staffplus.staff.altaccountdetect.config;

import net.shortninja.staffplus.common.config.ConfigLoader;

public class AltDetectModuleLoader extends ConfigLoader<AltDetectConfiguration> {

    @Override
    public AltDetectConfiguration load() {
        boolean banEnabled = config.getBoolean("alt-detect-module.enabled");
        String bypassPermission = config.getString("permissions.alt-detect-bypass");
        String whitelistPermission = config.getString("permissions.alt-detect-whitelist");
        String commandWhitelist = config.getString("commands.alt-detect-whitelist");

        return new AltDetectConfiguration(banEnabled, bypassPermission, whitelistPermission, commandWhitelist);
    }
}
