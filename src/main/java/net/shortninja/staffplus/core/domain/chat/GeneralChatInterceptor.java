package net.shortninja.staffplus.core.domain.chat;

import net.shortninja.staffplus.core.application.IocBean;
import net.shortninja.staffplus.core.application.IocMultiProvider;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.utils.MessageCoordinator;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@IocBean
@IocMultiProvider(ChatInterceptor.class)
public class GeneralChatInterceptor implements ChatInterceptor {

    private final ChatHandler chatHandler;
    private final MessageCoordinator message;
    private final Messages messages;

    public GeneralChatInterceptor(ChatHandler chatHandler, MessageCoordinator message, Messages messages) {
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
