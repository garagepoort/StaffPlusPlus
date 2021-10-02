package net.shortninja.staffplus.core.domain.player.listeners;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocListener;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.domain.staff.alerts.xray.XrayService;
import net.shortninja.staffplus.core.domain.staff.freeze.config.FreezeConfiguration;
import net.shortninja.staffplus.core.domain.staff.mode.StaffModeService;
import net.shortninja.staffplus.core.domain.staff.tracing.TraceService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@IocBean
@IocListener
public class PlayerQuit implements Listener {

    @ConfigProperty("permissions:freeze")
    private String permissionFreeze;

    private final Messages messages;
    private final OnlineSessionsManager sessionManager;
    private final StaffModeService staffModeService;
    private final TraceService traceService;
    private final XrayService xrayService;
    private final IProtocolService protocolService;
    private final FreezeConfiguration freezeConfiguration;

    public PlayerQuit(Messages messages,
                      OnlineSessionsManager sessionManager,
                      StaffModeService staffModeService,
                      TraceService traceService,
                      XrayService xrayService,
                      IProtocolService protocolService, FreezeConfiguration freezeConfiguration) {
        this.messages = messages;
        this.sessionManager = sessionManager;
        this.staffModeService = staffModeService;
        this.traceService = traceService;
        this.xrayService = xrayService;
        this.protocolService = protocolService;
        this.freezeConfiguration = freezeConfiguration;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onQuit(PlayerQuitEvent event) {

        protocolService.getVersionProtocol().uninject(event.getPlayer());
        Player player = event.getPlayer();
        OnlinePlayerSession session = sessionManager.get(player);

        if (session.isVanished()) {
            event.setQuitMessage("");
        }

        if (session.isFrozen()) {
            for (String command : freezeConfiguration.logoutCommands) {
                command = command.replace("%player%", player.getName());
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
            }
        }

        traceService.sendTraceMessage(player.getUniqueId(), "Left the game");
        traceService.stopAllTracesForPlayer(player.getUniqueId());
        xrayService.clearTrace(player);

        if (session.isFrozen()) {
            messages.sendGroupMessage(messages.freezeLogout.replace("%player%", player.getName()), permissionFreeze, messages.prefixGeneral);
        }

        if (session.isInStaffMode() && session.getModeConfig().get().isModeDisableOnLogout()) {
            staffModeService.turnStaffModeOff(player);
        }

    }

}