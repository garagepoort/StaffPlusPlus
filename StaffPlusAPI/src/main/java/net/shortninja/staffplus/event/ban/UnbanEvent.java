package net.shortninja.staffplus.event.ban;

import net.shortninja.staffplus.unordered.IBan;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UnbanEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final IBan ban;

    public UnbanEvent(IBan ban) {
        this.ban = ban;
    }

    public IBan getBan() {
        return ban;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
