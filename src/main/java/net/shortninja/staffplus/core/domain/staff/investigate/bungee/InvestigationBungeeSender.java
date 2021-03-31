package net.shortninja.staffplus.core.domain.staff.investigate.bungee;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.common.bungee.BungeeClient;
import net.shortninja.staffplusplus.investigate.InvestigationConcludedEvent;
import net.shortninja.staffplusplus.investigate.InvestigationStartedEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocBean(conditionalOnProperty = "server-sync-module.investigation-sync=true")
@IocListener
public class InvestigationBungeeSender implements Listener {

    private final BungeeClient bungeeClient;

    public InvestigationBungeeSender(BungeeClient bungeeClient) {
        this.bungeeClient = bungeeClient;
    }

    @EventHandler
    public void onInvestigationStarted(InvestigationStartedEvent investigationStartedEvent) {
        Player player = Bukkit.getOnlinePlayers().iterator().next();
        bungeeClient.sendMessage(player, Constants.BUNGEE_INVESTIGATION_STARTED_CHANNEL, new InvestigationBungee(investigationStartedEvent.getInvestigation()));
    }

    @EventHandler
    public void onInvestigationConcluded(InvestigationConcludedEvent investigationConcludedEvent) {
        Player player = Bukkit.getOnlinePlayers().iterator().next();
        bungeeClient.sendMessage(player, Constants.BUNGEE_INVESTIGATION_CONCLUDED_CHANNEL, new InvestigationBungee(investigationConcludedEvent.getInvestigation()));
    }
}
