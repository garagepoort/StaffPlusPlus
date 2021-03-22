package net.shortninja.staffplus.core.domain.player.listeners;

import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.application.IocContainer;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.utils.MessageCoordinator;
import net.shortninja.staffplus.core.domain.staff.alerts.xray.XrayService;
import net.shortninja.staffplus.core.domain.staff.mode.StaffModeService;
import net.shortninja.staffplus.core.domain.staff.tracing.TraceService;
import net.shortninja.staffplus.core.session.PlayerSession;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {
    private final MessageCoordinator message = IocContainer.get(MessageCoordinator.class);
    private final Options options = IocContainer.get(Options.class);
    private final Messages messages = IocContainer.get(Messages.class);
    private final SessionManagerImpl sessionManager = IocContainer.get(SessionManagerImpl.class);
    private final StaffModeService staffModeService = IocContainer.get(StaffModeService.class);
    private final TraceService traceService = IocContainer.get(TraceService.class);
    private final XrayService xrayService = IocContainer.get(XrayService.class);

    public PlayerQuit() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onQuit(PlayerQuitEvent event) {
        StaffPlus.get().versionProtocol.uninject(event.getPlayer());

        Player player = event.getPlayer();
        PlayerSession session = sessionManager.get(player.getUniqueId());
        manageUser(player);

        if(session.isVanished()) {
            event.setQuitMessage("");
        }

        if(options.modeConfiguration.isModeDisableOnLogout() && session.isInStaffMode()) {
            staffModeService.removeMode(player);
        }

        if (session.isFrozen()) {
            for (String command : options.modeConfiguration.getFreezeModeConfiguration().getLogoutCommands()) {
                command = command.replace("%player%", player.getName());
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
            }
        }

        traceService.sendTraceMessage(player.getUniqueId(), "Left the game");
        traceService.stopAllTracesForPlayer(player.getUniqueId());
        xrayService.clearTrace(player);
    }

    private void manageUser(Player player) {
        PlayerSession session = sessionManager.get(player.getUniqueId());
        if (session.isFrozen()) {
            message.sendGroupMessage(messages.freezeLogout.replace("%player%", player.getName()), options.permissionFreeze, messages.prefixGeneral);
        }
        sessionManager.unload(player.getUniqueId());
    }
}