package net.shortninja.staffplus.core.application.session.synchronizers;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.investigate.bungee.events.InvestigationConcludedBungeeEvent;
import net.shortninja.staffplus.core.domain.staff.investigate.bungee.events.InvestigationPausedBungeeEvent;
import net.shortninja.staffplus.core.domain.staff.investigate.bungee.events.InvestigationStartedBungeeEvent;
import net.shortninja.staffplusplus.investigate.InvestigationConcludedEvent;
import net.shortninja.staffplusplus.investigate.InvestigationPausedEvent;
import net.shortninja.staffplusplus.investigate.InvestigationStartedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocBean
@IocListener
public class InvestigationSessionSynchronizer implements Listener {

    private final OnlineSessionsManager sessionManager;
    private final PlayerManager playerManager;

    public InvestigationSessionSynchronizer(OnlineSessionsManager sessionManager, PlayerManager playerManager) {
        this.sessionManager = sessionManager;
        this.playerManager = playerManager;
    }

    @EventHandler
    public void onInvestigationStarted(InvestigationStartedEvent event) {
        event.getInvestigation().getInvestigatedUuid().flatMap(playerManager::getOnlinePlayer)
            .ifPresent(s -> sessionManager.get(s.getPlayer()).setUnderInvestigation(true));
    }

    @EventHandler
    public void onInvestigationStarted(InvestigationStartedBungeeEvent event) {
        event.getInvestigation().getInvestigatedUuid().flatMap(playerManager::getOnlinePlayer)
            .ifPresent(s -> sessionManager.get(s.getPlayer()).setUnderInvestigation(true));
    }

    @EventHandler
    public void onInvestigationConcluded(InvestigationConcludedEvent event) {
        event.getInvestigation().getInvestigatedUuid().flatMap(playerManager::getOnlinePlayer)
            .ifPresent(s -> sessionManager.get(s.getPlayer()).setUnderInvestigation(false));
    }

    @EventHandler
    public void onInvestigationConcluded(InvestigationConcludedBungeeEvent event) {
        event.getInvestigation().getInvestigatedUuid().flatMap(playerManager::getOnlinePlayer)
            .ifPresent(s -> sessionManager.get(s.getPlayer()).setUnderInvestigation(false));
    }

    @EventHandler
    public void onInvestigationPaused(InvestigationPausedEvent event) {
        event.getInvestigation().getInvestigatedUuid().flatMap(playerManager::getOnlinePlayer)
            .ifPresent(s -> sessionManager.get(s.getPlayer()).setUnderInvestigation(false));
    }

    @EventHandler
    public void onInvestigationPaused(InvestigationPausedBungeeEvent event) {
        event.getInvestigation().getInvestigatedUuid().flatMap(playerManager::getOnlinePlayer)
            .ifPresent(s -> sessionManager.get(s.getPlayer()).setUnderInvestigation(false));
    }
}
