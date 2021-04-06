package net.shortninja.staffplus.core.domain.staff.investigate;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.common.config.Options;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@IocBean(conditionalOnProperty = "investigations-module.enabled=true")
@IocListener
public class InvestigationPauseListener implements Listener {

    private final InvestigationService investigationService;
    private final Options options;

    public InvestigationPauseListener(InvestigationService investigationService, Options options) {
        this.investigationService = investigationService;
        this.options = options;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onQuit(PlayerQuitEvent event) {
        if(options.investigationConfiguration.isAutomaticPause()) {
            investigationService.pauseInvestigationsForInvestigated(event.getPlayer());
        }
    }
}