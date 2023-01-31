package net.shortninja.staffplus.core.tracing;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.tracing.config.TraceConfiguration;
import net.shortninja.staffplusplus.trace.TraceOutputChannel;
import net.shortninja.staffplusplus.trace.TraceWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@IocBean
public class TraceWriterFactory {

    private final Messages messages;
    private final TraceConfiguration traceConfiguration;

    public TraceWriterFactory(Messages messages,
                              TraceConfiguration traceConfiguration) {
        this.messages = messages;
        this.traceConfiguration = traceConfiguration;
    }

    public List<TraceWriter> buildTraceWriters(UUID tracerUuid, UUID tracedUuid) {
        List<TraceOutputChannel> outputChannels = traceConfiguration.getOutputChannels();

        List<TraceWriter> traceWriters = new ArrayList<>();
        if (outputChannels.contains(TraceOutputChannel.CHAT)) {
            traceWriters.add(new ChatTraceWriter(tracerUuid, messages));
        }
        if (outputChannels.contains(TraceOutputChannel.FILE)) {
            traceWriters.add(new FileTraceWriter(tracedUuid));
        }
        return traceWriters;
    }
}
