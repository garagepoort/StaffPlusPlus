package net.shortninja.staffplus.core.domain.staff.investigate.bungee.receive;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMessageListener;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.common.bungee.BungeeClient;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.investigate.bungee.InvestigationBungee;
import net.shortninja.staffplus.core.domain.staff.investigate.bungee.events.InvestigationConcludedBungeeEvent;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.Optional;

@IocBean(conditionalOnProperty = "server-sync-module.investigation-sync=true")
@IocMessageListener(channel = Constants.BUNGEE_CORD_CHANNEL)
public class InvestigationConcludedBungeeReceiver implements PluginMessageListener {

    private final BungeeClient bungeeClient;
    private final SessionManagerImpl sessionManager;
    private final PlayerManager playerManager;

    public InvestigationConcludedBungeeReceiver(BungeeClient bungeeClient, SessionManagerImpl sessionManager, PlayerManager playerManager) {
        this.bungeeClient = bungeeClient;
        this.sessionManager = sessionManager;
        this.playerManager = playerManager;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        Optional<InvestigationBungee> bungeeMessage = bungeeClient.handleReceived(channel, Constants.BUNGEE_INVESTIGATION_CONCLUDED_CHANNEL, message, InvestigationBungee.class);
        bungeeMessage.ifPresent(b -> {
            playerManager.getOnlinePlayer(b.getInvestigatedUuid()).ifPresent(s -> sessionManager.get(s.getId()).setUnderInvestigation(false));
            Bukkit.getPluginManager().callEvent(new InvestigationConcludedBungeeEvent(b));
        });
    }
}
