package net.shortninja.staffplus.staff.tracing.config;

import net.shortninja.staffplus.staff.tracing.TraceType;
import net.shortninja.staffplusplus.trace.TraceOutputChannel;

import java.util.List;

public class TraceConfiguration {

    private final boolean enabled;
    private final List<TraceType> traceTypes;
    private final List<TraceOutputChannel> outputChannels;

    public TraceConfiguration(boolean enabled, List<TraceType> traceTypes, List<TraceOutputChannel> outputChannels) {
        this.enabled = enabled;
        this.traceTypes = traceTypes;
        this.outputChannels = outputChannels;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public List<TraceType> getTraceTypes() {
        return traceTypes;
    }

    public boolean isTraceTypeEnabled(TraceType traceType) {
        return traceTypes.contains(traceType);
    }

    public List<TraceOutputChannel> getOutputChannels() {
        return outputChannels;
    }

    public boolean hasChannel(TraceOutputChannel channel) {
        return outputChannels.contains(channel);
    }
}
