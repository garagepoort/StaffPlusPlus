package net.shortninja.staffplus.core.mode.listeners;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.mode.ModeProvider;
import net.shortninja.staffplus.core.mode.config.GeneralModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.nightvision.NightVisionService;
import net.shortninja.staffplusplus.staffmode.EnterStaffModeEvent;
import net.shortninja.staffplusplus.staffmode.ExitStaffModeEvent;
import net.shortninja.staffplusplus.staffmode.SwitchStaffModeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.UUID;

@IocBukkitListener
public class NightVisionOnStaffMode implements Listener {

    private final PlayerManager playerManager;
    private final ModeProvider modeProvider;
    private final NightVisionService nightVisionService;

    public NightVisionOnStaffMode(PlayerManager playerManager, ModeProvider modeProvider, NightVisionService nightVisionService) {
        this.playerManager = playerManager;
        this.modeProvider = modeProvider;
        this.nightVisionService = nightVisionService;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void nightVisionOnStaffMode(EnterStaffModeEvent event) {
        setNightVision(event.getPlayerUuid(), event.getMode());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void nightVisionOnStaffMode(SwitchStaffModeEvent event) {
        setNightVision(event.getPlayerUuid(), event.getToMode());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void nightVisionOnStaffMode(ExitStaffModeEvent event) {
        playerManager.getOnlinePlayer(event.getPlayerUuid())
            .ifPresent(player -> nightVisionService.turnOffNightVision("STAFF_MODE", player.getPlayer()));
    }

    private void setNightVision(UUID playerUuid, String toMode) {
        playerManager.getOnlinePlayer(playerUuid)
            .ifPresent(player -> {
                GeneralModeConfiguration modeConfiguration = modeProvider.getMode(player, toMode);
                if (modeConfiguration.isNightVision()) {
                    nightVisionService.turnOnNightVision("STAFF_MODE", player.getPlayer());
                }
            });
    }
}
