package net.shortninja.staffplus.core.tracing.config;

import be.garagepoort.mcioc.configuration.IConfigTransformer;
import net.shortninja.staffplusplus.trace.TraceOutputChannel;

import java.util.List;
import java.util.stream.Collectors;

public class TraceOutputChannelConfigTransformer implements IConfigTransformer<List<TraceOutputChannel>, List<String>> {
    @Override
    public List<TraceOutputChannel> mapConfig(List<String> strings) {
        return strings.stream()
            .map(TraceOutputChannel::valueOf)
            .collect(Collectors.toList());
    }
}
