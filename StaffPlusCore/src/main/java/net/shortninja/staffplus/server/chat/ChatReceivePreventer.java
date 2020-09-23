package net.shortninja.staffplus.server.chat;

import org.bukkit.event.player.AsyncPlayerChatEvent;

public interface ChatReceivePreventer {

    void preventReceival(AsyncPlayerChatEvent event);

}
