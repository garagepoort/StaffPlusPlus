package net.shortninja.staffplus.domain.staff.mute;

import net.shortninja.staffplus.domain.chat.ChatInterceptor;
import net.shortninja.staffplus.common.config.Messages;
import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.session.SessionManagerImpl;
import net.shortninja.staffplus.common.utils.MessageCoordinator;
import org.bukkit.event.player.AsyncPlayerChatEvent;

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
