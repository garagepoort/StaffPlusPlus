package net.shortninja.staffplus.event;

import net.shortninja.staffplus.unordered.IReport;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ReopenReportEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * Report in question
     */
    private final IReport report;

    public ReopenReportEvent(IReport report) {
        this.report = report;
    }

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
