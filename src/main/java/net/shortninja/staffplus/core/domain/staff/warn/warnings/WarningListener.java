package net.shortninja.staffplus.core.domain.staff.warn.warnings;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.IocContainer;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.domain.actions.ActionService;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.warn.threshold.ThresholdService;
import net.shortninja.staffplusplus.warnings.WarningAppealApprovedEvent;
import net.shortninja.staffplusplus.warnings.WarningCreatedEvent;
import net.shortninja.staffplusplus.warnings.WarningRemovedEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public class WarningListener implements Listener {

    private static final String CREATION_CONTEXT = "creation";

    private final ActionService actionService = IocContainer.get(ActionService.class);
    private final PlayerManager playerManager = IocContainer.get(PlayerManager.class);
    private final Options options = IocContainer.get(Options.class);
    private final ThresholdService thresholdService = IocContainer.get(ThresholdService.class);

    public WarningListener() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }


    @EventHandler
    public void executeCreateActions(WarningCreatedEvent warningCreatedEvent) {
        UUID targetUuid = warningCreatedEvent.getWarning().getTargetUuid();
        Optional<SppPlayer> target = playerManager.getOnOrOfflinePlayer(targetUuid);
        if (target.isPresent()) {
            actionService.executeActions(warningCreatedEvent.getWarning(), target.get(), options.warningConfiguration.getActions(), Arrays.asList(new WarningActionFilter(warningCreatedEvent.getWarning(), CREATION_CONTEXT)));
            thresholdService.handleThresholds(warningCreatedEvent.getWarning(), target.get());
        }
    }

    @EventHandler
    public void executeRemovalActions(WarningRemovedEvent warningRemovedEvent) {
        UUID targetUuid = warningRemovedEvent.getWarning().getTargetUuid();
        Optional<SppPlayer> target = playerManager.getOnOrOfflinePlayer(targetUuid);
        if (target.isPresent()) {
            actionService.rollbackActionable(warningRemovedEvent.getWarning());
        }
        actionService.deleteActions(warningRemovedEvent.getWarning());
    }

    @EventHandler
    public void executeAppealedActions(WarningAppealApprovedEvent warningAppealApprovedEvent) {
        UUID targetUuid = warningAppealApprovedEvent.getWarning().getTargetUuid();
        Optional<SppPlayer> target = playerManager.getOnOrOfflinePlayer(targetUuid);
        if (target.isPresent()) {
            actionService.rollbackActionable(warningAppealApprovedEvent.getWarning());
        }
    }

}
