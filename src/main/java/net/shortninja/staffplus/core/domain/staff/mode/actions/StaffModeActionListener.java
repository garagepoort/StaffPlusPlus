package net.shortninja.staffplus.core.domain.staff.mode.actions;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.domain.actions.ActionService;
import net.shortninja.staffplus.core.domain.actions.ConfiguredCommand;
import net.shortninja.staffplus.core.domain.actions.CreateStoredCommandRequest;
import net.shortninja.staffplus.core.domain.actions.PermissionActionFilter;
import net.shortninja.staffplus.core.domain.actions.config.ConfiguredCommandMapper;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.mode.ModeProvider;
import net.shortninja.staffplus.core.domain.staff.mode.config.GeneralModeConfiguration;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplusplus.staffmode.EnterStaffModeEvent;
import net.shortninja.staffplusplus.staffmode.ExitStaffModeEvent;
import net.shortninja.staffplusplus.staffmode.SwitchStaffModeEvent;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.singletonList;

@IocBean
@IocListener
public class StaffModeActionListener implements Listener {

    private final PlayerManager playerManager;
    private final ModeProvider modeProvider;
    private final ActionService actionService;
    private final ConfiguredCommandMapper configuredCommandMapper;
    private final PermissionActionFilter permissionActionFilter;

    public StaffModeActionListener(PlayerManager playerManager, ModeProvider modeProvider, ActionService actionService, ConfiguredCommandMapper configuredCommandMapper, PermissionActionFilter permissionActionFilter) {
        this.playerManager = playerManager;
        this.modeProvider = modeProvider;
        this.actionService = actionService;
        this.configuredCommandMapper = configuredCommandMapper;
        this.permissionActionFilter = permissionActionFilter;
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
        Optional<SppPlayer> staff = playerManager.getOnOrOfflinePlayer(playerUuid);
        Optional<GeneralModeConfiguration> modeConfiguration = modeProvider.getConfiguration(toMode);
        if (staff.isPresent() && modeConfiguration.isPresent()) {
            executeCommands(staff.get(), modeConfiguration.get().getModeEnableCommands());
        }
    }

    private void onExit(UUID playerUuid, String mode) {
        Optional<SppPlayer> staff = playerManager.getOnOrOfflinePlayer(playerUuid);
        Optional<GeneralModeConfiguration> modeConfiguration = modeProvider.getConfiguration(mode);
        if (staff.isPresent() && modeConfiguration.isPresent()) {
            executeCommands(staff.get(), modeConfiguration.get().getModeDisableCommands());
        }
    }

    private void executeCommands(SppPlayer staff, List<ConfiguredCommand> modeCommands) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%staff%", staff.getUsername());

        Map<String, OfflinePlayer> targets = new HashMap<>();
        targets.put("staff", staff.getOfflinePlayer());

        List<CreateStoredCommandRequest> commandCreateRequest = configuredCommandMapper.toCreateRequests(placeholders, targets, modeCommands, singletonList(permissionActionFilter));
        actionService.createCommands(commandCreateRequest);
    }

}
