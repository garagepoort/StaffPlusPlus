package net.shortninja.staffplus.staff.tracing;

import net.shortninja.staffplus.server.chat.ChatInterceptor;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.util.MessageCoordinator;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;

import static net.shortninja.staffplus.unordered.trace.TraceOutputChannel.CHAT;

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
