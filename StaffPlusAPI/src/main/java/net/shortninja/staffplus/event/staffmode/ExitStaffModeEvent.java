package net.shortninja.staffplus.event.staffmode;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class ExitStaffModeEvent extends Event {


    private static final HandlerList HANDLERS = new HandlerList();

    private final String playerName;
    private final UUID playerUuid;
    private final Location location;
    private final String serverName;

    public ExitStaffModeEvent(String playerName, UUID playerUuid, Location location, String serverName) {
        this.playerName = playerName;
        this.playerUuid = playerUuid;
        this.location = location;
        this.serverName = serverName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public Location getLocation() {
        return location;
    }

    public String getServerName() {
        return serverName;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
