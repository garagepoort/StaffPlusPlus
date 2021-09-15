package net.shortninja.staffplus.core.domain.staff.mode.actions;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.domain.actions.ActionService;
import net.shortninja.staffplus.core.domain.actions.CreateStoredCommandRequest;
import net.shortninja.staffplus.core.domain.actions.config.ConfiguredCommand;
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

import static java.util.Collections.emptyList;

@IocBean
@IocListener
public class StaffModeActionHook implements Listener {

    private final PlayerManager playerManager;
    private final ModeProvider modeProvider;
    private final ActionService actionService;
    private final ConfiguredCommandMapper configuredCommandMapper;

    public StaffModeActionHook(PlayerManager playerManager, ModeProvider modeProvider, ActionService actionService, ConfiguredCommandMapper configuredCommandMapper) {
        this.playerManager = playerManager;
        this.modeProvider = modeProvider;
        this.actionService = actionService;
        this.configuredCommandMapper = configuredCommandMapper;
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

        List<CreateStoredCommandRequest> commandCreateRequest = configuredCommandMapper.toCreateRequests(modeCommands, placeholders, targets, emptyList());
        actionService.createCommands(commandCreateRequest);
    }

}
