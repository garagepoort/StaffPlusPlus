package net.shortninja.staffplus.core.domain.chatchannels.bungee.events;

import net.shortninja.staffplus.core.domain.chatchannels.bungee.dto.ChatChannelPlayerJoinedBungeeDto;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ChatChannelPlayerJoinedBungeeEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final ChatChannelPlayerJoinedBungeeDto chatChannelPlayerJoinedBungeeDto;

    public ChatChannelPlayerJoinedBungeeEvent(ChatChannelPlayerJoinedBungeeDto chatChannelPlayerJoinedBungeeDto) {
        this.chatChannelPlayerJoinedBungeeDto = chatChannelPlayerJoinedBungeeDto;
    }

    public ChatChannelPlayerJoinedBungeeDto getChatChannelPlayerJoinedBungeeDto() {
        return chatChannelPlayerJoinedBungeeDto;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
