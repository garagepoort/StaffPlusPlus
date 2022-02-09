package net.shortninja.staffplus.core.domain.chatchannels.bungee.events;

import net.shortninja.staffplus.core.domain.chatchannels.bungee.dto.ChatChannelCreatedBungeeDto;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ChatChannelCreatedBungeeEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final ChatChannelCreatedBungeeDto chatChannelCreatedBungeeDto;

    public ChatChannelCreatedBungeeEvent(ChatChannelCreatedBungeeDto chatChannelCreatedBungeeDto) {
        this.chatChannelCreatedBungeeDto = chatChannelCreatedBungeeDto;
    }

    public ChatChannelCreatedBungeeDto getChatChannelCreatedBungeeDto() {
        return chatChannelCreatedBungeeDto;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
