package net.shortninja.staffplus.core.domain.chatchannels.bungee.events;

import net.shortninja.staffplus.core.domain.chatchannels.bungee.dto.ChatChannelMessageBungeeDto;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ChatChannelMessageReceivedBungeeEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final ChatChannelMessageBungeeDto chatChannelMessageBungeeDto;

    public ChatChannelMessageReceivedBungeeEvent(ChatChannelMessageBungeeDto chatChannelMessageBungeeDto) {
        this.chatChannelMessageBungeeDto = chatChannelMessageBungeeDto;
    }

    public ChatChannelMessageBungeeDto getChatChannelMessageBungee() {
        return chatChannelMessageBungeeDto;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
