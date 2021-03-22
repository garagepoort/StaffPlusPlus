package net.shortninja.staffplus.core.domain.staff.freeze;

import net.shortninja.staffplus.core.application.IocBean;
import net.shortninja.staffplus.core.application.IocMultiProvider;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.utils.MessageCoordinator;
import net.shortninja.staffplus.core.domain.chat.ChatInterceptor;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@IocBean
@IocMultiProvider(ChatInterceptor.class)
public class FreezeChatInterceptor implements ChatInterceptor {

    private final FreezeHandler freezeHandler;
    private final Options options;
    private final Messages messages;
    private final MessageCoordinator message;

    public FreezeChatInterceptor(FreezeHandler freezeHandler, Options options, Messages messages, MessageCoordinator message) {
        this.freezeHandler = freezeHandler;
        this.options = options;
        this.messages = messages;
        this.message = message;
    }

    @Override
    public boolean intercept(AsyncPlayerChatEvent event) {
        if (freezeHandler.isFrozen(event.getPlayer().getUniqueId()) && !options.modeConfiguration.getFreezeModeConfiguration().isModeFreezeChat()) {
            this.message.send(event.getPlayer(), messages.chatPrevented, messages.prefixGeneral);
            return true;
        }
        return false;
    }
}
