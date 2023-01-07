package net.shortninja.staffplus.core.domain.staff.reporting.bungee.events;

import net.shortninja.staffplus.core.domain.staff.reporting.bungee.dto.ReportDeletedBungeeDto;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ReportDeletedBungeeEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final ReportDeletedBungeeDto reportDeleted;

    public ReportDeletedBungeeEvent(ReportDeletedBungeeDto reportDeleted) {
        this.reportDeleted = reportDeleted;
    }

    public ReportDeletedBungeeDto getReportDeleted() {
        return reportDeleted;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
