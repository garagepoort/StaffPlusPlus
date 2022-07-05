package net.shortninja.staffplus.core.domain.staff.mode.listeners;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.mode.ModeProvider;
import net.shortninja.staffplus.core.domain.staff.mode.config.GeneralModeConfiguration;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplusplus.staffmode.EnterStaffModeEvent;
import net.shortninja.staffplusplus.staffmode.SwitchStaffModeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

@IocBukkitListener
public class FlightOnStaffMode implements Listener {

    private final PlayerManager playerManager;
    private final ModeProvider modeProvider;

    public FlightOnStaffMode(PlayerManager playerManager, ModeProvider modeProvider) {
        this.playerManager = playerManager;
        this.modeProvider = modeProvider;
    }

    @EventHandler
    public void creativeOnStaffMode(EnterStaffModeEvent event) {
        setFlight(event.getPlayerUuid(), event.getMode());
    }
    @EventHandler
    public void creativeOnStaffMode(SwitchStaffModeEvent event) {
        setFlight(event.getPlayerUuid(), event.getToMode());
    }

    private void setFlight(UUID playerUuid, String toMode) {
        playerManager.getOnlinePlayer(playerUuid)
            .map(SppPlayer::getPlayer)
            .ifPresent(player -> {
                GeneralModeConfiguration modeConfiguration = modeProvider.getMode(player, toMode);
                player.setAllowFlight(modeConfiguration.isModeFlight());
            });
    }
}
