package net.shortninja.staffplus.staff.tracing;

import net.shortninja.staffplus.unordered.trace.ITrace;
import net.shortninja.staffplus.unordered.trace.TraceWriter;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class Trace implements ITrace {

    private final String tracedName;
    private final UUID tracerUuid;
    private final UUID tracedUuid;
    private final List<TraceWriter> writers;

    public Trace(Player traced, UUID tracerUuid, List<TraceWriter> writers) {
        this.writers = writers;
        this.tracedName = traced.getName();
        this.tracerUuid = tracerUuid;
        this.tracedUuid = traced.getUniqueId();
    }

    @Override
    public List<TraceWriter> getWriters() {
        return writers;
    }

    @Override
    public UUID getTracerUuid() {
        return tracerUuid;
    }

    @Override
    public String getTracedName() {
        return tracedName;
    }

    @Override
    public UUID getTracedUuid() {
        return tracedUuid;
    }

    public void writeToTrace(String message) {
        writers.forEach(w -> w.writeToTrace(message));
    }

    public void stopTrace() {
        writers.forEach(TraceWriter::stopTrace);
    }
}
