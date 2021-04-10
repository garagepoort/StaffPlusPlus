package net.shortninja.staffplus.core.domain.staff.investigate;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.mode.StaffModeService;
import net.shortninja.staffplusplus.investigate.InvestigationStartedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocBean
@IocListener
public class StaffModeHook implements Listener {

    private final StaffModeService staffModeService;
    private final PlayerManager playerManager;
    private final Options options;

    public StaffModeHook(StaffModeService staffModeService, PlayerManager playerManager, Options options) {
        this.staffModeService = staffModeService;
        this.playerManager = playerManager;
        this.options = options;
    }

    @EventHandler
    public void handleInvestigationStarted(InvestigationStartedEvent investigationStartedEvent) {
        if (options.investigationConfiguration.isEnforceStaffMode()) {
            playerManager.getOnlinePlayer(investigationStartedEvent.getInvestigation().getInvestigatorUuid())
                .ifPresent(p -> staffModeService.addMode(p.getPlayer()));
        }
    }
}
