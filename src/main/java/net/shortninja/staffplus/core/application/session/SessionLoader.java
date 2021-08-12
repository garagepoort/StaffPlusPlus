package net.shortninja.staffplus.core.application.session;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMulti;
import org.bukkit.entity.Player;

import java.util.List;

@IocBean
public class SessionLoader {

    private final List<SessionEnhancer> sessionEnhancers;

    public SessionLoader(@IocMulti(SessionEnhancer.class) List<SessionEnhancer> sessionEnhancers) {
        this.sessionEnhancers = sessionEnhancers;
    }

    public PlayerSession loadSession(Player player) {
        PlayerSession playerSession = new PlayerSession(player.getUniqueId(), player.getName());
        sessionEnhancers.forEach(s -> s.enhance(playerSession));
        return playerSession;
    }
}
