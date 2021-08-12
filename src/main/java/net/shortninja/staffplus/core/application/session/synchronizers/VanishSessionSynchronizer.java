package net.shortninja.staffplus.core.application.session.synchronizers;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplusplus.vanish.VanishOffEvent;
import net.shortninja.staffplusplus.vanish.VanishOnEvent;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocListener
@IocBean
public class VanishSessionSynchronizer implements Listener {

    private final OnlineSessionsManager onlineSessionsManager;

    public VanishSessionSynchronizer(OnlineSessionsManager onlineSessionsManager) {
        this.onlineSessionsManager = onlineSessionsManager;
    }

    @EventHandler
    public void vanishOn(VanishOnEvent event) {
        OnlinePlayerSession onlinePlayerSession = onlineSessionsManager.get(event.getPlayer());
        onlinePlayerSession.setVanishType(event.getType());
    }

    @EventHandler
    public void vanishOff(VanishOffEvent event) {
        OnlinePlayerSession onlinePlayerSession = onlineSessionsManager.get(event.getPlayer());
        onlinePlayerSession.setVanishType(VanishType.NONE);
    }
}
