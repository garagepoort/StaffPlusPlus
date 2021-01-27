package net.shortninja.staffplus.session;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.shortninja.staffplus.common.bungee.BungeeAction;
import net.shortninja.staffplus.common.bungee.BungeeClient;
import net.shortninja.staffplus.common.bungee.BungeeContext;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.session.bungee.SessionBungeeDto;
import net.shortninja.staffplus.session.bungee.SessionBungeeDtoMapper;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionManager {
    private static Map<UUID, PlayerSession> playerSessions;
    private final SessionLoader sessionLoader;
    private final Options options;
    private final BungeeClient bungeeClient;
    private final SessionBungeeDtoMapper sessionBungeeDtoMapper;

    public SessionManager(SessionLoader sessionLoader, Options options, BungeeClient bungeeClient, SessionBungeeDtoMapper sessionBungeeDtoMapper) {
        this.sessionLoader = sessionLoader;
        this.options = options;
        this.bungeeClient = bungeeClient;
        this.sessionBungeeDtoMapper = sessionBungeeDtoMapper;
        playerSessions = new HashMap<>();
    }

    public void initialize(Player player) {
        if (!has(player.getUniqueId())) {
            playerSessions.put(player.getUniqueId(), sessionLoader.loadSession(player));
        }
    }

    public Collection<PlayerSession> getAll() {
        return playerSessions.values();
    }

    public PlayerSession get(UUID uuid) {
        if (!has(uuid)) {
            PlayerSession playerSession = sessionLoader.loadSession(uuid);
            playerSessions.put(uuid, playerSession);
        }
        return playerSessions.get(uuid);
    }

    public boolean has(UUID uuid) {
        return playerSessions.containsKey(uuid);
    }

    public void unload(UUID uniqueId) {
        if (!has(uniqueId)) {
            return;
        }
        sessionLoader.saveSession(playerSessions.get(uniqueId));
        playerSessions.remove(uniqueId);
    }

    public void triggerSessionSync(Player player) {
        try {
            if (sessionBungeeDtoMapper.shouldSync()) {
                PlayerSession playerSession = get(player.getUniqueId());
                SessionBungeeDto sessionBungeeDto = sessionBungeeDtoMapper.map(playerSession);
                bungeeClient.sendAll(playerSession.getPlayer().get(), BungeeAction.FORWARD, BungeeContext.SESSION, new ObjectMapper().writeValueAsString(sessionBungeeDto));
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not synchronize Vanish across bungee servers.");
        }
    }

    public void handleSessionSync(SessionBungeeDto sessionBungeeDto) {
        PlayerSession playerSession = get(sessionBungeeDto.getPlayerUuid());
        if(sessionBungeeDto.getVanishType() != null) {
            playerSession.setVanishType(sessionBungeeDto.getVanishType());
        }
        if(sessionBungeeDto.getStaffMode() != null) {
            playerSession.setInStaffMode(sessionBungeeDto.getStaffMode());
        }
        sessionLoader.saveSession(playerSession);
    }
}