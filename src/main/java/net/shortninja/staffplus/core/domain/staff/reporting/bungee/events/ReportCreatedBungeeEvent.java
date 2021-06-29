package net.shortninja.staffplus.core.domain.staff.reporting.bungee.events;

import net.shortninja.staffplus.core.domain.staff.reporting.bungee.dto.ReportBungeeDto;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ReportCreatedBungeeEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final ReportBungeeDto report;

    public ReportCreatedBungeeEvent(ReportBungeeDto report) {
        this.report = report;
    }

    public ReportBungeeDto getReport() {
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
