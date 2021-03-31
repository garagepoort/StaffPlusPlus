package net.shortninja.staffplus.core.domain.staff.investigate.bungee.events;

import net.shortninja.staffplus.core.domain.staff.investigate.bungee.InvestigationBungee;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class InvestigationConcludedBungeeEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final InvestigationBungee investigation;

    public InvestigationConcludedBungeeEvent(InvestigationBungee investigation) {
        this.investigation = investigation;
    }

    public InvestigationBungee getInvestigation() {
        return investigation;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
