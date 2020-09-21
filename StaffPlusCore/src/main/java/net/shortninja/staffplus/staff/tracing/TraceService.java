package net.shortninja.staffplus.staff.tracing;

import net.shortninja.staffplus.common.BusinessException;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.server.data.config.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class TraceService {

    private final Map<UUID, UUID> tracedPlayers = new HashMap<>();

    private final UserManager userManager;
    private final Messages messages;

    public TraceService(UserManager userManager, Messages messages) {
        this.userManager = userManager;
        this.messages = messages;
    }

    public void startTrace(Player tracer, String tracedPlayerName) {
        Player traced = userManager.getOnlinePlayer(tracedPlayerName);
        if (tracedPlayers.containsKey(tracer.getUniqueId())) {
            throw new BusinessException("Cannot start a trace. You are already tracing a player", messages.prefixGeneral);
        }
        tracedPlayers.put(tracer.getUniqueId(), traced.getUniqueId());
    }

    public void stopTrace(Player tracer) {
        if (!tracedPlayers.containsKey(tracer.getUniqueId())) {
            throw new BusinessException("You are currently not tracing anyone", messages.prefixGeneral);
        }
        tracedPlayers.remove(tracer.getUniqueId());
    }

    public boolean isPlayerTracing(Player player) {
        return tracedPlayers.containsKey(player.getUniqueId());
    }

    public List<Player> getTracingPlayers() {
        return tracedPlayers.keySet().stream().map(Bukkit::getPlayer)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
}
