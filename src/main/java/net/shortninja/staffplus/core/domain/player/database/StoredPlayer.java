package net.shortninja.staffplus.core.domain.player.database;

import java.util.Set;
import java.util.UUID;

public class StoredPlayer {
    private int id;
    private final UUID uuid;
    private final String name;
    private final Set<String> servers;

    public StoredPlayer(int id, UUID uuid, String name, Set<String> servers) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.servers = servers;
    }

    public StoredPlayer(UUID uuid, String name, Set<String> servers) {
        this.uuid = uuid;
        this.name = name;
        this.servers = servers;
    }

    public int getId() {
        return id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public Set<String> getServers() {
        return servers;
    }

    public void addServer(String server) {
        this.servers.add(server);
    }
}
