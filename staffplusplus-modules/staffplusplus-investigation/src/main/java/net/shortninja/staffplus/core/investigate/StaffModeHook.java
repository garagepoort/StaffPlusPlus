package net.shortninja.staffplus.core.investigate;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.investigate.config.InvestigationConfiguration;
import net.shortninja.staffplus.core.mode.StaffModeService;
import net.shortninja.staffplusplus.investigate.InvestigationStartedEvent;
import net.shortninja.staffplusplus.staffmode.ExitStaffModeEvent;
import net.shortninja.staffplusplus.staffmode.SwitchStaffModeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Optional;

@IocBukkitListener
public class StaffModeHook implements Listener {

    private final StaffModeService staffModeService;
    private final PlayerManager playerManager;
    private final InvestigationService investigationService;
    private final BukkitUtils bukkitUtils;
    private final InvestigationConfiguration investigationConfiguration;

    public StaffModeHook(StaffModeService staffModeService,
                         PlayerManager playerManager,
                         InvestigationService investigationService,
                         BukkitUtils bukkitUtils,
                         InvestigationConfiguration investigationConfiguration) {
        this.staffModeService = staffModeService;
        this.playerManager = playerManager;
        this.investigationService = investigationService;
        this.bukkitUtils = bukkitUtils;
        this.investigationConfiguration = investigationConfiguration;
    }

    @EventHandler
    public void handleInvestigationStarted(InvestigationStartedEvent investigationStartedEvent) {
        if (investigationConfiguration.isEnforceStaffMode()) {
            playerManager.getOnlinePlayer(investigationStartedEvent.getInvestigation().getInvestigatorUuid())
                .ifPresent(p -> {
                    bukkitUtils.runTaskAsync(() -> {
                        if (investigationConfiguration.getStaffMode().isPresent()) {
                            staffModeService.turnStaffModeOn(p.getPlayer(), investigationConfiguration.getStaffMode().get());
                        } else {
                            staffModeService.turnStaffModeOn(p.getPlayer());
                        }
                    });
                });
        }
    }

    @EventHandler
    public void handleExitStaffMode(ExitStaffModeEvent exitStaffModeEvent) {
        if (investigationConfiguration.isEnforceStaffMode()) {
            playerManager.getOnlinePlayer(exitStaffModeEvent.getPlayerUuid()).ifPresent(p -> investigationService.tryPausingInvestigation(p.getPlayer()));
        }
    }

    @EventHandler
    public void handleExitStaffMode(SwitchStaffModeEvent switchStaffModeEvent) {
        Optional<String> staffModeConfig = investigationConfiguration.getStaffMode();
        if (investigationConfiguration.isEnforceStaffMode() && staffModeConfig.isPresent() && !staffModeConfig.get().equalsIgnoreCase(switchStaffModeEvent.getToMode())) {
            playerManager.getOnlinePlayer(switchStaffModeEvent.getPlayerUuid()).ifPresent(p -> investigationService.tryPausingInvestigation(p.getPlayer()));
        }
    }
}
