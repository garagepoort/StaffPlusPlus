package net.shortninja.staffplus.core.tracing.config;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.configuration.ConfigTransformer;
import net.shortninja.staffplus.core.application.config.SplitBySemicolon;
import net.shortninja.staffplus.core.tracing.TraceType;
import net.shortninja.staffplusplus.trace.TraceOutputChannel;

import java.util.ArrayList;
import java.util.List;

@IocBean
public class TraceConfiguration {

    @ConfigProperty(
        value = "trace-module.trace-events",
        required = true,
        error = "Invalid configuration: no trace events registered for the tracing module")
    @ConfigTransformer(SplitBySemicolon.class)
    private List<TraceType> traceTypes = new ArrayList<>();

    @ConfigProperty(
        value = "trace-module.output-channels",
        required = true,
        error = "Invalid configuration: no output channels registered for the tracing module")
    @ConfigTransformer({SplitBySemicolon.class, TraceOutputChannelConfigTransformer.class})
    private List<TraceOutputChannel> outputChannels = new ArrayList<>();

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
