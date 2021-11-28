package net.shortninja.staffplus.core.domain.synchronization;

import java.util.List;

public class ServerSyncConfig {

    private final List<String> servers;
    private final boolean matchesAll;
    private final boolean disabled;

    public ServerSyncConfig(List<String> servers) {
        this.servers = servers;
        this.disabled = servers.size() <= 1;
        this.matchesAll = servers.contains("[ALL]");
    }

    public boolean isEnabled() {
        return !disabled;
    }

    public List<String> getServers() {
        return servers;
    }

    public boolean isMatchesAll() {
        return matchesAll;
    }

    public boolean matchesServer(String serverName) {
        return matchesAll || servers.contains(serverName);
    }
}
