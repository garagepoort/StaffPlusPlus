package net.shortninja.staffplus.staff.broadcast.config;

import net.shortninja.staffplus.common.config.ConfigLoader;

import java.util.Arrays;
import java.util.List;

public class BroadcastConfigurationLoader extends ConfigLoader<BroadcastConfiguration> {
    @Override
    public BroadcastConfiguration load() {
        boolean enabled = config.getBoolean("broadcast-module.enabled");
        List<String> enabledServers = Arrays.asList(config.getString("broadcast-module.enabled-servers", "").split(";"));
        String prefix = config.getString("broadcast-module.prefix", "&dBroadcast &8»");
        return new BroadcastConfiguration(enabled, enabledServers, prefix);
    }
}
