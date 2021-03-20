package net.shortninja.staffplus.domain.chat;

import org.bukkit.event.player.AsyncPlayerChatEvent;

public interface ChatInterceptor {

    /**
     * @param event The AsyncPlayerChatEvent
     * @return boolean indicating if this interceptor cancels the event. If true the event is cancelled immediately
     *         and no other interceptors will be executed.
     */
    boolean intercept(AsyncPlayerChatEvent event);

}
