package net.shortninja.staffplus.event.kick;

import net.shortninja.staffplus.unordered.IKick;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class KickEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final IKick kick;

    public KickEvent(IKick kick) {
        this.kick = kick;
    }

    public IKick getKick() {
        return kick;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
