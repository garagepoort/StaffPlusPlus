package net.shortninja.staffplus.core.domain.staff.vanish;

import net.shortninja.staffplus.core.application.IocBean;
import net.shortninja.staffplus.core.application.IocMultiProvider;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.utils.MessageCoordinator;
import net.shortninja.staffplus.core.domain.chat.ChatInterceptor;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@IocBean
@IocMultiProvider(ChatInterceptor.class)
public class VanishChatInterceptor implements ChatInterceptor {

    private final VanishServiceImpl vanishServiceImpl;
    private final Options options;
    private final MessageCoordinator message;
    private final Messages messages;

    public VanishChatInterceptor(VanishServiceImpl vanishServiceImpl, Options options, MessageCoordinator message, Messages messages) {
        this.vanishServiceImpl = vanishServiceImpl;
        this.options = options;
        this.message = message;
        this.messages = messages;
    }

    @Override
    public boolean intercept(AsyncPlayerChatEvent event) {
        if (options.vanishEnabled && !options.vanishChatEnabled && vanishServiceImpl.isVanished(event.getPlayer())) {
            this.message.send(event.getPlayer(), messages.chatPrevented, messages.prefixGeneral);
            return true;
        }
        return false;
    }
}
