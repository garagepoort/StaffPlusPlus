package net.shortninja.staffplus.core.domain.staff.warn.warnings;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.domain.actions.ActionService;
import net.shortninja.staffplus.core.domain.actions.config.ConfiguredCommandMapper;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.warn.threshold.ThresholdService;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplusplus.warnings.WarningAppealApprovedEvent;
import net.shortninja.staffplusplus.warnings.WarningCreatedEvent;
import net.shortninja.staffplusplus.warnings.WarningRemovedEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@IocBean
public class WarningListener implements Listener {

    private static final String CREATION_CONTEXT = "creation";

    private final ActionService actionService;
    private final PlayerManager playerManager;
    private final Options options;
    private final ThresholdService thresholdService;
    private final ConfiguredCommandMapper configuredCommandMapper;

    public WarningListener(ActionService actionService, PlayerManager playerManager, Options options, ThresholdService thresholdService, ConfiguredCommandMapper configuredCommandMapper) {
        this.actionService = actionService;
        this.playerManager = playerManager;
        this.options = options;
        this.thresholdService = thresholdService;
        this.configuredCommandMapper = configuredCommandMapper;
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }


    @EventHandler
    public void executeCreateActions(WarningCreatedEvent warningCreatedEvent) {
        UUID targetUuid = warningCreatedEvent.getWarning().getTargetUuid();
        Optional<SppPlayer> culprit = playerManager.getOnOrOfflinePlayer(targetUuid);

        Map<String, String> placeholders = new HashMap<>();
        culprit.ifPresent(sppPlayer -> placeholders.put("%culprit%", sppPlayer.getUsername()));
        Map<String, OfflinePlayer> targets = new HashMap<>();
        culprit.ifPresent(sppPlayer -> targets.put("culprit", sppPlayer.getOfflinePlayer()));

        actionService.createCommands(configuredCommandMapper.toCreateRequests(placeholders, targets, options.warningConfiguration.getActions(), Collections.singletonList(new WarningActionFilter(warningCreatedEvent.getWarning(), CREATION_CONTEXT))));
        culprit.ifPresent(sppPlayer -> thresholdService.handleThresholds(warningCreatedEvent.getWarning(), sppPlayer));
    }

    @EventHandler
    public void executeRemovalActions(WarningRemovedEvent warningRemovedEvent) {
        Bukkit.getScheduler().runTaskAsynchronously(StaffPlus.get(), () -> {
            UUID targetUuid = warningRemovedEvent.getWarning().getTargetUuid();
            Optional<SppPlayer> target = playerManager.getOnOrOfflinePlayer(targetUuid);
            if (target.isPresent()) {
                actionService.rollbackActionable(warningRemovedEvent.getWarning());
            }
        });
    }

    @EventHandler
    public void executeAppealedActions(WarningAppealApprovedEvent warningAppealApprovedEvent) {
        Bukkit.getScheduler().runTaskAsynchronously(StaffPlus.get(), () -> {
            actionService.rollbackActionable(warningAppealApprovedEvent.getWarning());
        });
    }

}
