package net.shortninja.staffplus.staff.tracing.config;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.staff.tracing.TraceType;
import net.shortninja.staffplus.unordered.trace.TraceOutputChannel;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TraceModuleLoader {

    private final static FileConfiguration config = StaffPlus.get().getConfig();

    public static TraceConfiguration load() {
        boolean enabled = config.getBoolean("trace-module.enabled");
        if (!enabled) {
            return new TraceConfiguration(false, Collections.emptyList(), Collections.emptyList());
        }

        List<TraceType> traceTypes = getTraceTypes();
        List<TraceOutputChannel> traceOutputChannels = getTraceOutputChannels();
        return new TraceConfiguration(true, traceTypes, traceOutputChannels);
    }

    private static List<TraceOutputChannel> getTraceOutputChannels() {
        String outputChannelsString = config.getString("trace-module.output-channels");
        if (outputChannelsString == null || outputChannelsString.isEmpty()) {
            throw new RuntimeException("Invalid configuration: no output channels registered for the tracing module");
        }
        return Arrays.stream(outputChannelsString.split(";"))
            .map(TraceOutputChannel::valueOf)
            .collect(Collectors.toList());
    }

    private static List<TraceType> getTraceTypes() {
        String traceEventsString = config.getString("trace-module.trace-events");
        if (traceEventsString == null || traceEventsString.isEmpty()) {
            throw new RuntimeException("Invalid configuration: no trace events registered for the tracing module");
        }
        return Arrays.stream(traceEventsString.split(";"))
            .map(TraceType::valueOf)
            .collect(Collectors.toList());
    }
}
