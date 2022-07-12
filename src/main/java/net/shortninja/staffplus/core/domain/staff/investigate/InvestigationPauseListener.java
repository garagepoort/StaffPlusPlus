package net.shortninja.staffplus.core.domain.staff.investigate;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@IocBukkitListener(conditionalOnProperty = "investigations-module.enabled=true && investigations-module.automatic-pause=true")
public class InvestigationPauseListener implements Listener {

    private final InvestigationService investigationService;
    private final BukkitUtils bukkitUtils;

    public InvestigationPauseListener(InvestigationService investigationService, BukkitUtils bukkitUtils) {
        this.investigationService = investigationService;
        this.bukkitUtils = bukkitUtils;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onQuit(PlayerQuitEvent event) {
        bukkitUtils.runTaskAsync(() -> investigationService.pauseInvestigationsForInvestigated(event.getPlayer()));
    }
}