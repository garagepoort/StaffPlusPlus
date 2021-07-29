package net.shortninja.staffplus.core.domain.staff.ban.ipbans.bungee.events;

import net.shortninja.staffplus.core.domain.staff.ban.ipbans.bungee.dto.IpBanBungeeDto;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class IpUnbanBungeeEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final IpBanBungeeDto ban;

    public IpUnbanBungeeEvent(IpBanBungeeDto ban) {
        this.ban = ban;
    }

    public IpBanBungeeDto getBan() {
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
