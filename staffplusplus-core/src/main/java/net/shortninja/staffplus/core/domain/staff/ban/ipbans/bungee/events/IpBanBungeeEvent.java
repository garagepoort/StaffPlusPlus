package net.shortninja.staffplus.core.domain.staff.ban.ipbans.bungee.events;

import net.shortninja.staffplus.core.domain.staff.ban.ipbans.bungee.dto.IpBanBungeeDto;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class IpBanBungeeEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final IpBanBungeeDto ban;
    private final String template;

    public IpBanBungeeEvent(IpBanBungeeDto ban) {
        this.ban = ban;
        this.template = ban.getTemplate();
    }

    public IpBanBungeeDto getBan() {
        return ban;
    }

    public String getKickTemplate() {
        return template;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
