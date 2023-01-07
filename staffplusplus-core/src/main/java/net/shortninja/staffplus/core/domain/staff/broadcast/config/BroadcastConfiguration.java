package net.shortninja.staffplus.core.domain.staff.broadcast.config;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.configuration.ConfigTransformer;
import net.shortninja.staffplus.core.application.config.SplitBySemicolon;

import java.util.List;

import static net.shortninja.staffplus.core.domain.staff.broadcast.config.BroadcastSelector.ALL;
import static net.shortninja.staffplus.core.domain.staff.broadcast.config.BroadcastSelector.CURRENT;

@IocBean
public class BroadcastConfiguration {

    @ConfigProperty("broadcast-module.prefix")
    private String prefix;
    @ConfigProperty("broadcast-module.enabled-servers")
    @ConfigTransformer(SplitBySemicolon.class)
    private List<String> enabledServers;

    public List<String> getEnabledServers() {
        return enabledServers;
    }

    public String getPrefix() {
        return prefix;
    }

    public boolean sendToAll() {
        return enabledServers.size() == 1 && enabledServers.get(0).equalsIgnoreCase(ALL.name());
    }

    public boolean sendToCurrent() {
        return enabledServers.size() == 1 && enabledServers.get(0).equalsIgnoreCase(CURRENT.name());
    }

    public boolean multipleServers() {
        return enabledServers.size() > 1;
    }
}
