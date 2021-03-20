package net.shortninja.staffplus.domain.staff.broadcast.config;

import java.util.List;

import static net.shortninja.staffplus.domain.staff.broadcast.config.BroadcastSelector.ALL;
import static net.shortninja.staffplus.domain.staff.broadcast.config.BroadcastSelector.CURRENT;

public class BroadcastConfiguration {
    private final boolean enabled;
    private final List<String> enabledServers;
    private final String prefix;

    public BroadcastConfiguration(boolean enabled, List<String> enabledServers, String prefix) {
        this.enabled = enabled;
        this.enabledServers = enabledServers;
        this.prefix = prefix;
    }

    public boolean isEnabled() {
        return enabled;
    }

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
