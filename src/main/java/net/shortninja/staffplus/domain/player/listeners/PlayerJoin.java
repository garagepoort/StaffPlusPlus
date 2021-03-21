package net.shortninja.staffplus.domain.player.listeners;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.common.config.Options;
import net.shortninja.staffplus.common.utils.BukkitUtils;
import net.shortninja.staffplus.common.utils.PermissionHandler;
import net.shortninja.staffplus.domain.actions.ActionService;
import net.shortninja.staffplus.domain.delayedactions.DelayedAction;
import net.shortninja.staffplus.domain.delayedactions.Executor;
import net.shortninja.staffplus.domain.player.PlayerManager;
import net.shortninja.staffplus.domain.staff.mode.StaffModeService;
import net.shortninja.staffplus.domain.staff.vanish.VanishServiceImpl;
import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.session.SessionLoader;
import net.shortninja.staffplus.session.SessionManagerImpl;
import net.shortninja.staffplusplus.chat.NameChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;
import java.util.UUID;

public class PlayerJoin implements Listener {
    private final PermissionHandler permission = IocContainer.getPermissionHandler();
    private final Options options = IocContainer.getOptions();
    private final SessionManagerImpl sessionManager = IocContainer.getSessionManager();
    private final SessionLoader sessionLoader = IocContainer.getSessionLoader();
    private final StaffModeService staffModeService = IocContainer.getModeCoordinator();
    private final VanishServiceImpl vanishServiceImpl = IocContainer.getVanishService();
    private final PlayerManager playerManager = IocContainer.getPlayerManager();
    private final ActionService actionService = IocContainer.getActionService();

    public PlayerJoin() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onJoin(PlayerJoinEvent event) {
        StaffPlus.get().versionProtocol.inject(event.getPlayer());
        playerManager.syncPlayer(event.getPlayer());

        Player player = event.getPlayer();

        manageUser(player);
        vanishServiceImpl.updateVanish();

        PlayerSession session = sessionManager.get(player.getUniqueId());
        if (permission.has(player, options.permissionMode) && (options.modeConfiguration.isModeEnableOnLogin() || session.isInStaffMode())) {
            staffModeService.addMode(player);
        }
        if (!session.isInStaffMode()) {
            staffModeService.removeMode(player);
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
            BukkitUtils.sendEvent(new NameChangeEvent(options.serverName, player, playerSession.getName(), player.getName()));
        }
    }

    private void delayedActions(Player player) {
        List<DelayedAction> delayedActions = IocContainer.getDelayedActionsRepository().getDelayedActions(player.getUniqueId());
        delayedActions.forEach(delayedAction -> {
            CommandSender sender = delayedAction.getExecutor() == Executor.CONSOLE ? Bukkit.getConsoleSender() : player;
            Bukkit.dispatchCommand(sender, delayedAction.getCommand().replace("%player%", player.getName()));
            updateActionable(delayedAction);
        });
        IocContainer.getDelayedActionsRepository().clearDelayedActions(player.getUniqueId());
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