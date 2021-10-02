package net.shortninja.staffplus.core.domain.staff.freeze;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.domain.chat.ChatInterceptor;
import net.shortninja.staffplus.core.domain.staff.freeze.config.FreezeConfiguration;
import net.shortninja.staffplusplus.session.IPlayerSession;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@IocBean(conditionalOnProperty = "freeze-module.enabled=true")
@IocMultiProvider(ChatInterceptor.class)
public class FreezeChatInterceptor implements ChatInterceptor {

    private final Messages messages;
    private final OnlineSessionsManager onlineSessionsManager;
    private final FreezeConfiguration freezeConfiguration;

    public FreezeChatInterceptor(Messages messages, OnlineSessionsManager onlineSessionsManager, FreezeConfiguration freezeConfiguration) {
        this.messages = messages;
        this.onlineSessionsManager = onlineSessionsManager;
        this.freezeConfiguration = freezeConfiguration;
    }

    @Override
    public boolean intercept(AsyncPlayerChatEvent event) {
        IPlayerSession session = onlineSessionsManager.get(event.getPlayer().getUniqueId());
        if (session.isFrozen() && !freezeConfiguration.chat) {
            this.messages.send(event.getPlayer(), messages.chatPrevented, messages.prefixGeneral);
            return true;
        }
        return false;
    }

    @Override
    public int getPriority() {
        return 2;
    }
}
