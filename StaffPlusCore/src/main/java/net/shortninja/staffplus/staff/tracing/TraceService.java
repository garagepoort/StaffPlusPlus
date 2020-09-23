package net.shortninja.staffplus.staff.tracing;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.BusinessException;
import net.shortninja.staffplus.common.PlayerOfflineException;
import net.shortninja.staffplus.event.trace.StartTraceEvent;
import net.shortninja.staffplus.event.trace.StopTraceEvent;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.tracing.config.TraceConfiguration;
import net.shortninja.staffplus.unordered.trace.TraceWriter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.*;
import java.util.stream.Collectors;

import static org.bukkit.Bukkit.getScheduler;

public class TraceService {

    private final Map<UUID, Trace> tracedPlayers = new HashMap<>();

    private final UserManager userManager;
    private final TraceWriterFactory traceWriterFactory;
    private final TraceConfiguration traceConfiguration;

    public TraceService(UserManager userManager, TraceWriterFactory traceWriterFactory, Options options) {
        this.userManager = userManager;
        this.traceWriterFactory = traceWriterFactory;
        this.traceConfiguration = options.traceConfiguration;
    }

    public void startTrace(CommandSender tracer, String tracedPlayerName) {
        UUID tracerUuid = tracer instanceof Player ? ((Player) tracer).getUniqueId() : StaffPlus.get().consoleUUID;
        Player traced = userManager.getOnlinePlayer(tracedPlayerName);
        if (traced == null) {
            throw new PlayerOfflineException();
        }

        if (tracedPlayers.containsKey(tracerUuid)) {
            throw new BusinessException("Cannot start a trace. You are already tracing a player");
        }

        List<TraceWriter> traceWriters = traceWriterFactory.buildTraceWriters(tracerUuid, traced.getUniqueId());
        Trace trace = new Trace(traced, tracerUuid, traceWriters);
        tracedPlayers.put(tracerUuid, trace);
        sendEvent(new StartTraceEvent(trace));
    }

    public void stopTrace(CommandSender tracer) {
        UUID tracerUuid = tracer instanceof Player ? ((Player) tracer).getUniqueId() : StaffPlus.get().consoleUUID;
        stopTrace(tracerUuid);
    }

    public void stopTrace(UUID tracerUuid) {
        if (!tracedPlayers.containsKey(tracerUuid)) {
            throw new BusinessException("You are currently not tracing anyone");
        }
        Trace trace = tracedPlayers.get(tracerUuid);
        trace.stopTrace();
        tracedPlayers.remove(tracerUuid);
        sendEvent(new StopTraceEvent(trace));
    }

    public void stopAllTracesForPlayer(UUID tracedUuid) {
        tracedPlayers.values().stream()
            .filter(t -> t.getTracedUuid() == tracedUuid)
            .forEach(t -> {
                t.stopTrace();
                sendEvent(new StopTraceEvent(t));
            });

        tracedPlayers.values().removeIf(t -> t.getTracedUuid() == tracedUuid);
    }

    public void sendTraceMessage(TraceType traceType, UUID tracedUuid, String message) {
        if (!traceConfiguration.isTraceTypeEnabled(traceType)) {
            return;
        }

        List<UUID> tracers = tracedPlayers.entrySet().stream()
            .filter(e -> e.getValue().getTracedUuid() == tracedUuid)
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

    public boolean isPlayerTracing(Player player) {
        return tracedPlayers.containsKey(player.getUniqueId());
    }

    public List<Player> getTracingPlayers() {
        return tracedPlayers.keySet().stream()
            .map(Bukkit::getPlayer)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    private void sendEvent(Event event) {
        getScheduler().runTask(StaffPlus.get(), () -> {
            Bukkit.getPluginManager().callEvent(event);
        });
    }
}
