package net.shortninja.staffplus.staff.tracing;

import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.unordered.trace.TraceWriter;
import net.shortninja.staffplus.util.MessageCoordinator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TraceWriterFactory {

    private final MessageCoordinator message;
    private final Messages messages;

    public TraceWriterFactory(MessageCoordinator message, Messages messages) {
        this.message = message;
        this.messages = messages;
    }

    public List<TraceWriter> buildTraceWriters(UUID tracerUuid, UUID tracedUuid) {
        List<TraceWriter> traceWriters = new ArrayList<>();
        traceWriters.add(new ChatTraceWriter(tracerUuid, message, messages));
        traceWriters.add(new FileTraceWriter(tracedUuid));
        return traceWriters;
    }
}
