package net.shortninja.staffplus.core.domain.staff.investigate;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@IocBean(conditionalOnProperty = "investigations-module.automatic-pause=true")
@IocListener
public class InvestigationPauseListener implements Listener {

    private final InvestigationService investigationService;

    public InvestigationPauseListener(InvestigationService investigationService) {
        this.investigationService = investigationService;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onQuit(PlayerQuitEvent event) {
        investigationService.pauseInvestigationsForInvestigated(event.getPlayer());
    }
}