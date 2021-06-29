package net.shortninja.staffplus.core.domain.staff.reporting.bungee.events;

import net.shortninja.staffplus.core.domain.staff.reporting.bungee.dto.ReportReopenedBungeeDto;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ReportReopenedBungeeEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final ReportReopenedBungeeDto reopenedBungeeDto;

    public ReportReopenedBungeeEvent(ReportReopenedBungeeDto reopenedBungeeDto) {
        this.reopenedBungeeDto = reopenedBungeeDto;
    }

    public ReportReopenedBungeeDto getReopenedBungeeDto() {
        return reopenedBungeeDto;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
