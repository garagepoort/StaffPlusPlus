package net.shortninja.staffplus.core.domain.chatchannels.bungee.events;

import net.shortninja.staffplus.core.domain.chatchannels.bungee.dto.ChatChannelBungeeDto;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ChatChannelClosedBungeeEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final ChatChannelBungeeDto chatChannelBungeeDto;

    public ChatChannelClosedBungeeEvent(ChatChannelBungeeDto chatChannelBungeeDto) {
        this.chatChannelBungeeDto = chatChannelBungeeDto;
    }

    public ChatChannelBungeeDto getChannel() {
        return chatChannelBungeeDto;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
