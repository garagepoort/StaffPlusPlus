package net.shortninja.staffplus.core.domain.player;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.domain.chat.ChatAction;
import net.shortninja.staffplus.core.domain.chat.ChatInterceptor;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import static net.shortninja.staffplus.core.common.cmd.CommandUtil.playerAction;

@IocBean
@IocMultiProvider(ChatInterceptor.class)
public class ChatActionChatInterceptor implements ChatInterceptor {
    private final OnlineSessionsManager sessionManager;

    public ChatActionChatInterceptor(OnlineSessionsManager sessionManager) {
        this.sessionManager = sessionManager;
    }


    @Override
    public boolean intercept(AsyncPlayerChatEvent event) {
        OnlinePlayerSession session = sessionManager.get(event.getPlayer());
        ChatAction chatAction = session.getChatAction();

        if (chatAction != null) {
            playerAction(event.getPlayer(), () -> {
                chatAction.execute(event.getPlayer(), event.getMessage());
                session.setChatAction(null);
            });
            return true;
        }
        return false;
    }

    @Override
    public int getPriority() {
        return 1;
    }
}
