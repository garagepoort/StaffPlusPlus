package net.shortninja.staffplus.domain.staff.tracing;

import net.shortninja.staffplus.domain.chat.ChatInterceptor;
import net.shortninja.staffplus.common.config.Messages;
import net.shortninja.staffplus.common.config.Options;
import net.shortninja.staffplus.common.utils.MessageCoordinator;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;

import static net.shortninja.staffplusplus.trace.TraceOutputChannel.CHAT;

public class TraceChatInterceptor implements ChatInterceptor {

    private final TraceService traceService;
    private final Messages messages;
    private final MessageCoordinator message;
    private Options options;

    public TraceChatInterceptor(TraceService traceService, Messages messages, MessageCoordinator message, Options options) {
        this.traceService = traceService;
        this.messages = messages;
        this.message = message;
        this.options = options;
    }

    @Override
    public boolean intercept(AsyncPlayerChatEvent event) {
        if (traceService.isPlayerTracing(event.getPlayer()) && options.traceConfiguration.hasChannel(CHAT)) {
            this.message.send(event.getPlayer(), messages.chatPrevented, messages.prefixGeneral);
            return true;
        }

        if(options.traceConfiguration.hasChannel(CHAT)) {
            List<Player> allTracers = traceService.getTracingPlayers();
            event.getRecipients().removeAll(allTracers);
        }
        return false;
    }
}
