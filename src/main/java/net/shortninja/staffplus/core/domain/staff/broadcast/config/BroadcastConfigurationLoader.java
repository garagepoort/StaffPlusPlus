package net.shortninja.staffplus.core.domain.staff.broadcast.config;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.config.AbstractConfigLoader;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.List;

@IocBean
public class BroadcastConfigurationLoader extends AbstractConfigLoader<BroadcastConfiguration> {
    @Override
    protected BroadcastConfiguration load(FileConfiguration config) {
        boolean enabled = config.getBoolean("broadcast-module.enabled");
        List<String> enabledServers = Arrays.asList(config.getString("broadcast-module.enabled-servers", "").split(";"));
        String prefix = config.getString("broadcast-module.prefix", "&dBroadcast &8»");
        return new BroadcastConfiguration(enabled, enabledServers, prefix);
    }
}
