package net.shortninja.staffplus.core.domain.staff.tracing;

import net.shortninja.staffplusplus.trace.ITrace;
import net.shortninja.staffplusplus.trace.TraceWriter;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class Trace implements ITrace {

    private final String targetName;
    private final UUID targetUuid;
    private final UUID tracerUuid;
    private final List<TraceWriter> writers;

    public Trace(Player traced, UUID tracerUuid, List<TraceWriter> writers) {
        this.writers = writers;
        this.targetName = traced.getName();
        this.tracerUuid = tracerUuid;
        this.targetUuid = traced.getUniqueId();
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
    public String getTargetName() {
        return targetName;
    }

    @Override
    public UUID getTargetUuid() {
        return targetUuid;
    }

    public void writeToTrace(String message) {
        writers.forEach(w -> w.writeToTrace(message));
    }

    public void stopTrace() {
        writers.forEach(TraceWriter::stopTrace);
    }
}
