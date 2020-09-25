package net.shortninja.staffplus.server.chat;

import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.util.MessageCoordinator;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class GeneralChatPreventer implements ChatInterceptor {

    private final ChatHandler chatHandler;
    private final MessageCoordinator message;
    private final Messages messages;

    public GeneralChatPreventer(ChatHandler chatHandler, MessageCoordinator message, Messages messages) {
        this.chatHandler = chatHandler;
        this.message = message;
        this.messages = messages;
    }

    @Override
    public boolean intercept(AsyncPlayerChatEvent event) {
        if (!chatHandler.canChat(event.getPlayer())) {
            this.message.send(event.getPlayer(), messages.chattingFast, messages.prefixGeneral);
            return true;
        }

        if (!chatHandler.isChatEnabled(event.getPlayer())) {
            this.message.send(event.getPlayer(), messages.chatPrevented, messages.prefixGeneral);
            return true;
        }

        return false;
    }
}
