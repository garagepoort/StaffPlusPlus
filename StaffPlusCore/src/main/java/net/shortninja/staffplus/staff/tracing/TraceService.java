package net.shortninja.staffplus.staff.tracing;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.BusinessException;
import net.shortninja.staffplus.event.trace.StartTraceEvent;
import net.shortninja.staffplus.event.trace.StopTraceEvent;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.unordered.trace.TraceWriter;
import net.shortninja.staffplus.util.MessageCoordinator;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.bukkit.Bukkit.getScheduler;

public class TraceService {

    private final Map<UUID, Trace> tracedPlayers = new HashMap<>();

    private final UserManager userManager;
    private final Messages messages;
    private final MessageCoordinator message;
    private final TraceWriterFactory traceWriterFactory;

    public TraceService(UserManager userManager, Messages messages, MessageCoordinator message, TraceWriterFactory traceWriterFactory) {
        this.userManager = userManager;
        this.messages = messages;
        this.message = message;
        this.traceWriterFactory = traceWriterFactory;
    }

    public void startTrace(CommandSender tracer, String tracedPlayerName) {
        UUID tracerUuid = tracer instanceof Player ? ((Player) tracer).getUniqueId() : StaffPlus.get().consoleUUID;
        Player traced = userManager.getOnlinePlayer(tracedPlayerName);
        if (traced == null) {
            throw new BusinessException(messages.playerOffline, messages.prefixGeneral);
        }

        if (tracedPlayers.containsKey(tracerUuid)) {
            throw new BusinessException("Cannot start a trace. You are already tracing a player", messages.prefixGeneral);
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
            throw new BusinessException("You are currently not tracing anyone", messages.prefixGeneral);
        }
        Trace trace = tracedPlayers.get(tracerUuid);
        trace.stopTrace();
        tracedPlayers.remove(tracerUuid);
        sendEvent(new StopTraceEvent(trace));
    }

    public void sendTraceMessage(UUID tracedUuid, String message) {
        List<UUID> tracers = tracedPlayers.entrySet().stream()
            .filter(e -> e.getValue().getTracedUuid() == tracedUuid)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

        Player traced = Bukkit.getPlayer(tracedUuid);
        tracers.forEach(tracerUuid -> {
            Player player = Bukkit.getPlayer(tracerUuid);
            if (player != null && traced != null) {
                String traceMessage = "[" + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) + "] : " + message;
                this.message.send(player, traceMessage, messages.prefixTrace);
                tracedPlayers.get(tracerUuid).writeToTrace(traceMessage);
            } else {
                //Remove trace if the tracerUuid or the traced is offline
                stopTrace(tracerUuid);
            }
        });
    }

    public boolean isPlayerTracing(Player player) {
        return tracedPlayers.containsKey(player.getUniqueId());
    }

    public List<Player> getTracingPlayers() {
        return tracedPlayers.keySet().stream().map(Bukkit::getPlayer)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    public List<Player> getTracingPlayers(Player tracedPlayer) {
        return tracedPlayers.entrySet().stream()
            .filter((e) -> e.getValue().getTracedUuid() == tracedPlayer.getUniqueId())
            .map(e -> Bukkit.getPlayer(e.getKey()))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    private void sendEvent(Event event) {
        getScheduler().runTask(StaffPlus.get(), () -> {
            Bukkit.getPluginManager().callEvent(event);
        });
    }
}
