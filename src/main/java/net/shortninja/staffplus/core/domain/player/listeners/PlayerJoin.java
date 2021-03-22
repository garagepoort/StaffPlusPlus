package net.shortninja.staffplus.core.domain.player.listeners;

import net.shortninja.staffplus.core.StaffPlus;
import be.garagepoort.mcioc.IocContainer;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import net.shortninja.staffplus.core.domain.actions.ActionService;
import net.shortninja.staffplus.core.domain.delayedactions.DelayedAction;
import net.shortninja.staffplus.core.domain.delayedactions.Executor;
import net.shortninja.staffplus.core.domain.delayedactions.database.DelayedActionsRepository;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.mode.StaffModeService;
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
import java.util.UUID;

public class PlayerJoin implements Listener {
    private final PermissionHandler permission = IocContainer.get(PermissionHandler.class);
    private final Options options = IocContainer.get(Options.class);
    private final SessionManagerImpl sessionManager = IocContainer.get(SessionManagerImpl.class);
    private final SessionLoader sessionLoader = IocContainer.get(SessionLoader.class);
    private final StaffModeService staffModeService = IocContainer.get(StaffModeService.class);
    private final VanishServiceImpl vanishServiceImpl = IocContainer.get(VanishServiceImpl.class);
    private final PlayerManager playerManager = IocContainer.get(PlayerManager.class);
    private final ActionService actionService = IocContainer.get(ActionService.class);

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
        List<DelayedAction> delayedActions = IocContainer.get(DelayedActionsRepository.class).getDelayedActions(player.getUniqueId());
        delayedActions.forEach(delayedAction -> {
            CommandSender sender = delayedAction.getExecutor() == Executor.CONSOLE ? Bukkit.getConsoleSender() : player;
            Bukkit.dispatchCommand(sender, delayedAction.getCommand().replace("%player%", player.getName()));
            updateActionable(delayedAction);
        });
        IocContainer.get(DelayedActionsRepository.class).clearDelayedActions(player.getUniqueId());
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