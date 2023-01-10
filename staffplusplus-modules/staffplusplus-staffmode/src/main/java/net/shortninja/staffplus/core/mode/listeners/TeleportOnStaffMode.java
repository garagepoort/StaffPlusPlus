package net.shortninja.staffplus.core.mode.listeners;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.mode.ModeProvider;
import net.shortninja.staffplus.core.mode.config.GeneralModeConfiguration;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplusplus.staffmode.ExitStaffModeEvent;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Optional;

@IocBukkitListener
public class TeleportOnStaffMode implements Listener {

    private final PlayerManager playerManager;
    private final ModeProvider modeProvider;
    private final Messages messages;

    public TeleportOnStaffMode(PlayerManager playerManager, ModeProvider modeProvider, Messages messages) {
        this.playerManager = playerManager;
        this.modeProvider = modeProvider;
        this.messages = messages;
    }

    @EventHandler
    public void teleportOnExit(ExitStaffModeEvent event) {
        playerManager.getOnlinePlayer(event.getPlayerUuid())
            .map(SppPlayer::getPlayer)
            .ifPresent(player -> {
                GeneralModeConfiguration modeConfiguration = modeProvider.getMode(player, event.getMode());
                Optional<Location> previousLocation = event.getModeData().getPreviousLocation();
                if (modeConfiguration.isModeOriginalLocation() && previousLocation.isPresent()) {
                    player.teleport(previousLocation.get().setDirection(player.getLocation().getDirection()));
                    messages.send(player, messages.modeOriginalLocation, messages.prefixGeneral);
                }
                event.getTeleportToLocation().ifPresent(player::teleport);
            });
    }
}
