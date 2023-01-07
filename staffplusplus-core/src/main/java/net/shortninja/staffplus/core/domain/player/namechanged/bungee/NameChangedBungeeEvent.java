package net.shortninja.staffplus.core.domain.player.namechanged.bungee;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NameChangedBungeeEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final NameChangeBungeeDto nameChangeBungeeDto;

    public NameChangedBungeeEvent(NameChangeBungeeDto nameChangeBungeeDto) {
        this.nameChangeBungeeDto = nameChangeBungeeDto;
    }

    public NameChangeBungeeDto getNameChangeBungeeDto() {
        return nameChangeBungeeDto;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
