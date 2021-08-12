package net.shortninja.staffplus.core.domain.staff.freeze;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.domain.chat.ChatInterceptor;
import net.shortninja.staffplusplus.session.IPlayerSession;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@IocBean
@IocMultiProvider(ChatInterceptor.class)
public class FreezeChatInterceptor implements ChatInterceptor {

    private final Options options;
    private final Messages messages;
    private final OnlineSessionsManager onlineSessionsManager;


    public FreezeChatInterceptor(Options options, Messages messages, OnlineSessionsManager onlineSessionsManager) {
        this.options = options;
        this.messages = messages;
        this.onlineSessionsManager = onlineSessionsManager;
    }

    @Override
    public boolean intercept(AsyncPlayerChatEvent event) {
        IPlayerSession session = onlineSessionsManager.get(event.getPlayer().getUniqueId());
        if (session.isFrozen() && !options.staffItemsConfiguration.getFreezeModeConfiguration().isModeFreezeChat()) {
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
