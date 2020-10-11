package net.shortninja.staffplus.event.altdetect;

import net.shortninja.staffplus.unordered.altdetect.IAltDetectResult;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AltDetectEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final IAltDetectResult altDetectResult;

    public AltDetectEvent(IAltDetectResult altDetectResult) {
        super(true);
        this.altDetectResult = altDetectResult;
    }

    public IAltDetectResult getAltDetectResult() {
        return altDetectResult;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
