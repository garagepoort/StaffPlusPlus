package net.shortninja.staffplus.event.warnings;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class WarningsClearedEvent extends Event {


    private static final HandlerList HANDLERS = new HandlerList();
    private final String issuerName;
    private final UUID issuerUuid;
    private final String playerName;
    private final UUID playerUuid;


    public WarningsClearedEvent(String issuerName, UUID issuerUuid, String playerName, UUID playerUuid) {
        this.issuerName = issuerName;
        this.issuerUuid = issuerUuid;
        this.playerName = playerName;
        this.playerUuid = playerUuid;
    }

    public String getIssuerName() {
        return issuerName;
    }

    public UUID getIssuerUuid() {
        return issuerUuid;
    }

    public String getPlayerName() {
        return playerName;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
