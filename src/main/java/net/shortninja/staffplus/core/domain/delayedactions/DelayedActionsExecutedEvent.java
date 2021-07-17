package net.shortninja.staffplus.core.domain.delayedactions;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;

public class DelayedActionsExecutedEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final List<DelayedAction> delayedActions;

    public DelayedActionsExecutedEvent(Player player, List<DelayedAction> delayedActions) {
        this.player = player;
        this.delayedActions = delayedActions;
    }

    public Player getPlayer() {
        return player;
    }

    public List<DelayedAction> getDelayedActions() {
        return delayedActions;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
