package net.shortninja.staffplus.core.domain.staff.investigate.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.investigate.bungee.events.InvestigationConcludedBungeeEvent;
import net.shortninja.staffplus.core.domain.staff.investigate.bungee.events.InvestigationPausedBungeeEvent;
import net.shortninja.staffplus.core.domain.staff.investigate.bungee.events.InvestigationStartedBungeeEvent;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import net.shortninja.staffplusplus.investigate.InvestigationConcludedEvent;
import net.shortninja.staffplusplus.investigate.InvestigationPausedEvent;
import net.shortninja.staffplusplus.investigate.InvestigationStartedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocBean
@IocListener
public class InvestigationSessionSynchronizer implements Listener {

    private final SessionManagerImpl sessionManager;
    private final PlayerManager playerManager;

    public InvestigationSessionSynchronizer(SessionManagerImpl sessionManager, PlayerManager playerManager) {
        this.sessionManager = sessionManager;
        this.playerManager = playerManager;
    }

    @EventHandler
    public void onInvestigationStarted(InvestigationStartedEvent event) {
        event.getInvestigation().getInvestigatedUuid().flatMap(playerManager::getOnlinePlayer).ifPresent(s -> sessionManager.get(s.getId()).setUnderInvestigation(true));
    }

    @EventHandler
    public void onInvestigationStarted(InvestigationStartedBungeeEvent event) {
        event.getInvestigation().getInvestigatedUuid().flatMap(playerManager::getOnlinePlayer).ifPresent(s -> sessionManager.get(s.getId()).setUnderInvestigation(true));
    }

    @EventHandler
    public void onInvestigationConcluded(InvestigationConcludedEvent event) {
        event.getInvestigation().getInvestigatedUuid().flatMap(playerManager::getOnlinePlayer).ifPresent(s -> sessionManager.get(s.getId()).setUnderInvestigation(false));
    }

    @EventHandler
    public void onInvestigationConcluded(InvestigationConcludedBungeeEvent event) {
        event.getInvestigation().getInvestigatedUuid().flatMap(playerManager::getOnlinePlayer).ifPresent(s -> sessionManager.get(s.getId()).setUnderInvestigation(false));
    }

    @EventHandler
    public void onInvestigationPaused(InvestigationPausedEvent event) {
        event.getInvestigation().getInvestigatedUuid().flatMap(playerManager::getOnlinePlayer).ifPresent(s -> sessionManager.get(s.getId()).setUnderInvestigation(false));
    }

    @EventHandler
    public void onInvestigationPaused(InvestigationPausedBungeeEvent event) {
        event.getInvestigation().getInvestigatedUuid().flatMap(playerManager::getOnlinePlayer).ifPresent(s -> sessionManager.get(s.getId()).setUnderInvestigation(false));
    }
}
