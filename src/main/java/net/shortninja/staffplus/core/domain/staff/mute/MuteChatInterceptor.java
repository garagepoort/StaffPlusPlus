package net.shortninja.staffplus.core.domain.staff.mute;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.config.Messages;

import net.shortninja.staffplus.core.domain.chat.ChatInterceptor;
import net.shortninja.staffplus.core.session.PlayerSession;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@IocBean
@IocMultiProvider(ChatInterceptor.class)
public class MuteChatInterceptor implements ChatInterceptor {

    private final Messages messages;
    private final SessionManagerImpl sessionManager;

    public MuteChatInterceptor(SessionManagerImpl sessionManager, Messages messages) {
        this.sessionManager = sessionManager;

        this.messages = messages;
    }

    @Override
    public boolean intercept(AsyncPlayerChatEvent event) {
        PlayerSession playerSession = sessionManager.get(event.getPlayer().getUniqueId());
        if(playerSession.isMuted()) {
            this.messages.send(event.getPlayer(), messages.muted, messages.prefixGeneral);
            return true;
        }
        return false;
    }
}
