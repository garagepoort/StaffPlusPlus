package net.shortninja.staffplus.core.domain.staff.nightvision;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.nightvision.NightVisionOffEvent;
import net.shortninja.staffplusplus.nightvision.NightVisionOnEvent;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocBukkitListener
public class NightVisionListener implements Listener {

    private final PlayerManager playerManager;

    public NightVisionListener(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @EventHandler
    public void on(NightVisionOnEvent event) {
        playerManager.getOnlinePlayer(event.getPlayer().getUniqueId())
            .ifPresent(SppPlayer::turnNightVisionOn);
    }

    @EventHandler
    public void on(NightVisionOffEvent event) {
        playerManager.getOnlinePlayer(event.getPlayer().getUniqueId())
            .ifPresent(SppPlayer::turnNightVisionOff);
    }
}
