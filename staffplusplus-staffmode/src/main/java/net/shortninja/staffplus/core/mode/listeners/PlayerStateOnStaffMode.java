package net.shortninja.staffplus.core.mode.listeners;

import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.potioneffects.PotionEffectService;
import net.shortninja.staffplus.core.mode.ModeProvider;
import net.shortninja.staffplus.core.mode.config.GeneralModeConfiguration;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplusplus.staffmode.EnterStaffModeEvent;
import net.shortninja.staffplusplus.staffmode.ExitStaffModeEvent;
import net.shortninja.staffplusplus.staffmode.IModeData;
import net.shortninja.staffplusplus.staffmode.SwitchStaffModeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@IocBukkitListener
public class PlayerStateOnStaffMode implements Listener {

    private final PlayerManager playerManager;
    private final ModeProvider modeProvider;
    private final Messages messages;
    private final PotionEffectService potionEffectService;

    public PlayerStateOnStaffMode(PlayerManager playerManager,
                                  ModeProvider modeProvider,
                                  Messages messages, PotionEffectService potionEffectService) {
        this.playerManager = playerManager;
        this.modeProvider = modeProvider;
        this.messages = messages;
        this.potionEffectService = potionEffectService;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onStaffMode(EnterStaffModeEvent event) {
        playerManager.getOnlinePlayer(event.getPlayerUuid())
            .map(SppPlayer::getPlayer)
            .ifPresent(player -> {
                potionEffectService.removeAllPotionEffects(player);
                messages.send(player, messages.modeStatus.replace("%status%", messages.enabled), messages.prefixGeneral);
            });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onSwitchStaffMode(SwitchStaffModeEvent event) {
        playerManager.getOnlinePlayer(event.getPlayerUuid())
            .map(SppPlayer::getPlayer)
            .ifPresent(player -> {
                GeneralModeConfiguration modeConfiguration = modeProvider.getMode(player, event.getToMode());
                potionEffectService.removeAllPotionEffects(player);
                messages.send(player, "&eYou switched to staff mode &6" + modeConfiguration.getName(), messages.prefixGeneral);
            });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onExitMode(ExitStaffModeEvent event) {
        playerManager.getOnOrOfflinePlayer(event.getPlayerUuid())
            .map(SppPlayer::getPlayer)
            .ifPresent(player -> {
                IModeData modeData = event.getModeData();
                player.setExp(modeData.getXp());
                player.setAllowFlight(modeData.hasFlight());
                player.setGameMode(modeData.getGameMode());
                player.setFireTicks(modeData.getFireTicks());
                potionEffectService.removeAllPotionEffects(player);
                modeData.getPotionEffects().forEach(player::addPotionEffect);

                messages.send(player, messages.modeStatus.replace("%status%", messages.disabled), messages.prefixGeneral);
            });
    }
}
