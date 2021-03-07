package net.shortninja.staffplus.staff.warn.warnings;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.actions.ActionService;
import net.shortninja.staffplus.player.PlayerManager;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.warn.threshold.ThresholdService;
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

    private static final String REMOVAL_CONTEXT = "removal";
    private static final String CREATION_CONTEXT = "creation";
    private static final String APPEAL_APPROVED = "appeal_approved";

    private final ActionService actionService = IocContainer.getActionService();
    private final PlayerManager playerManager = IocContainer.getPlayerManager();
    private final Options options = IocContainer.getOptions();
    private final ThresholdService thresholdService = IocContainer.getThresholdService();

    public WarningListener() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }


    @EventHandler
    public void executeCreateActions(WarningCreatedEvent warningCreatedEvent) {
        UUID targetUuid = warningCreatedEvent.getWarning().getUuid();
        Optional<SppPlayer> target = playerManager.getOnOrOfflinePlayer(targetUuid);
        if (target.isPresent()) {
            actionService.executeActions(warningCreatedEvent.getWarning(), target.get(), options.warningConfiguration.getActions(), Arrays.asList(new WarningActionFilter(warningCreatedEvent.getWarning(), CREATION_CONTEXT)));
            thresholdService.handleThresholds(warningCreatedEvent.getWarning(), target.get());
        }
    }

    @EventHandler
    public void executeRemovalActions(WarningRemovedEvent warningRemovedEvent) {
        UUID targetUuid = warningRemovedEvent.getWarning().getUuid();
        Optional<SppPlayer> target = playerManager.getOnOrOfflinePlayer(targetUuid);
        if (target.isPresent()) {
            actionService.rollbackActionable(warningRemovedEvent.getWarning());
        }
        actionService.deleteActions(warningRemovedEvent.getWarning());
    }

    @EventHandler
    public void executeAppealedActions(WarningAppealApprovedEvent warningAppealApprovedEvent) {
        UUID targetUuid = warningAppealApprovedEvent.getWarning().getUuid();
        Optional<SppPlayer> target = playerManager.getOnOrOfflinePlayer(targetUuid);
        if (target.isPresent()) {
            actionService.rollbackActionable(warningAppealApprovedEvent.getWarning());
        }
    }

}
