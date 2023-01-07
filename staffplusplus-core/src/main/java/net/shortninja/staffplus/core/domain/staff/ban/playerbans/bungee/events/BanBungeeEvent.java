package net.shortninja.staffplus.core.domain.staff.ban.playerbans.bungee.events;

import net.shortninja.staffplus.core.domain.staff.ban.playerbans.bungee.dto.BanBungeeDto;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BanBungeeEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final BanBungeeDto ban;

    public BanBungeeEvent(BanBungeeDto ban) {
        this.ban = ban;
    }

    public BanBungeeDto getBan() {
        return ban;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
