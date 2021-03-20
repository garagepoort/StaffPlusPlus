package net.shortninja.staffplus.domain.player.listeners;

import net.shortninja.staffplus.application.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.common.config.Messages;
import net.shortninja.staffplus.common.config.Options;
import net.shortninja.staffplus.session.PlayerSession;
import net.shortninja.staffplus.session.SessionManagerImpl;
import net.shortninja.staffplus.domain.staff.alerts.xray.XrayService;
import net.shortninja.staffplus.domain.staff.mode.StaffModeService;
import net.shortninja.staffplus.domain.staff.tracing.TraceService;
import net.shortninja.staffplus.domain.staff.vanish.VanishServiceImpl;
import net.shortninja.staffplus.common.utils.MessageCoordinator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {
    private final MessageCoordinator message = IocContainer.getMessage();
    private final Options options = IocContainer.getOptions();
    private final Messages messages = IocContainer.getMessages();
    private final SessionManagerImpl sessionManager = IocContainer.getSessionManager();
    private final StaffModeService staffModeService = IocContainer.getModeCoordinator();
    private final VanishServiceImpl vanishServiceImpl = IocContainer.getVanishService();
    private final TraceService traceService = IocContainer.getTraceService();
    private final XrayService xrayService = IocContainer.getXrayService();

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