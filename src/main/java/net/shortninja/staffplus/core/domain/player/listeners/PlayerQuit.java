package net.shortninja.staffplus.core.domain.player.listeners;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.staffplusplus.craftbukkit.common.IProtocol;
import net.shortninja.staffplus.core.StaffPlus;
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
public class PlayerQuit implements Listener {

    private final Options options;
    private final Messages messages;
    private final SessionManagerImpl sessionManager;
    private final StaffModeService staffModeService;
    private final TraceService traceService;
    private final XrayService xrayService;
    private final IProtocol versionProtocol;

    public PlayerQuit(Options options, Messages messages, SessionManagerImpl sessionManager, StaffModeService staffModeService, TraceService traceService, XrayService xrayService, IProtocol versionProtocol) {

        this.options = options;
        this.messages = messages;
        this.sessionManager = sessionManager;
        this.staffModeService = staffModeService;
        this.traceService = traceService;
        this.xrayService = xrayService;
        this.versionProtocol = versionProtocol;
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onQuit(PlayerQuitEvent event) {
        versionProtocol.uninject(event.getPlayer());

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
            messages.sendGroupMessage(messages.freezeLogout.replace("%player%", player.getName()), options.permissionFreeze, messages.prefixGeneral);
        }
        sessionManager.unload(player.getUniqueId());
    }
}