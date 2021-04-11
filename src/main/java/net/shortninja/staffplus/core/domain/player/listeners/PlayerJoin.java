package net.shortninja.staffplus.core.domain.player.listeners;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import net.shortninja.staffplus.core.domain.actions.ActionService;
import net.shortninja.staffplus.core.domain.delayedactions.DelayedAction;
import net.shortninja.staffplus.core.domain.delayedactions.Executor;
import net.shortninja.staffplus.core.domain.delayedactions.database.DelayedActionsRepository;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.mode.StaffModeService;
import net.shortninja.staffplus.core.domain.staff.mode.config.GeneralModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.vanish.VanishServiceImpl;
import net.shortninja.staffplus.core.session.PlayerSession;
import net.shortninja.staffplus.core.session.SessionLoader;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import net.shortninja.staffplusplus.chat.NameChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@IocBean
public class PlayerJoin implements Listener {
    private final PermissionHandler permission;
    private final Options options;
    private final SessionManagerImpl sessionManager;
    private final SessionLoader sessionLoader;
    private final StaffModeService staffModeService;
    private final VanishServiceImpl vanishServiceImpl;
    private final PlayerManager playerManager;
    private final ActionService actionService;
    private final IProtocolService protocolService;

    public PlayerJoin(PermissionHandler permission, Options options, SessionManagerImpl sessionManager, SessionLoader sessionLoader, StaffModeService staffModeService, VanishServiceImpl vanishServiceImpl, PlayerManager playerManager, ActionService actionService, IProtocolService protocolService) {
        this.permission = permission;
        this.options = options;
        this.sessionManager = sessionManager;
        this.sessionLoader = sessionLoader;
        this.staffModeService = staffModeService;
        this.vanishServiceImpl = vanishServiceImpl;
        this.playerManager = playerManager;
        this.actionService = actionService;
        this.protocolService = protocolService;
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onJoin(PlayerJoinEvent event) {
        protocolService.getVersionProtocol().inject(event.getPlayer());
        playerManager.syncPlayer(event.getPlayer());

        Player player = event.getPlayer();

        manageUser(player);
        vanishServiceImpl.updateVanish();

        PlayerSession session = sessionManager.get(player.getUniqueId());
        Optional<GeneralModeConfiguration> defaultMode = staffModeService.getModeConfig(player);
        Optional<GeneralModeConfiguration> modeConfiguration = session.getModeConfiguration().isPresent() ? session.getModeConfiguration() : defaultMode;
        if (session.isInStaffMode() && modeConfiguration.isPresent() && permission.has(player, options.permissionMode)) {
            staffModeService.turnStaffModeOn(player, modeConfiguration.get());
        } else {
            staffModeService.turnStaffModeOff(player);
        }

        if (session.isVanished()) {
            event.setJoinMessage("");
        }

        sessionLoader.saveSession(session);
        delayedActions(player);
    }

    private void manageUser(Player player) {
        UUID uuid = player.getUniqueId();
        PlayerSession playerSession = sessionManager.get(uuid);

        if (!playerSession.getName().equals(player.getName())) {
            BukkitUtils.sendEventAsync(new NameChangeEvent(options.serverName, player, playerSession.getName(), player.getName()));
        }
    }

    private void delayedActions(Player player) {
        List<DelayedAction> delayedActions = StaffPlus.get().getIocContainer().get(DelayedActionsRepository.class).getDelayedActions(player.getUniqueId());
        delayedActions.forEach(delayedAction -> {
            CommandSender sender = delayedAction.getExecutor() == Executor.CONSOLE ? Bukkit.getConsoleSender() : player;
            Bukkit.dispatchCommand(sender, delayedAction.getCommand().replace("%player%", player.getName()));
            updateActionable(delayedAction);
        });
        StaffPlus.get().getIocContainer().get(DelayedActionsRepository.class).clearDelayedActions(player.getUniqueId());
    }

    private void updateActionable(DelayedAction delayedAction) {
        if (delayedAction.getExecutableActionId().isPresent()) {
            if (delayedAction.isRollback()) {
                actionService.markRollbacked(delayedAction.getExecutableActionId().get());
            } else {
                actionService.markExecuted(delayedAction.getExecutableActionId().get());
            }
        }
    }
}