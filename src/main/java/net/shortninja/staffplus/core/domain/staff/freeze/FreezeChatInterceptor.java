package net.shortninja.staffplus.core.domain.staff.freeze;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;

import net.shortninja.staffplus.core.domain.chat.ChatInterceptor;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@IocBean
@IocMultiProvider(ChatInterceptor.class)
public class FreezeChatInterceptor implements ChatInterceptor {

    private final FreezeHandler freezeHandler;
    private final Options options;
    private final Messages messages;


    public FreezeChatInterceptor(FreezeHandler freezeHandler, Options options, Messages messages) {
        this.freezeHandler = freezeHandler;
        this.options = options;
        this.messages = messages;

    }

    @Override
    public boolean intercept(AsyncPlayerChatEvent event) {
        if (freezeHandler.isFrozen(event.getPlayer().getUniqueId()) && !options.staffItemsConfiguration.getFreezeModeConfiguration().isModeFreezeChat()) {
            this.messages.send(event.getPlayer(), messages.chatPrevented, messages.prefixGeneral);
            return true;
        }
        return false;
    }
}
