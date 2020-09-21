package net.shortninja.staffplus.staff.tracing;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.BusinessException;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.util.MessageCoordinator;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class TraceService {

    private final Map<UUID, UUID> tracedPlayers = new HashMap<>();

    private final UserManager userManager;
    private final Messages messages;
    private final MessageCoordinator message;

    public TraceService(UserManager userManager, Messages messages, MessageCoordinator message) {
        this.userManager = userManager;
        this.messages = messages;
        this.message = message;
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
        tracedPlayers.put(tracerUuid, traced.getUniqueId());
    }

    public void stopTrace(CommandSender tracer) {
        UUID tracerUuid = tracer instanceof Player ? ((Player) tracer).getUniqueId() : StaffPlus.get().consoleUUID;
        if (!tracedPlayers.containsKey(tracerUuid)) {
            throw new BusinessException("You are currently not tracing anyone", messages.prefixGeneral);
        }
        tracedPlayers.remove(tracerUuid);
    }

    public void sendTraceMessage(UUID tracedUuid, String message) {
        List<UUID> tracers = tracedPlayers.entrySet().stream()
            .filter(e -> e.getValue() == tracedUuid)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

        Player traced = Bukkit.getPlayer(tracedUuid);
        tracers.forEach(tracer -> {
            Player player = Bukkit.getPlayer(tracer);
            if (player != null && traced != null) {
                this.message.send(player, "[" + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) + "] : " + message, messages.prefixTrace);
            } else {
                //Remove trace if the tracer or the traced is offline
                tracedPlayers.remove(tracer);
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
            .filter((e) -> e.getValue() == tracedPlayer.getUniqueId())
            .map(e -> Bukkit.getPlayer(e.getKey()))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
}
