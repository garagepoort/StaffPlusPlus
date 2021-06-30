package net.shortninja.staffplus.core.domain.staff.tracing;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;

import net.shortninja.staffplus.core.domain.chat.ChatInterceptor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;

import static net.shortninja.staffplusplus.trace.TraceOutputChannel.CHAT;

@IocBean
@IocMultiProvider(ChatInterceptor.class)
public class TraceChatInterceptor implements ChatInterceptor {

    private final TraceService traceService;
    private final Messages messages;

    private Options options;

    public TraceChatInterceptor(TraceService traceService, Messages messages, Options options) {
        this.traceService = traceService;
        this.messages = messages;

        this.options = options;
    }

    @Override
    public boolean intercept(AsyncPlayerChatEvent event) {
        if (traceService.isPlayerTracing(event.getPlayer()) && options.traceConfiguration.hasChannel(CHAT)) {
            this.messages.send(event.getPlayer(), messages.chatPrevented, messages.prefixGeneral);
            return true;
        }

        if(options.traceConfiguration.hasChannel(CHAT)) {
            List<Player> allTracers = traceService.getTracingPlayers();
            event.getRecipients().removeAll(allTracers);
        }
        return false;
    }

    @Override
    public int getPriority() {
        return 8;
    }
}
