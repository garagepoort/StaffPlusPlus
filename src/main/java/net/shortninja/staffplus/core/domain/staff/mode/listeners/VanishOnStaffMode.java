package net.shortninja.staffplus.core.domain.staff.mode.listeners;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.mode.ModeProvider;
import net.shortninja.staffplus.core.domain.staff.mode.config.GeneralModeConfiguration;
import net.shortninja.staffplus.core.domain.staff.vanish.VanishServiceImpl;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplusplus.staffmode.EnterStaffModeEvent;
import net.shortninja.staffplusplus.staffmode.ExitStaffModeEvent;
import net.shortninja.staffplusplus.staffmode.SwitchStaffModeEvent;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@IocBukkitListener
public class VanishOnStaffMode implements Listener {

    private final ModeProvider modeProvider;
    private final PlayerManager playerManager;
    private final VanishServiceImpl vanishServiceImpl;

    public VanishOnStaffMode(ModeProvider modeProvider, PlayerManager playerManager, VanishServiceImpl vanishServiceImpl) {
        this.modeProvider = modeProvider;
        this.playerManager = playerManager;
        this.vanishServiceImpl = vanishServiceImpl;
    }

    @EventHandler
    public void onEnter(EnterStaffModeEvent event) {
        playerManager.getOnlinePlayer(event.getPlayerUuid())
            .map(SppPlayer::getPlayer)
            .ifPresent(player -> {
                GeneralModeConfiguration modeConfiguration = modeProvider.getMode(player, event.getMode());
                if (modeConfiguration.isVanishOnEnter()) {
                    vanishServiceImpl.addVanish(player, modeConfiguration.getModeVanish());
                }
            });
    }

    @EventHandler
    public void onExit(ExitStaffModeEvent event) {
        if (event.getModeData().getVanishType() != VanishType.NONE) {
            playerManager.getOnlinePlayer(event.getPlayerUuid())
                .ifPresent(p -> vanishServiceImpl.addVanish(p.getPlayer(), event.getModeData().getVanishType()));
        } else {
            playerManager.getOnlinePlayer(event.getPlayerUuid())
                .ifPresent(p -> vanishServiceImpl.removeVanish(p.getPlayer()));
        }
    }

    @EventHandler
    public void onSwitch(SwitchStaffModeEvent event) {
        playerManager.getOnlinePlayer(event.getPlayerUuid())
            .map(SppPlayer::getPlayer)
            .ifPresent(player -> {
                GeneralModeConfiguration modeConfiguration = modeProvider.getMode(player, event.getToMode());
                vanishServiceImpl.removeVanish(player);
                if (modeConfiguration.isVanishOnEnter()) {
                    vanishServiceImpl.addVanish(player, modeConfiguration.getModeVanish());
                }
            });
    }
}
