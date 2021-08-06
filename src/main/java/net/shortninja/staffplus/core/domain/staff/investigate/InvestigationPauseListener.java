package net.shortninja.staffplus.core.domain.staff.investigate;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@IocBean(conditionalOnProperty = "investigations-module.enabled=true")
@IocListener
public class InvestigationPauseListener implements Listener {

    private final InvestigationService investigationService;
    private final Options options;
    private final BukkitUtils bukkitUtils;

    public InvestigationPauseListener(InvestigationService investigationService, Options options, BukkitUtils bukkitUtils) {
        this.investigationService = investigationService;
        this.options = options;
        this.bukkitUtils = bukkitUtils;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onQuit(PlayerQuitEvent event) {
        if (options.investigationConfiguration.isAutomaticPause()) {
            bukkitUtils.runTaskAsync(() -> investigationService.pauseInvestigationsForInvestigated(event.getPlayer()));
        }
    }
}