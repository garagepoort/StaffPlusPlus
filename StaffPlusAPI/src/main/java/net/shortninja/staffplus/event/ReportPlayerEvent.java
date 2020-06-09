package net.shortninja.staffplus.event;

import net.shortninja.staffplus.unordered.IReport;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
<<<<<<< HEAD
 *  This event is called when a new unresolved report is added
=======
 * This event is called when a new unresolved report is added
>>>>>>> b2eb803718fc6d2d09f3ef627210b17920278857
 */
public class ReportPlayerEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * Report in question
     */
    private final IReport report;

    public ReportPlayerEvent(IReport report) {
        this.report = report;
    }

    /**
     * Returns the report
<<<<<<< HEAD
=======
     *
>>>>>>> b2eb803718fc6d2d09f3ef627210b17920278857
     * @return report
     */
    public IReport getReport() {
        return report;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
