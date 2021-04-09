package net.shortninja.staffplus.core.domain.staff.investigate;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.bootstrap.PluginDisable;
import net.shortninja.staffplus.core.domain.staff.investigate.database.investigation.InvestigationsRepository;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@IocBean
@IocListener
@IocMultiProvider(PluginDisable.class)
public class PauseInvestigationOnQuit implements Listener, PluginDisable {

    private final InvestigationService investigationService;
    private final InvestigationsRepository investigationsRepository;

    public PauseInvestigationOnQuit(InvestigationService investigationService, InvestigationsRepository investigationsRepository) {
        this.investigationService = investigationService;
        this.investigationsRepository = investigationsRepository;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {
        Player player = playerQuitEvent.getPlayer();
        investigationService.tryPausingInvestigation(player);
    }

    @Override
    public void disable(StaffPlus staffPlus) {
        //disable all when plugin disabled. To ensure pausing all investigations when the server restarts.
        investigationsRepository.pauseAllInvestigations();
    }
}
