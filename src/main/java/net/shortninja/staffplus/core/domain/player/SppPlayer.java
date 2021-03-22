package net.shortninja.staffplus.core.domain.player;

import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

public class SppPlayer {

    private final UUID id;
    private final String username;
    private final boolean online;
    private Player player;

    public SppPlayer(UUID id, String username) {
        this.id = id;
        this.username = username;
        this.online = false;
    }

    public SppPlayer(UUID id, String username, Player player) {
        this.id = id;
        this.username = username;
        this.online = true;
        this.player = player;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public boolean isOnline() {
        return online;
    }

    public Player getPlayer() {
        if(!online) {
            throw new RuntimeException("Cannot retrieve bukkit player. Player is offline");
        }
        return player;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SppPlayer sppPlayer = (SppPlayer) o;
        return id.equals(sppPlayer.id) &&
            username.equals(sppPlayer.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }
}
