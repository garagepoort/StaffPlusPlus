package net.shortninja.staffplus.core.domain.blacklist.bungee;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ChatMessageCensoredBungeeEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final ChatMessageCensoredBungeeDto chatMessageCensoredBungeeDto;

    public ChatMessageCensoredBungeeEvent(ChatMessageCensoredBungeeDto chatMessageCensoredBungeeDto) {
        this.chatMessageCensoredBungeeDto = chatMessageCensoredBungeeDto;
    }

    public ChatMessageCensoredBungeeDto getChatMessageCensoredBungeeDto() {
        return chatMessageCensoredBungeeDto;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
