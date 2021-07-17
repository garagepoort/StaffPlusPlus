package net.shortninja.staffplus.core.domain.staff.mode.actions;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.domain.actions.ActionFilter;
import net.shortninja.staffplus.core.domain.actions.ActionService;
import net.shortninja.staffplus.core.domain.actions.ConfiguredAction;
import net.shortninja.staffplus.core.domain.actions.PermissionActionFilter;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.mode.ModeProvider;
import net.shortninja.staffplus.core.domain.staff.mode.config.GeneralModeConfiguration;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplusplus.staffmode.EnterStaffModeEvent;
import net.shortninja.staffplusplus.staffmode.ExitStaffModeEvent;
import net.shortninja.staffplusplus.staffmode.SwitchStaffModeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@IocBean
@IocListener
public class StaffModeActionListener implements Listener {

    private final PlayerManager playerManager;
    private final ModeProvider modeProvider;
    private final ActionService actionService;

    public StaffModeActionListener(PlayerManager playerManager, ModeProvider modeProvider, ActionService actionService) {
        this.playerManager = playerManager;
        this.modeProvider = modeProvider;
        this.actionService = actionService;
    }

    @EventHandler
    public void enterMode(EnterStaffModeEvent event) {
        onEnter(event.getPlayerUuid(), event.getMode());
    }

    @EventHandler
    public void exitMode(ExitStaffModeEvent event) {
        onExit(event.getPlayerUuid(), event.getMode());
    }

    @EventHandler
    public void switchMode(SwitchStaffModeEvent event) {
        onExit(event.getPlayerUuid(), event.getFromMode());
        onEnter(event.getPlayerUuid(), event.getToMode());
    }

    private void onEnter(UUID playerUuid, String toMode) {
        Optional<SppPlayer> target = playerManager.getOnOrOfflinePlayer(playerUuid);
        Optional<GeneralModeConfiguration> modeConfiguration = modeProvider.getConfiguration(toMode);
        if (target.isPresent() && modeConfiguration.isPresent()) {
            List<ActionFilter> actionFilters = Collections.singletonList(new PermissionActionFilter());
            List<ConfiguredAction> actions = modeConfiguration.get().getModeEnableCommands();
            actionService.executeActions(configuredAction -> target, actions, actionFilters, new HashMap<>());
        }
    }

    private void onExit(UUID playerUuid, String mode) {
        Optional<SppPlayer> target = playerManager.getOnOrOfflinePlayer(playerUuid);
        Optional<GeneralModeConfiguration> modeConfiguration = modeProvider.getConfiguration(mode);
        if (target.isPresent() && modeConfiguration.isPresent()) {
            List<ActionFilter> actionFilters = Collections.singletonList(new PermissionActionFilter());
            List<ConfiguredAction> actions = modeConfiguration.get().getModeDisableCommands();
            actionService.executeActions(configuredAction -> target, actions, actionFilters, new HashMap<>());
        }
    }

}
