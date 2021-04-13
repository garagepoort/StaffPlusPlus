package net.shortninja.staffplus.core.domain.staff.investigate;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.mode.StaffModeService;
import net.shortninja.staffplusplus.investigate.InvestigationStartedEvent;
import net.shortninja.staffplusplus.staffmode.ExitStaffModeEvent;
import net.shortninja.staffplusplus.staffmode.SwitchStaffModeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Optional;

@IocBean
@IocListener
public class StaffModeHook implements Listener {

    private final StaffModeService staffModeService;
    private final PlayerManager playerManager;
    private final Options options;
    private final InvestigationService investigationService;

    public StaffModeHook(StaffModeService staffModeService, PlayerManager playerManager, Options options, InvestigationService investigationService) {
        this.staffModeService = staffModeService;
        this.playerManager = playerManager;
        this.options = options;
        this.investigationService = investigationService;
    }

    @EventHandler
    public void handleInvestigationStarted(InvestigationStartedEvent investigationStartedEvent) {
        if (options.investigationConfiguration.isEnforceStaffMode()) {
            playerManager.getOnlinePlayer(investigationStartedEvent.getInvestigation().getInvestigatorUuid())
                .ifPresent(p -> {
                    if (options.investigationConfiguration.getStaffMode().isPresent()) {
                        staffModeService.turnStaffModeOn(p.getPlayer(), options.investigationConfiguration.getStaffMode().get());
                    } else {
                        staffModeService.turnStaffModeOn(p.getPlayer());
                    }
                });
        }
    }

    @EventHandler
    public void handleExitStaffMode(ExitStaffModeEvent exitStaffModeEvent) {
        if (options.investigationConfiguration.isEnforceStaffMode()) {
            playerManager.getOnlinePlayer(exitStaffModeEvent.getPlayerUuid()).ifPresent(p -> investigationService.tryPausingInvestigation(p.getPlayer()));
        }
    }
    @EventHandler
    public void handleExitStaffMode(SwitchStaffModeEvent switchStaffModeEvent) {
        Optional<String> staffModeConfig = options.investigationConfiguration.getStaffMode();
        if (options.investigationConfiguration.isEnforceStaffMode() && staffModeConfig.isPresent() && !staffModeConfig.get().equalsIgnoreCase(switchStaffModeEvent.getToMode())) {
            playerManager.getOnlinePlayer(switchStaffModeEvent.getPlayerUuid()).ifPresent(p -> investigationService.tryPausingInvestigation(p.getPlayer()));
        }
    }
}
