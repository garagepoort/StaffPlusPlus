package net.shortninja.staffplus.core.domain.staff.mode.listeners;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.settings.PlayerSettingsRepository;
import net.shortninja.staffplus.core.domain.staff.mode.ModeProvider;
import net.shortninja.staffplus.core.domain.staff.mode.StaffModeItemsService;
import net.shortninja.staffplus.core.domain.staff.mode.config.GeneralModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.custommodules.state.CustomModuleStateMachine;
import net.shortninja.staffplus.core.domain.staff.mode.custommodules.state.ModuleStateChangedEvent;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplusplus.staffmode.EnterStaffModeEvent;
import net.shortninja.staffplusplus.staffmode.ExitStaffModeEvent;
import net.shortninja.staffplusplus.staffmode.IModeData;
import net.shortninja.staffplusplus.staffmode.SwitchStaffModeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@IocBukkitListener
public class StaffItemsOnStaffMode implements Listener {

    private final PlayerManager playerManager;
    private final StaffModeItemsService staffModeItemsService;
    private final ModeProvider modeProvider;
    private final CustomModuleStateMachine customModuleStateMachine;
    private final PlayerSettingsRepository playerSettingsRepository;

    public StaffItemsOnStaffMode(PlayerManager playerManager,
                                 StaffModeItemsService staffModeItemsService,
                                 ModeProvider modeProvider, CustomModuleStateMachine customModuleStateMachine, PlayerSettingsRepository playerSettingsRepository) {
        this.playerManager = playerManager;
        this.staffModeItemsService = staffModeItemsService;
        this.modeProvider = modeProvider;
        this.customModuleStateMachine = customModuleStateMachine;
        this.playerSettingsRepository = playerSettingsRepository;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onModulesStateSwitch(ModuleStateChangedEvent event) {
        playerManager.getOnlinePlayer(event.getPlayerUuid())
            .map(SppPlayer::getPlayer)
            .ifPresent(player -> {
                GeneralModeConfiguration modeConfiguration = modeProvider.getMode(player, playerSettingsRepository.get(player).getModeName().get());
                staffModeItemsService.setStaffModeItems(player, modeConfiguration);
            });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onStaffMode(EnterStaffModeEvent event) {
        playerManager.getOnlinePlayer(event.getPlayerUuid())
            .map(SppPlayer::getPlayer)
            .ifPresent(player -> {
                GeneralModeConfiguration modeConfiguration = modeProvider.getMode(player, event.getMode());
                customModuleStateMachine.initState(player, modeConfiguration.getInitialItemStates());
                staffModeItemsService.setStaffModeItems(player, modeConfiguration);
            });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onSwitchStaffMode(SwitchStaffModeEvent event) {
        playerManager.getOnlinePlayer(event.getPlayerUuid())
            .map(SppPlayer::getPlayer)
            .ifPresent(player -> {
                GeneralModeConfiguration modeConfiguration = modeProvider.getMode(player, event.getToMode());
                customModuleStateMachine.initState(player, modeConfiguration.getInitialItemStates());
                staffModeItemsService.setStaffModeItems(player, modeConfiguration);
            });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void resetItemsOnExit(ExitStaffModeEvent event) {
        playerManager.getOnOrOfflinePlayer(event.getPlayerUuid())
            .map(SppPlayer::getPlayer)
            .ifPresent(player -> {
                customModuleStateMachine.clearState(player);
                IModeData modeData = event.getModeData();
                JavaUtils.clearInventory(player);
                player.getInventory().setContents(modeData.getPlayerInventory());
                player.updateInventory();
            });
    }
}
