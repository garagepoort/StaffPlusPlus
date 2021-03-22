package net.shortninja.staffplus.core.domain.staff.mute;

import net.shortninja.staffplus.core.application.IocBean;
import net.shortninja.staffplus.core.application.IocMultiProvider;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.utils.MessageCoordinator;
import net.shortninja.staffplus.core.domain.chat.ChatInterceptor;
import net.shortninja.staffplus.core.session.PlayerSession;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@IocBean
@IocMultiProvider(ChatInterceptor.class)
public class MuteChatInterceptor implements ChatInterceptor {
    private final MessageCoordinator message;
    private final Messages messages;
    private final SessionManagerImpl sessionManager;

    public MuteChatInterceptor(SessionManagerImpl sessionManager, MessageCoordinator message, Messages messages) {
        this.sessionManager = sessionManager;
        this.message = message;
        this.messages = messages;
    }

    @Override
    public boolean intercept(AsyncPlayerChatEvent event) {
        PlayerSession playerSession = sessionManager.get(event.getPlayer().getUniqueId());
        if(playerSession.isMuted()) {
            this.message.send(event.getPlayer(), messages.muted, messages.prefixGeneral);
            return true;
        }
        return false;
    }
}
