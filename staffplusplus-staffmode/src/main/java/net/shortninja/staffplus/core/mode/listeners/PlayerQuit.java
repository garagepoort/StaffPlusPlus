package net.shortninja.staffplus.core.mode.listeners;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.IProtocolService;
import net.shortninja.staffplus.core.mode.StaffModeService;
import net.shortninja.staffplus.core.mode.config.GeneralModeConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;

@IocBukkitListener
public class PlayerQuit implements Listener {

    private final OnlineSessionsManager sessionManager;
    private final StaffModeService staffModeService;
    private final IProtocolService protocolService;

    public PlayerQuit(OnlineSessionsManager sessionManager,
                      StaffModeService staffModeService,
                      IProtocolService protocolService) {
        this.sessionManager = sessionManager;
        this.staffModeService = staffModeService;
        this.protocolService = protocolService;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onQuit(PlayerQuitEvent event) {
        protocolService.getVersionProtocol().uninject(event.getPlayer());
        Player player = event.getPlayer();
        OnlinePlayerSession session = sessionManager.get(player);

        Optional<GeneralModeConfiguration> modeConfig = session.get("modeConfig");
        if (session.isInStaffMode() && modeConfig.get().isModeDisableOnLogout()) {
            staffModeService.turnStaffModeOffOnQuit(player);
        }
    }
}