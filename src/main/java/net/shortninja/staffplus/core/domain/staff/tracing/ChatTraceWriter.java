package net.shortninja.staffplus.core.domain.staff.tracing;

import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplusplus.trace.TraceOutputChannel;
import net.shortninja.staffplusplus.trace.TraceWriter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class ChatTraceWriter implements TraceWriter {


    private final Messages messages;
    private final UUID tracerUuid;

    public ChatTraceWriter(UUID tracerUuid, Messages messages) {
        this.tracerUuid = tracerUuid;

        this.messages = messages;
    }

    @Override
    public void writeToTrace(String message) {
        Player player = Bukkit.getPlayer(tracerUuid);
        if(player != null) {
            String traceMessage = "[" + LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_DATE_TIME) + "] : " + message;
            this.messages.send(player, traceMessage, messages.prefixTrace);
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
    public TraceOutputChannel getType() {
        return TraceOutputChannel.CHAT;
    }
}
