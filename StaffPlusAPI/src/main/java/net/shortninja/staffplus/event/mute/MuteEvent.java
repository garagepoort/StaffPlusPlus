package net.shortninja.staffplus.event.mute;

import net.shortninja.staffplus.unordered.IMute;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MuteEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final IMute mute;

    public MuteEvent(IMute mute) {
        this.mute = mute;
    }

    public IMute getMute() {
        return mute;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
