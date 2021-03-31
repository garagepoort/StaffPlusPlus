package net.shortninja.staffplus.core.domain.staff.altaccountdetect.config;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.config.AbstractConfigLoader;

import org.bukkit.configuration.file.FileConfiguration;

@IocBean
public class AltDetectModuleLoader extends AbstractConfigLoader<AltDetectConfiguration> {

    @Override
    protected AltDetectConfiguration load(FileConfiguration config) {
        boolean banEnabled = config.getBoolean("alt-detect-module.enabled");
        String bypassPermission = config.getString("permissions.alt-detect-bypass");
        String whitelistPermission = config.getString("permissions.alt-detect-whitelist");
        String commandWhitelist = config.getString("commands.alt-detect-whitelist");

        return new AltDetectConfiguration(banEnabled, bypassPermission, whitelistPermission, commandWhitelist);
    }
}
