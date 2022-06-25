package net.shortninja.staffplus.core.domain.staff.investigate;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.TubingPlugin;
import be.garagepoort.mcioc.load.BeforeTubingReload;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.bootstrap.PluginDisable;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.staff.investigate.database.investigation.InvestigationsRepository;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@IocBukkitListener
@IocMultiProvider({PluginDisable.class, BeforeTubingReload.class})
public class PauseInvestigationOnQuit implements Listener, PluginDisable, BeforeTubingReload {

    private final InvestigationService investigationService;
    private final InvestigationsRepository investigationsRepository;
    private final BukkitUtils bukkitUtils;

    public PauseInvestigationOnQuit(InvestigationService investigationService, InvestigationsRepository investigationsRepository, BukkitUtils bukkitUtils) {
        this.investigationService = investigationService;
        this.investigationsRepository = investigationsRepository;
        this.bukkitUtils = bukkitUtils;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {
        Player player = playerQuitEvent.getPlayer();
        bukkitUtils.runTaskAsync(player, () -> investigationService.tryPausingInvestigation(player));
    }

    @Override
    public void disable(StaffPlus staffPlus) {
        //disable all when plugin disabled. To ensure pausing all investigations when the server restarts.
        investigationsRepository.pauseAllInvestigations();
    }

    @Override
    public void execute(TubingPlugin tubingPlugin) {
        //disable all when plugin reloads. To ensure pausing all investigations when the server restarts.
        investigationsRepository.pauseAllInvestigations();
    }
}
