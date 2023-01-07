package net.shortninja.staffplus.core.domain.staff.tracing;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.domain.chat.ChatInterceptor;
import net.shortninja.staffplus.core.domain.staff.tracing.config.TraceConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;

import static net.shortninja.staffplusplus.trace.TraceOutputChannel.CHAT;

@IocBean(conditionalOnProperty = "trace-module.enabled=true")
@IocMultiProvider(ChatInterceptor.class)
public class TraceChatInterceptor implements ChatInterceptor {

    private final TraceService traceService;
    private final Messages messages;
    private final TraceConfiguration traceConfiguration;

    public TraceChatInterceptor(TraceService traceService,
                                Messages messages,
                                TraceConfiguration traceConfiguration) {
        this.traceService = traceService;
        this.messages = messages;
        this.traceConfiguration = traceConfiguration;
    }

    @Override
    public boolean intercept(AsyncPlayerChatEvent event) {
        boolean hasChatChannel = traceConfiguration.hasChannel(CHAT);

        if (traceService.isPlayerTracing(event.getPlayer()) && hasChatChannel) {
            this.messages.send(event.getPlayer(), messages.chatPrevented, messages.prefixGeneral);
            return true;
        }

        if(hasChatChannel) {
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
