package net.shortninja.staffplus.session;

import net.shortninja.staffplus.player.PlayerSession;
import net.shortninja.staffplus.unordered.IPlayerSession;
import net.shortninja.staffplus.unordered.ISessionManager;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionManager implements ISessionManager {
    private static Map<UUID, IPlayerSession> playerSessions;
    private final SessionLoader sessionLoader;

    public SessionManager(SessionLoader sessionLoader) {
        this.sessionLoader = sessionLoader;
        playerSessions = new HashMap<>();
    }

    @Override
    public void initialize(Player player) {
        if(!has(player.getUniqueId())) {
            playerSessions.put(player.getUniqueId(), sessionLoader.loadSession(player));
        }
    }

    @Override
    public Collection<IPlayerSession> getAll() {
        return playerSessions.values();
    }


    @Override
    public IPlayerSession get(UUID uuid) {
        if(!has(uuid)) {
            PlayerSession playerSession = sessionLoader.loadSession(uuid);
            playerSessions.put(uuid, playerSession);
        }
        return playerSessions.get(uuid);
    }

    public boolean has(UUID uuid) {
        return playerSessions.containsKey(uuid);
    }

    public void unload(UUID uniqueId) {
        if(!has(uniqueId)) {
            return;
        }
        sessionLoader.saveSession(playerSessions.get(uniqueId));
        playerSessions.remove(uniqueId);
    }
}