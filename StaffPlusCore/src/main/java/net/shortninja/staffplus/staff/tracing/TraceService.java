package net.shortninja.staffplus.staff.tracing;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.exceptions.BusinessException;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.tracing.config.TraceConfiguration;
import net.shortninja.staffplusplus.trace.StartTraceEvent;
import net.shortninja.staffplusplus.trace.StopTraceEvent;
import net.shortninja.staffplusplus.trace.TraceWriter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.util.BukkitUtils.sendEvent;

public class TraceService {

    private final Map<UUID, Trace> tracedPlayers = new HashMap<>();

    private final TraceWriterFactory traceWriterFactory;
    private final TraceConfiguration traceConfiguration;

    public TraceService(TraceWriterFactory traceWriterFactory, Options options) {
        this.traceWriterFactory = traceWriterFactory;
        this.traceConfiguration = options.traceConfiguration;
    }

    public void startTrace(CommandSender tracer, SppPlayer traced) {
        UUID tracerUuid = tracer instanceof Player ? ((Player) tracer).getUniqueId() : StaffPlus.get().consoleUUID;

        if (tracedPlayers.containsKey(tracerUuid)) {
            throw new BusinessException("&CCannot start a trace. You are already tracing a player");
        }

        List<TraceWriter> traceWriters = traceWriterFactory.buildTraceWriters(tracerUuid, traced.getId());
        Trace trace = new Trace(traced.getPlayer(), tracerUuid, traceWriters);
        tracedPlayers.put(tracerUuid, trace);
        sendEvent(new StartTraceEvent(trace));
    }

    public void stopTrace(CommandSender tracer) {
        UUID tracerUuid = tracer instanceof Player ? ((Player) tracer).getUniqueId() : StaffPlus.get().consoleUUID;
        stopTrace(tracerUuid);
    }

    public void stopTrace(UUID tracerUuid) {
        if (!tracedPlayers.containsKey(tracerUuid)) {
            throw new BusinessException("&CYou are currently not tracing anyone");
        }
        Trace trace = tracedPlayers.get(tracerUuid);
        trace.stopTrace();
        tracedPlayers.remove(tracerUuid);
        sendEvent(new StopTraceEvent(trace));
    }

    public void stopAllTracesForPlayer(UUID tracedUuid) {
        tracedPlayers.values().stream()
            .filter(t -> t.getTargetUuid() == tracedUuid)
            .forEach(t -> {
                t.stopTrace();
                sendEvent(new StopTraceEvent(t));
            });

        tracedPlayers.values().removeIf(t -> t.getTargetUuid() == tracedUuid);
    }

    public void sendTraceMessage(UUID tracedUuid, String message) {
        List<UUID> tracers = tracedPlayers.entrySet().stream()
            .filter(e -> e.getValue().getTargetUuid() == tracedUuid)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

        Player traced = Bukkit.getPlayer(tracedUuid);
        for (UUID tracerUuid : tracers) {
            Player tracer = Bukkit.getPlayer(tracerUuid);
            if (tracer == null || traced == null) {
                //Remove trace if the tracerUuid or the traced is offline
                stopTrace(tracerUuid);
                continue;
            }

            tracedPlayers.get(tracerUuid).writeToTrace(message);
        }
    }

    public void sendTraceMessage(TraceType traceType, UUID tracedUuid, String message) {
        if (!traceConfiguration.isTraceTypeEnabled(traceType)) {
            return;
        }
        sendTraceMessage(tracedUuid, message);
    }

    public boolean isPlayerTracing(Player player) {
        return tracedPlayers.containsKey(player.getUniqueId());
    }

    public List<Player> getTracingPlayers() {
        return tracedPlayers.keySet().stream()
            .map(Bukkit::getPlayer)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

}
