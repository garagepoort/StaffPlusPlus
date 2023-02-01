package net.shortninja.staffplus.core.vanish.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.domain.chat.ChatInterceptor;
import net.shortninja.staffplus.core.vanish.VanishService;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@IocBean(conditionalOnProperty = "vanish-module.enabled=true && vanish-module.chat=true")
@IocMultiProvider(ChatInterceptor.class)
public class VanishChatInterceptor implements ChatInterceptor {

    @ConfigProperty("%lang%:vanish-chat-prevented")
    private String chatPrevented;

    private final VanishService vanishService;
    private final Messages messages;

    public VanishChatInterceptor(VanishService vanishService, Messages messages) {
        this.vanishService = vanishService;
        this.messages = messages;
    }

    @Override
    public boolean intercept(AsyncPlayerChatEvent event) {
        if (vanishService.isVanished(event.getPlayer())) {
            this.messages.send(event.getPlayer(), chatPrevented, messages.prefixGeneral);
            return true;
        }
        return false;
    }

    @Override
    public int getPriority() {
        return 4;
    }
}
