package net.shortninja.staffplus.core.domain.staff.altaccountdetect.config;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.config.AbstractConfigLoader;

@IocBean
public class AltDetectModuleLoader extends AbstractConfigLoader<AltDetectConfiguration> {

    @Override
    protected AltDetectConfiguration load() {
        boolean banEnabled = defaultConfig.getBoolean("alt-detect-module.enabled");
        String bypassPermission = defaultConfig.getString("permissions.alt-detect-bypass");
        String whitelistPermission = defaultConfig.getString("permissions.alt-detect-whitelist");
        String commandWhitelist = defaultConfig.getString("commands.alt-detect-whitelist");

        return new AltDetectConfiguration(banEnabled, bypassPermission, whitelistPermission, commandWhitelist);
    }
}
