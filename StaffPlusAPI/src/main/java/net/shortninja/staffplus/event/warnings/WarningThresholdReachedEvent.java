package net.shortninja.staffplus.event.warnings;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;
import java.util.UUID;

public class WarningThresholdReachedEvent extends Event {


    private static final HandlerList HANDLERS = new HandlerList();
    private final UUID playerUuid;
    private final String playerName;
    private int thresholdScore;
    private List<String> commandsTriggered;


    public WarningThresholdReachedEvent(String playerName, UUID playerUuid, int thresholdScore, List<String> commandsTriggered) {
        this.playerName = playerName;
        this.playerUuid = playerUuid;
        this.thresholdScore = thresholdScore;
        this.commandsTriggered = commandsTriggered;
    }

    public int getThresholdScore() {
        return thresholdScore;
    }

    public List<String> getCommandsTriggered() {
        return commandsTriggered;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public String getPlayerName() {
        return playerName;
    }
}
