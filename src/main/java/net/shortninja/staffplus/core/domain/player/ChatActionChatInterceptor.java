package net.shortninja.staffplus.core.domain.player;

import net.shortninja.staffplus.core.application.IocBean;
import net.shortninja.staffplus.core.application.IocMultiProvider;
import net.shortninja.staffplus.core.domain.chat.ChatAction;
import net.shortninja.staffplus.core.domain.chat.ChatInterceptor;
import net.shortninja.staffplus.core.session.PlayerSession;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import static net.shortninja.staffplus.core.common.cmd.CommandUtil.playerAction;

@IocBean
@IocMultiProvider(ChatInterceptor.class)
public class ChatActionChatInterceptor implements ChatInterceptor {
    private final SessionManagerImpl sessionManager;

    public ChatActionChatInterceptor(SessionManagerImpl sessionManager) {
        this.sessionManager = sessionManager;
    }


    @Override
    public boolean intercept(AsyncPlayerChatEvent event) {
        PlayerSession session = sessionManager.get(event.getPlayer().getUniqueId());
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
}
