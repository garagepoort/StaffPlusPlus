package net.shortninja.staffplus.core.domain.player.queue.listeners;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.application.queue.QueueMessageListener;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.session.SppPlayer;

@IocBean
@IocMultiProvider(QueueMessageListener.class)
public class PlayersUpdateListener implements QueueMessageListener<Void> {

    @ConfigProperty("server-name")
    private String serverName;

    private final PlayerManager playerManager;

    public PlayersUpdateListener(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @Override
    public String handleMessage(Void message) {
        for (SppPlayer onAndOfflinePlayer : playerManager.getOnAndOfflinePlayers()) {
            playerManager.storePlayer(onAndOfflinePlayer.getOfflinePlayer());
        }
        return "Player list has been updated for server: " + serverName;
    }

    @Override
    public String getType() {
        return "players/update";
    }

    @Override
    public Class getMessageClass() {
        return Void.class;
    }
}
