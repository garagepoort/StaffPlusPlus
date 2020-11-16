package net.shortninja.staffplus.staff.tracing.config;

import net.shortninja.staffplus.common.config.ConfigLoader;
import net.shortninja.staffplus.staff.tracing.TraceType;
import net.shortninja.staffplus.unordered.trace.TraceOutputChannel;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TraceModuleLoader extends ConfigLoader<TraceConfiguration> {

    @Override
    protected TraceConfiguration load(FileConfiguration config) {
        boolean enabled = config.getBoolean("trace-module.enabled");
        if (!enabled) {
            return new TraceConfiguration(false, Collections.emptyList(), Collections.emptyList());
        }

        List<TraceType> traceTypes = getTraceTypes(config);
        List<TraceOutputChannel> traceOutputChannels = getTraceOutputChannels(config);
        return new TraceConfiguration(true, traceTypes, traceOutputChannels);
    }

    private List<TraceOutputChannel> getTraceOutputChannels(FileConfiguration config) {
        String outputChannelsString = config.getString("trace-module.output-channels");
        if (outputChannelsString == null || outputChannelsString.isEmpty()) {
            throw new RuntimeException("Invalid configuration: no output channels registered for the tracing module");
        }
        return Arrays.stream(outputChannelsString.split(";"))
            .map(TraceOutputChannel::valueOf)
            .collect(Collectors.toList());
    }

    private List<TraceType> getTraceTypes(FileConfiguration config) {
        String traceEventsString = config.getString("trace-module.trace-events");
        if (traceEventsString == null || traceEventsString.isEmpty()) {
            throw new RuntimeException("Invalid configuration: no trace events registered for the tracing module");
        }
        return Arrays.stream(traceEventsString.split(";"))
            .map(TraceType::valueOf)
            .collect(Collectors.toList());
    }
}
