package net.shortninja.staffplus.event.warnings;

import net.shortninja.staffplus.unordered.IWarning;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WarningExpiredEvent extends Event {


    private static final HandlerList HANDLERS = new HandlerList();

    private final IWarning warning;

    public WarningExpiredEvent(IWarning warning) {
        this.warning = warning;
    }

    public IWarning getWarning() {
        return warning;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
