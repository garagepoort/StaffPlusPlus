package net.shortninja.staffplus.core.domain.staff.mute.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.domain.chat.ChatInterceptor;
import net.shortninja.staffplus.core.domain.staff.mute.Mute;
import net.shortninja.staffplus.core.domain.staff.mute.MuteService;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Optional;

@IocBean
@IocMultiProvider(ChatInterceptor.class)
public class MuteChatInterceptor implements ChatInterceptor {

    private final OnlineSessionsManager sessionManager;
    private final MuteService muteService;
    private final MuteChatNotifier muteChatNotifier;

    public MuteChatInterceptor(OnlineSessionsManager sessionManager, MuteService muteService, MuteChatNotifier muteChatNotifier) {
        this.sessionManager = sessionManager;
        this.muteService = muteService;
        this.muteChatNotifier = muteChatNotifier;
    }

    @Override
    public boolean intercept(AsyncPlayerChatEvent event) {
        OnlinePlayerSession playerSession = sessionManager.get(event.getPlayer());
        if (!playerSession.isMuted()) {
            return false;
        }
        Optional<Mute> mute = muteService.getMuteByMutedUuid(event.getPlayer().getUniqueId());
        if (!mute.isPresent()) {
            return false;
        }

        muteChatNotifier.notifyPlayerMuted(mute.get(), event.getPlayer());
        if (mute.get().isSoftMute()) {
            event.getRecipients().removeIf(p -> p.getUniqueId() != event.getPlayer().getUniqueId());
            return false;
        }
        return true;
    }

    @Override
    public int getPriority() {
        return 3;
    }
}
