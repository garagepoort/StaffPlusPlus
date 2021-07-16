package net.shortninja.staffplus.core.domain.staff.tracing;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplusplus.trace.TraceOutputChannel;
import net.shortninja.staffplusplus.trace.TraceWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@IocBean
public class TraceWriterFactory {


    private final Messages messages;
    private final Options options;

    public TraceWriterFactory(Messages messages, Options options) {

        this.messages = messages;
        this.options = options;
    }

    public List<TraceWriter> buildTraceWriters(UUID tracerUuid, UUID tracedUuid) {
        List<TraceOutputChannel> outputChannels = options.traceConfiguration.getOutputChannels();

        List<TraceWriter> traceWriters = new ArrayList<>();
        if(outputChannels.contains(TraceOutputChannel.CHAT))  {
            traceWriters.add(new ChatTraceWriter(tracerUuid, messages));
        }
        if(outputChannels.contains(TraceOutputChannel.FILE)) {
            traceWriters.add(new FileTraceWriter(tracedUuid));
        }
        return traceWriters;
    }
}
