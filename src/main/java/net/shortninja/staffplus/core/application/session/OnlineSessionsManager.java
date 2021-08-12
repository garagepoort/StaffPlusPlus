package net.shortninja.staffplus.core.application.session;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.session.IPlayerSession;
import net.shortninja.staffplusplus.session.SessionManager;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@IocBean
public class OnlineSessionsManager implements SessionManager {

    @ConfigProperty("permissions:member")
    private String permissionMember;

    private static final Map<UUID, PlayerSession> playerSessions = new HashMap<>();

    private final PermissionHandler permissionHandler;
    private final PlayerManager playerManager;
    private final SessionLoader sessionLoader;

    public OnlineSessionsManager(PermissionHandler permissionHandler, PlayerManager playerManager, SessionLoader sessionLoader) {
        this.permissionHandler = permissionHandler;
        this.playerManager = playerManager;
        this.sessionLoader = sessionLoader;
        Bukkit.getOnlinePlayers().forEach(this::get);
    }

    @Override
    public Collection<? extends OnlinePlayerSession> getAll() {
        return playerSessions.values();
    }

    @Override
    public Collection<? extends OnlinePlayerSession> getOnlineStaffMembers() {
        List<OnlinePlayerSession> sessions = new ArrayList<>();
        for (OnlinePlayerSession s : getAll()) {
            playerManager.getOnlinePlayer(s.getUuid())
                .map(SppPlayer::getPlayer)
                .filter(p -> permissionHandler.has(p, permissionMember))
                .ifPresent(p -> sessions.add(s));
        }
        return sessions;
    }

    @Override
    public IPlayerSession get(UUID uuid) {
        return get(Bukkit.getPlayer(uuid));
    }

    public PlayerSession get(Player player) {
        if (!has(player.getUniqueId())) {
            playerSessions.put(player.getUniqueId(), sessionLoader.loadSession(player));
        }
        return playerSessions.get(player.getUniqueId());
    }

    public boolean has(UUID uuid) {
        return playerSessions.containsKey(uuid);
    }

    public void remove(Player player) {
        playerSessions.remove(player.getUniqueId());
    }
}