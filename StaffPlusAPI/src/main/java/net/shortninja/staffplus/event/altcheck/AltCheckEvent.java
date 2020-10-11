package net.shortninja.staffplus.event.altcheck;

import net.shortninja.staffplus.unordered.altcheck.IAltDetectResult;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AltCheckEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final IAltDetectResult altCheckResult;

    public AltCheckEvent(IAltDetectResult altCheckResult) {
        super(true);
        this.altCheckResult = altCheckResult;
    }

    public IAltDetectResult getAltCheckResult() {
        return altCheckResult;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
