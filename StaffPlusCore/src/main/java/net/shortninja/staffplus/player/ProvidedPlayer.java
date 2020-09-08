package net.shortninja.staffplus.player;

import java.util.UUID;

public class ProvidedPlayer {

    private UUID id;
    private String username;

    public ProvidedPlayer(UUID id, String username) {
        this.id = id;
        this.username = username;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
