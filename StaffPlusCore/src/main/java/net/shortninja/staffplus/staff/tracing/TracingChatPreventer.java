package net.shortninja.staffplus.staff.tracing;

import net.shortninja.staffplus.server.chat.ChatPreventer;
import net.shortninja.staffplus.server.chat.ChatReceivePreventer;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.util.MessageCoordinator;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;

public class TracingChatPreventer implements ChatPreventer, ChatReceivePreventer {

    private final TraceService traceService;
    private final Messages messages;
    private final MessageCoordinator message;

    public TracingChatPreventer(TraceService traceService, Messages messages, MessageCoordinator message) {
        this.traceService = traceService;
        this.messages = messages;
        this.message = message;
    }

    @Override
    public boolean shouldPrevent(Player player, String message) {
        if (traceService.isPlayerTracing(player)) {
            this.message.send(player, messages.chatPrevented, messages.prefixGeneral);
        }
        return false;
    }

    @Override
    public void preventReceival(AsyncPlayerChatEvent event) {
        List<Player> tracingPlayers = traceService.getTracingPlayers();
        event.getRecipients().removeAll(tracingPlayers);
    }
}
