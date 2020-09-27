package net.shortninja.staffplus.staff.broadcast.config;

import java.util.List;

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
}
