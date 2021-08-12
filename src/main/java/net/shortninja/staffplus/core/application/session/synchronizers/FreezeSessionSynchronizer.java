package net.shortninja.staffplus.core.application.session.synchronizers;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplusplus.freeze.PlayerFrozenEvent;
import net.shortninja.staffplusplus.freeze.PlayerUnFrozenEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocBean
@IocListener
public class FreezeSessionSynchronizer implements Listener {

    private final OnlineSessionsManager onlineSessionsManager;

    public FreezeSessionSynchronizer(OnlineSessionsManager onlineSessionsManager) {
        this.onlineSessionsManager = onlineSessionsManager;
    }

    @EventHandler
    public void onFreeze(PlayerFrozenEvent event) {
        OnlinePlayerSession onlinePlayerSession = onlineSessionsManager.get(event.getTarget());
        onlinePlayerSession.setFrozen(true);
    }

    @EventHandler
    public void onFreeze(PlayerUnFrozenEvent event) {
        OnlinePlayerSession onlinePlayerSession = onlineSessionsManager.get(event.getTarget());
        onlinePlayerSession.setFrozen(false);
    }
}
