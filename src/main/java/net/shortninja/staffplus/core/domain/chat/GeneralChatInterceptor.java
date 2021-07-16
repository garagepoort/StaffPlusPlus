package net.shortninja.staffplus.core.domain.chat;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.Messages;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@IocBean
@IocMultiProvider(ChatInterceptor.class)
public class GeneralChatInterceptor implements ChatInterceptor {

    private final ChatHandler chatHandler;

    private final Messages messages;

    public GeneralChatInterceptor(ChatHandler chatHandler, Messages messages) {
        this.chatHandler = chatHandler;

        this.messages = messages;
    }

    @Override
    public boolean intercept(AsyncPlayerChatEvent event) {
        if (!chatHandler.canChat(event.getPlayer())) {
            this.messages.send(event.getPlayer(), messages.chattingFast, messages.prefixGeneral);
            return true;
        }

        if (!chatHandler.isChatEnabled(event.getPlayer())) {
            this.messages.send(event.getPlayer(), messages.chatPrevented, messages.prefixGeneral);
            return true;
        }

        return false;
    }

    @Override
    public int getPriority() {
        return 5;
    }
}
