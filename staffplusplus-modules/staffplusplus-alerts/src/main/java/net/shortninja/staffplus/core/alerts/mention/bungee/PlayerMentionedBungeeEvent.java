package net.shortninja.staffplus.core.alerts.mention.bungee;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerMentionedBungeeEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final MentionBungeeDto mentionBungeeDto;

    public PlayerMentionedBungeeEvent(MentionBungeeDto mentionBungeeDto) {
        this.mentionBungeeDto = mentionBungeeDto;
    }

    public MentionBungeeDto getMentionBungeeDto() {
        return mentionBungeeDto;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
