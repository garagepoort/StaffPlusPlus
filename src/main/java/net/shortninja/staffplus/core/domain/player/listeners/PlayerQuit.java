package net.shortninja.staffplus.core.domain.player.listeners;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
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

@IocBean
@IocListener
public class PlayerQuit implements Listener {

    private final Options options;
    private final Messages messages;
    private final SessionManagerImpl sessionManager;
    private final StaffModeService staffModeService;
    private final TraceService traceService;
    private final XrayService xrayService;
    private final IProtocolService protocolService;

    public PlayerQuit(Options options, Messages messages, SessionManagerImpl sessionManager, StaffModeService staffModeService, TraceService traceService, XrayService xrayService, IProtocolService protocolService) {
        this.options = options;
        this.messages = messages;
        this.sessionManager = sessionManager;
        this.staffModeService = staffModeService;
        this.traceService = traceService;
        this.xrayService = xrayService;
        this.protocolService = protocolService;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onQuit(PlayerQuitEvent event) {
        protocolService.getVersionProtocol().uninject(event.getPlayer());

        Player player = event.getPlayer();
        PlayerSession session = sessionManager.get(player.getUniqueId());

        if(session.isVanished()) {
            event.setQuitMessage("");
        }

        if(session.isInStaffMode() && session.getModeConfiguration().get().isModeDisableOnLogout()) {
            staffModeService.turnStaffModeOff(player);
        }

        if (session.isFrozen()) {
            for (String command : options.staffItemsConfiguration.getFreezeModeConfiguration().getLogoutCommands()) {
                command = command.replace("%player%", player.getName());
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
            }
        }

        traceService.sendTraceMessage(player.getUniqueId(), "Left the game");
        traceService.stopAllTracesForPlayer(player.getUniqueId());
        xrayService.clearTrace(player);
        manageUser(player);
    }

    private void manageUser(Player player) {
        PlayerSession session = sessionManager.get(player.getUniqueId());
        if (session.isFrozen()) {
            messages.sendGroupMessage(messages.freezeLogout.replace("%player%", player.getName()), options.permissionFreeze, messages.prefixGeneral);
        }
    }
}