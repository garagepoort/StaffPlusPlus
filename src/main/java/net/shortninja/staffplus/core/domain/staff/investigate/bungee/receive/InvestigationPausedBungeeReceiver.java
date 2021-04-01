package net.shortninja.staffplus.core.domain.staff.investigate.bungee.receive;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMessageListener;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.common.bungee.BungeeClient;
import net.shortninja.staffplus.core.domain.staff.investigate.bungee.InvestigationBungee;
import net.shortninja.staffplus.core.domain.staff.investigate.bungee.events.InvestigationPausedBungeeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.Optional;

@IocBean(conditionalOnProperty = "server-sync-module.investigation-sync=true")
@IocMessageListener(channel = Constants.BUNGEE_CORD_CHANNEL)
public class InvestigationPausedBungeeReceiver implements PluginMessageListener {

    private final BungeeClient bungeeClient;

    public InvestigationPausedBungeeReceiver(BungeeClient bungeeClient) {
        this.bungeeClient = bungeeClient;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        Optional<InvestigationBungee> bungeeMessage = bungeeClient.handleReceived(channel, Constants.BUNGEE_INVESTIGATION_PAUSED_CHANNEL, message, InvestigationBungee.class);
        bungeeMessage.ifPresent(b -> Bukkit.getPluginManager().callEvent(new InvestigationPausedBungeeEvent(b)));
    }
}
