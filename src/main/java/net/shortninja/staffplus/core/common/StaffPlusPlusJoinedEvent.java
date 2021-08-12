package net.shortninja.staffplus.core.common;

import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettings;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerJoinEvent;

public class StaffPlusPlusJoinedEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final PlayerJoinEvent playerJoinEvent;
    private final OnlinePlayerSession playerSession;
    private final PlayerSettings playerSettings;

    public StaffPlusPlusJoinedEvent(PlayerJoinEvent playerJoinEvent, OnlinePlayerSession playerSession, PlayerSettings playerSettings) {
        this.playerJoinEvent = playerJoinEvent;
        this.playerSession = playerSession;
        this.playerSettings = playerSettings;
    }

    public PlayerJoinEvent getPlayerJoinEvent() {
        return playerJoinEvent;
    }

    public OnlinePlayerSession getPlayerSession() {
        return playerSession;
    }

    public PlayerSettings getPlayerSettings() {
        return playerSettings;
    }

    public Player getPlayer() {
        return playerJoinEvent.getPlayer();
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
