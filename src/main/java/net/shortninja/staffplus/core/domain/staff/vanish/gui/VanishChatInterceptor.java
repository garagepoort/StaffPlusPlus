package net.shortninja.staffplus.core.domain.staff.vanish.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.domain.chat.ChatAction;
import net.shortninja.staffplus.core.domain.chat.ChatInterceptor;
import net.shortninja.staffplus.core.domain.staff.vanish.VanishConfiguration;
import net.shortninja.staffplus.core.domain.staff.vanish.VanishServiceImpl;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@IocBean
@IocMultiProvider(ChatInterceptor.class)
public class VanishChatInterceptor implements ChatInterceptor {

    private final VanishServiceImpl vanishServiceImpl;
    private final VanishConfiguration vanishConfiguration;

    private final Messages messages;
    private final OnlineSessionsManager sessionManager;

    public VanishChatInterceptor(VanishServiceImpl vanishServiceImpl, VanishConfiguration vanishConfiguration, Messages messages, OnlineSessionsManager sessionManager) {
        this.vanishServiceImpl = vanishServiceImpl;
        this.vanishConfiguration = vanishConfiguration;
        this.messages = messages;
        this.sessionManager = sessionManager;
    }

    @Override
    public boolean intercept(AsyncPlayerChatEvent event) {
        OnlinePlayerSession session = sessionManager.get(event.getPlayer());
        ChatAction chatAction = session.getChatAction();

        if (chatAction != null) {
            return false;
        }
        if (vanishConfiguration.vanishEnabled && !vanishConfiguration.vanishChatEnabled && vanishServiceImpl.isVanished(event.getPlayer())) {
            this.messages.send(event.getPlayer(), messages.chatPrevented, messages.prefixGeneral);
            return true;
        }
        return false;
    }

    @Override
    public int getPriority() {
        return 4;
    }
}
