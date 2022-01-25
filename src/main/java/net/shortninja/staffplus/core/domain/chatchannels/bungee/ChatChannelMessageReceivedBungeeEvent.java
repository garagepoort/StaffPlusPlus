package net.shortninja.staffplus.core.domain.chatchannels.bungee;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ChatChannelMessageReceivedBungeeEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final ChatChannelMessageBungee chatChannelMessageBungee;

    public ChatChannelMessageReceivedBungeeEvent(ChatChannelMessageBungee chatChannelMessageBungee) {
        this.chatChannelMessageBungee = chatChannelMessageBungee;
    }

    public ChatChannelMessageBungee getChatChannelMessageBungee() {
        return chatChannelMessageBungee;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
