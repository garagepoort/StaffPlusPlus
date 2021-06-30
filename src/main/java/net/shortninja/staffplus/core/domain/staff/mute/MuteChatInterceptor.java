 package net.shortninja.staffplus.core.domain.staff.mute;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.Messages;

import net.shortninja.staffplus.core.domain.chat.ChatInterceptor;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.application.session.SessionManagerImpl;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import static net.shortninja.staffplus.core.domain.staff.mute.MuteMessageStringUtil.replaceMutePlaceholders;

@IocBean
@IocMultiProvider(ChatInterceptor.class)
public class MuteChatInterceptor implements ChatInterceptor {

    private final Messages messages;
    private final SessionManagerImpl sessionManager;
    private final MuteService muteService;

    public MuteChatInterceptor(SessionManagerImpl sessionManager, Messages messages, MuteService muteService) {
        this.sessionManager = sessionManager;
        this.messages = messages;
        this.muteService = muteService;
    }

    @Override
    public boolean intercept(AsyncPlayerChatEvent event) {
        PlayerSession playerSession = sessionManager.get(event.getPlayer().getUniqueId());
        if (playerSession.isMuted()) {
            muteService.getMuteByMutedUuid(event.getPlayer().getUniqueId()).ifPresent(mute -> {
                String message = replaceMutePlaceholders(messages.muted, mute);
                this.messages.send(event.getPlayer(), message, messages.prefixGeneral);
            });
            return true;
        }
        return false;
    }

    @Override
    public int getPriority() {
        return 3;
    }
}
