package net.shortninja.staffplus.core.session;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import net.shortninja.staffplusplus.session.SessionManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@IocBean
public class SessionManagerImpl implements SessionManager {
    private static Map<UUID, PlayerSession> playerSessions = new HashMap<>();
    private final SessionLoader sessionLoader;
    private final PermissionHandler permissionHandler;
    private final Options options;

    public SessionManagerImpl(SessionLoader sessionLoader, PermissionHandler permissionHandler, Options options) {
        this.sessionLoader = sessionLoader;
        this.permissionHandler = permissionHandler;
        this.options = options;
        Bukkit.getOnlinePlayers().forEach(this::initialize);
    }

    public void initialize(Player player) {
        if (!has(player.getUniqueId())) {
            playerSessions.put(player.getUniqueId(), sessionLoader.loadSession(player));
        }
    }

    @Override
    public Collection<PlayerSession> getAll() {
        return playerSessions.values();
    }

    @Override
    public Collection<PlayerSession> getOnlineStaffMembers() {
        return playerSessions.values().stream()
            .filter(p -> p.getPlayer().isPresent())
            .filter(p -> permissionHandler.has(p.getPlayer().get(), options.permissionMember))
            .collect(Collectors.toList());
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

    public void saveAll() {
        sessionLoader.saveSessions(playerSessions.values());
    }

    public void saveSession(Player player) {
        sessionLoader.saveSession(get(player.getUniqueId()));
    }
}