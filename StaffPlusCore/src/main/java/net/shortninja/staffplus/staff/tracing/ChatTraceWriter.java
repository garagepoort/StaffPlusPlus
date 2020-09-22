package net.shortninja.staffplus.staff.tracing;

import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.unordered.trace.TraceWriter;
import net.shortninja.staffplus.unordered.trace.TraceWriterType;
import net.shortninja.staffplus.util.MessageCoordinator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class ChatTraceWriter implements TraceWriter {

    private final MessageCoordinator message;
    private final Messages messages;
    private final UUID tracerUuid;

    public ChatTraceWriter(UUID tracerUuid, MessageCoordinator message, Messages messages) {
        this.tracerUuid = tracerUuid;
        this.message = message;
        this.messages = messages;
    }

    @Override
    public void writeToTrace(String message) {
        Player player = Bukkit.getPlayer(tracerUuid);
        if(player != null) {
            String traceMessage = "[" + LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_DATE_TIME) + "] : " + message;
            this.message.send(player, traceMessage, messages.prefixTrace);
        }
    }

    @Override
    public void stopTrace(){
        //No action when chat writer is stopped
    }

    @Override
    public String getResource() {
        return "CHAT";
    }

    @Override
    public TraceWriterType getType() {
        return TraceWriterType.CHAT;
    }
}
