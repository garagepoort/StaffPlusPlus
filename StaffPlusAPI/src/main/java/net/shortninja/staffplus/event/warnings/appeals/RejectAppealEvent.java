package net.shortninja.staffplus.event.warnings.appeals;

import net.shortninja.staffplus.unordered.IWarning;
import net.shortninja.staffplus.unordered.IWarningAppeal;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RejectAppealEvent extends Event {


    private static final HandlerList HANDLERS = new HandlerList();

    private final IWarning warning;
    private final IWarningAppeal warningAppeal;

    public RejectAppealEvent(IWarning warning, IWarningAppeal warningAppeal) {
        this.warning = warning;
        this.warningAppeal = warningAppeal;
    }

    public IWarning getWarning() {
        return warning;
    }

    public IWarningAppeal getWarningAppeal() {
        return warningAppeal;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
