package net.shortninja.staffplus.core.domain.staff.vanish;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.domain.chat.ChatAction;
import net.shortninja.staffplus.core.domain.chat.ChatInterceptor;
import net.shortninja.staffplus.core.session.PlayerSession;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@IocBean
@IocMultiProvider(ChatInterceptor.class)
public class VanishChatInterceptor implements ChatInterceptor {

    private final VanishServiceImpl vanishServiceImpl;
    private final Options options;

    private final Messages messages;
    private final SessionManagerImpl sessionManager;

    public VanishChatInterceptor(VanishServiceImpl vanishServiceImpl, Options options, Messages messages, SessionManagerImpl sessionManager) {
        this.vanishServiceImpl = vanishServiceImpl;
        this.options = options;
        this.messages = messages;
        this.sessionManager = sessionManager;
    }

    @Override
    public boolean intercept(AsyncPlayerChatEvent event) {
        PlayerSession session = sessionManager.get(event.getPlayer().getUniqueId());
        ChatAction chatAction = session.getChatAction();

        if (chatAction != null) {
            return false;
        }
        if (options.vanishEnabled && !options.vanishChatEnabled && vanishServiceImpl.isVanished(event.getPlayer())) {
            this.messages.send(event.getPlayer(), messages.chatPrevented, messages.prefixGeneral);
            return true;
        }
        return false;
    }
}
