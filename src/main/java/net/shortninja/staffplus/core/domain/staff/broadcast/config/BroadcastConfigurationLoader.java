package net.shortninja.staffplus.core.domain.staff.broadcast.config;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.AbstractConfigLoader;

import java.util.Arrays;
import java.util.List;

@IocBean
public class BroadcastConfigurationLoader extends AbstractConfigLoader<BroadcastConfiguration> {
    @Override
    protected BroadcastConfiguration load() {
        boolean enabled = defaultConfig.getBoolean("broadcast-module.enabled");
        List<String> enabledServers = Arrays.asList(defaultConfig.getString("broadcast-module.enabled-servers", "").split(";"));
        String prefix = defaultConfig.getString("broadcast-module.prefix", "&dBroadcast &8»");
        return new BroadcastConfiguration(enabled, enabledServers, prefix);
    }
}
