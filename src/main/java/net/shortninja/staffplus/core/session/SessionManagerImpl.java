package net.shortninja.staffplus.core.session;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplusplus.session.SessionManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@IocBean
public class SessionManagerImpl  implements SessionManager {
    private static Map<UUID, PlayerSession> playerSessions = new HashMap<>();
    private final SessionLoader sessionLoader;

    public SessionManagerImpl(SessionLoader sessionLoader) {
        this.sessionLoader = sessionLoader;
        Bukkit.getOnlinePlayers().forEach(this::initialize);
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
}