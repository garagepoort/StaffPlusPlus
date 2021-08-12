package net.shortninja.staffplus.core.application.session.synchronizers;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.mute.MuteEvent;
import net.shortninja.staffplusplus.mute.UnmuteEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocListener
@IocBean
public class MuteSessionSynchronizer implements Listener {

    private final OnlineSessionsManager onlineSessionsManager;
    private final PlayerManager playerManager;

    public MuteSessionSynchronizer(OnlineSessionsManager onlineSessionsManager, PlayerManager playerManager) {
        this.onlineSessionsManager = onlineSessionsManager;
        this.playerManager = playerManager;
    }

    @EventHandler
    public void onMute(MuteEvent muteEvent) {
        playerManager.getOnlinePlayer(muteEvent.getMute().getTargetUuid())
            .ifPresent(p -> {
                OnlinePlayerSession onlinePlayerSession = onlineSessionsManager.get(p.getPlayer());
                onlinePlayerSession.setMuted(true);
            });
    }

    @EventHandler
    public void onUnMute(UnmuteEvent muteEvent) {
        playerManager.getOnlinePlayer(muteEvent.getMute().getTargetUuid())
            .ifPresent(p -> {
                OnlinePlayerSession onlinePlayerSession = onlineSessionsManager.get(p.getPlayer());
                onlinePlayerSession.setMuted(true);
            });
    }
}
