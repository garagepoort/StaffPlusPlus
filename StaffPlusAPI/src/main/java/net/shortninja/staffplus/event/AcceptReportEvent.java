package net.shortninja.staffplus.event;

import net.shortninja.staffplus.unordered.IReport;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AcceptReportEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final IReport report;

    public AcceptReportEvent(IReport report) {
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
