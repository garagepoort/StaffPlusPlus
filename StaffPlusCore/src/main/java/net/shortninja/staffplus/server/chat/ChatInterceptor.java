package net.shortninja.staffplus.server.chat;

import org.bukkit.event.player.AsyncPlayerChatEvent;

public interface ChatInterceptor {

    boolean intercept(AsyncPlayerChatEvent event);

}
