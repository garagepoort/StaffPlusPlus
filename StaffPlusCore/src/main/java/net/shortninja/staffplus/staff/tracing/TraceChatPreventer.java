package net.shortninja.staffplus.staff.tracing;

import net.shortninja.staffplus.server.chat.ChatPreventer;
import net.shortninja.staffplus.server.chat.ChatReceivePreventer;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.util.MessageCoordinator;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;

import static net.shortninja.staffplus.unordered.trace.TraceOutputChannel.CHAT;

public class TraceChatPreventer implements ChatPreventer, ChatReceivePreventer {

    private final TraceService traceService;
    private final Messages messages;
    private final MessageCoordinator message;
    private Options options;

    public TraceChatPreventer(TraceService traceService, Messages messages, MessageCoordinator message, Options options) {
        this.traceService = traceService;
        this.messages = messages;
        this.message = message;
        this.options = options;
    }

    @Override
    public boolean shouldPrevent(Player player, String message) {
        if (traceService.isPlayerTracing(player) && options.traceConfiguration.hasChannel(CHAT)) {
            this.message.send(player, messages.chatPrevented, messages.prefixGeneral);
        }
        return false;
    }

    @Override
    public void preventReceival(AsyncPlayerChatEvent event) {
        if(options.traceConfiguration.hasChannel(CHAT)) {
            List<Player> allTracers = traceService.getTracingPlayers();
            event.getRecipients().removeAll(allTracers);
        }
    }
}
