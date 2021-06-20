package net.shortninja.staffplus.core.domain.staff.altaccountdetect.config;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.config.AbstractConfigLoader;

@IocBean
public class AltDetectModuleLoader extends AbstractConfigLoader<AltDetectConfiguration> {

    @Override
    protected AltDetectConfiguration load() {
        boolean banEnabled = defaultConfig.getBoolean("alt-detect-module.enabled");
        boolean sameIpRequired = defaultConfig.getBoolean("alt-detect-module.same-ip-required");
        String bypassPermission = permissionsConfig.getString("permissions.alt-detect-bypass");
        String whitelistPermission = permissionsConfig.getString("permissions.alt-detect-whitelist");
        String commandWhitelist = commandsConfig.getString("commands.alt-detect-whitelist");

        return new AltDetectConfiguration(banEnabled, sameIpRequired, bypassPermission, whitelistPermission, commandWhitelist);
    }
}
