package net.shortninja.staffplus.core.domain.chatchannels.bungee.events;

import net.shortninja.staffplus.core.domain.chatchannels.bungee.dto.ChatChannelPlayerLeftBungeeDto;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ChatChannelPlayerLeftBungeeEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final ChatChannelPlayerLeftBungeeDto chatChannelPlayerLeftBungeeDto;

    public ChatChannelPlayerLeftBungeeEvent(ChatChannelPlayerLeftBungeeDto chatChannelPlayerLeftBungeeDto) {
        this.chatChannelPlayerLeftBungeeDto = chatChannelPlayerLeftBungeeDto;
    }

    public ChatChannelPlayerLeftBungeeDto getChatChannelPlayerLeftBungeeDto() {
        return chatChannelPlayerLeftBungeeDto;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
